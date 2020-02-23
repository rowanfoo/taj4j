package com.dharma.algo.Algo

import arrow.core.Option
import arrow.core.toOption
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.ignitedata.service.RSiIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.ConvertUtily
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.GJson
import com.dharma.algo.utility.TechStrBuilderUtility
import com.dharma.algo.utility.TechStrUtility
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class RSIAlgo {
    @Autowired
    lateinit var ignite: Ignite
    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>
    @Autowired
    lateinit var rSiIgniteService: RSiIgniteService

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService
    @Autowired
    lateinit var  techStrBuilderUtility : TechStrBuilderUtility

    fun process(data: JsonObject): List<techstr> {
        var list = mutableListOf<techstr>()
        //var rsidata = data.get("rsi").asInt
        var rsialgodata = data.get("rsialgo").asInt
        var usertop = data.get("sector").asString

        var cache = rSiIgniteService.getCache(data)
        var date = coreDataIgniteService.today("BHP.AX").date

        println("----RSI----SIZE ---------${cache.size()}------------vs ------------------------")

        cache.forEach {
            if (it.value.first < rsialgodata && it.value.first != 0.0) {



                println("----ADD------------RSI ---------${it.key}------------vs ---------${it.value}---------------")


                techStrBuilderUtility.techstrsNews()

                var tech = techstr(it.key, date, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)")
                var stk = ignitecachestock.get(it.key)
                var a = GJson.toGson(mapOf("code" to stk.code, "date" to date.toString()))
//                var sector = stk.top ?: ""
//                if ((TechStrUtility.filtersector(usertop))(stk)) {
//                    tech.stock = Stock(stk.code, sector, stk.name)
//                    list.add(tech)
//                }
                //
                var a = JsonObject()
                a.addProperty("code", stk.code)
                a.addProperty("date", date.toString())
                tech.news = newsIgniteService.getCode(a)
            }
        }
        return list
    }



    private fun mypredicate(params: Map<String, Any>):Boolean {
        var percentage = params.get("percentage") as Double
        var userprecentage = params.get("userprecentage") as Double
        return (percentage > userprecentage)
    }
    private fun addmesesage(data: Map<String, Any>): String {
        var userprecentage = data.get("userprecentage") as Double
        var date = data.get("date") as LocalDate
        return "Fall below period $date for ${userprecentage * 100}"
    }

    private fun getvaluemsg(data: Map<String, Any>): String {
        var percentage = data.get("percentage") as Double
        return "down ${ConvertUtily.round(percentage) * 100}%"
    }



}