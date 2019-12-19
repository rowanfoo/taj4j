package com.dharma.algo.Algo

import com.dhamma.algodata.algodata.MA
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.google.gson.JsonObject
import com.learn.ta4j.entity.pojo.Stock
import com.learn.ta4j.entity.pojo.techstr
import com.learn.ta4j.utility.Maths
import com.learn.ta4j.utility.TechStrUtility
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class MAalog {


    @Autowired
    lateinit var ignite: Ignite
    @Autowired
    lateinit var ma: MA

    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>

    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>


    //
    fun process(data: JsonObject): List<techstr> {

//        var date = data.get("time").asString
//        var price = data.get("precent").asString
//        var list = mutableListOf<techstr>()
//        var ma = data.get("ma").asInt
//        var percentfall = data.get("precent").asDouble
//
        var percent = data.get("percent").asDouble
        var usertop = data.get("sector").asString
        var array: IgniteCache<String, Double> = ma.getCache(data)

        var list = mutableListOf<techstr>()



        array.forEach {
            // println("----RSI ---------${it.key}------------vs ---------${it.value}---------------")


            var today = getTOdayPrice(it.key)
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


//                tech.stock = Stock(stk.code, sector, stk.name)
//                list.add(tech)

            }


        }

        return list

    }

    fun getTOdayPrice(code: String): Double {
        var today = ignitecache.values(" where code=?   order by date desc limit ?  ", arrayOf(code, "1"))
        return today[0].close

    }

}