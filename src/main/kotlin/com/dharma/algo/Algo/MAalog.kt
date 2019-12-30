package com.dharma.algo.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.service.algodata.CoreDataService
import com.dhamma.service.algodata.MA
import com.dhamma.service.algodata.NewsService
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
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
    @Autowired
    lateinit var newsService: NewsService

    @Autowired
    lateinit var coreDataService: CoreDataService

    fun process(data: JsonObject): List<techstr> {
        var percent = data.get("percent").asDouble
        var usertop = data.get("sector").asString
        var array: IgniteCache<String, Double> = ma.getCache(data)
        var list = mutableListOf<techstr>()
        var date = coreDataService.today("BHP.AX").date
        array.forEach {

            //            var today = DataUtility.todayData(it.key).close

            var today = coreDataService.today(it.key).close
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

                var a = JsonObject()
                a.addProperty("code", stk.code)
                a.addProperty("date", date.toString())
                tech.news = newsService.getCode(a)


            }
        }
        return list
    }
}