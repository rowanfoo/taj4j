package com.dharma.algo.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.service.algodata.MA
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.DataUtility
import com.dharma.algo.utility.Maths
import com.dharma.algo.utility.TechStrUtility
import com.google.gson.JsonObject
import org.apache.ignite.IgniteCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class MAalog {
    @Autowired
    lateinit var ma: MA
    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>

    fun process(data: JsonObject): List<techstr> {
        var percent = data.get("percent").asDouble
        var usertop = data.get("sector").asString
        var array: IgniteCache<String, Double> = ma.getCache(data)
        var list = mutableListOf<techstr>()

        array.forEach {
            var today = DataUtility.todayData(it.key).close
            var value = Maths.percent(today, it.value)

            if (value < percent) {
                println("----ADD------------RSI ---------${it.key}------------vs ---------${it.value}----------------$value---")
                var tech = techstr(it.key, LocalDate.now(), "MA", "today  ${today}----- : {$value}%)")
                var stk = ignitecachestock.get(it.key)
                var sector = stk.top ?: ""
                if ((TechStrUtility.filtersector(usertop))(stk)) {
                    tech.stock = Stock(stk.code, sector, stk.name)
                    list.add(tech)
                }
            }
        }
        return list
    }
}