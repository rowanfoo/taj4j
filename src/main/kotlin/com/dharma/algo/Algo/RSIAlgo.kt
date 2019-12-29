package com.dharma.algo.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.service.algodata.CoreDataService
import com.dhamma.service.algodata.NewsService
import com.dhamma.service.algodata.RSI
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
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
    lateinit var rsialgo: RSI

    @Autowired
    lateinit var newsService: NewsService

    @Autowired
    lateinit var coreDataService: CoreDataService

    fun process(data: JsonObject): List<techstr> {
        var list = mutableListOf<techstr>()
        var rsidata = data.get("rsi").asInt
        var rsialgodata = data.get("rsialgo").asInt
        var usertop = data.get("sector").asString

        var cache = rsialgo.getCache(data)
        var date = coreDataService.today("BHP.AX").date

        println("----RSI----SIZE ---------${cache.size()}------------vs ------------------------")

        cache.forEach {
            if (it.value.first < rsialgodata && it.value.first != 0.0) {
                println("----ADD------------RSI ---------${it.key}------------vs ---------${it.value}---------------")
                var tech = techstr(it.key, LocalDate.now(), "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)")
                var stk = ignitecachestock.get(it.key)
                var sector = stk.top ?: ""
                if ((TechStrUtility.filtersector(usertop))(stk)) {
                    tech.stock = Stock(stk.code, sector, stk.name)
                    list.add(tech)
                }
                //
                var a = JsonObject()
                a.addProperty("code", stk.code)
                a.addProperty("date", date.toString())
                tech.news = newsService.getCode(a)
            }
        }
        return list
    }
}