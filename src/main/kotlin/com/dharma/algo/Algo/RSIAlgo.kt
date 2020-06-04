package com.dharma.algo.Algo

import arrow.syntax.function.curried
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.ignitedata.service.RSiIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.TechStrBuilderUtility
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


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
    lateinit var techStrBuilderUtility: TechStrBuilderUtility
    @Autowired
    lateinit var fundamentalService: FundamentalService


    lateinit var setDateR: (techstr) -> techstr
    lateinit var addNewsR: (techstr) -> techstr
    lateinit var addFundamentalR: (techstr) -> techstr

    fun process(data: JsonObject): List<techstr> {
        var list = mutableListOf<techstr>()
        //var rsidata = data.get("rsi").asInt
        var rsialgodata = data.get("rsialgo").asInt
        var usertop = data.get("sector").asString

        var cache = rSiIgniteService.getCache(data)
        // var date = coreDataIgniteService.today("BHP.AX").date

        println("----RSI----SIZE ---------${cache.size()}------------vs ------------------------")

        cache.forEach {
            if (it.value.first < rsialgodata && it.value.first != 0.0) {


                println("----ADD------------RSI ---------${it.key}------------vs ---------${it.value}---------------")
                //  var params = mapOf<String, Any>("code" to it.key, "date" to date)

                //  list.add(techStrBuilderUtility.techstrs(params, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)", true))

//                var tech = techstr(it.key, date, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)")
//                var stk = ignitecachestock.get(it.key)
//                var a = GJson.toGson(mapOf("code" to stk.code, "date" to date.toString()))
////                var sector = stk.top ?: ""
////                if ((TechStrUtility.filtersector(usertop))(stk)) {
////                    tech.stock = Stock(stk.code, sector, stk.name)
////                    list.add(tech)
////                }
//                //
//                var a = JsonObject()
//                a.addProperty("code", stk.code)
//                a.addProperty("date", date.toString())
//                tech.news = newsIgniteService.getCode(a)
            }
        }
        return list
    }


    @PostConstruct
    fun init() {
        setDateR = ::setDate.curried()(coreDataIgniteService)
        addNewsR = ::addNews.curried()(newsIgniteService)
        addFundamentalR = ::addFundamental.curried()(fundamentalService)

    }

    fun process1(data: JsonObject, addDate: Boolean, addNews: Boolean, addFund: Boolean): List<techstr> {
        // var list
        //var rsidata = data.get("rsi").asInt
        var rsialgodata = data.get("rsialgo").asInt
        var usertop = data.get("sector").asString

        var setDateC = ::bind.curried()(addDate)(setDateR)
        var setNewsC = ::bind.curried()(addNews)(addNewsR)
        var setFundC = ::bind.curried()(addFund)(addFundamentalR)

        var cache = rSiIgniteService.getCache(data)
// for now not using filter of sector
        return cache
                .asSequence()
                .filter { it.value.first < rsialgodata && it.value.first != 0.0 }
                .map { setTechStr(it.key, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)") }
                //            .onEach { setDateC(it) }
                .onEach { setNewsC(it) }
                .onEach { setFundC(it) }
                .toList()


        // return list
    }
}

