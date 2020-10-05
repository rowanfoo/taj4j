package com.dharma.algo.service.algo

import arrow.syntax.function.curried
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.ConvertUtily
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.TechStrBuilderUtility
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import javax.annotation.PostConstruct


/*
find eg 6 months with 30% fall
 */
@Component
class FallPeriod {
    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>

    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>

    @Autowired
    lateinit var techStrBuilderUtility: TechStrBuilderUtility


    lateinit var setDateR: (techstr) -> techstr
    lateinit var addNewsR: (techstr) -> techstr
    lateinit var addFundamentalR: (techstr) -> techstr

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    @Autowired
    lateinit var fundamentalService: FundamentalService


    @PostConstruct
    fun init() {
        setDateR = ::setDate.curried()(coreDataIgniteService)
        addNewsR = ::addNews.curried()(newsIgniteService)
        addFundamentalR = ::addFundamental.curried()(fundamentalService)

    }


    fun process(data: JsonObject): List<techstr> {

        var date = data.get("time").asString
        var userprecentage = data.get("percent").asDouble
        var usersector = data.get("sector").asString
        var list = mutableListOf<techstr>()

        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)
        println("------sectors------${userprecentage}-----")
        var start = System.currentTimeMillis()

        stocks.keys.parallelStream().forEach {
            var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ?  ", arrayOf(it, date))
            var date = querydata.first().date
            var last = querydata.first().close
            var max = querydata.maxBy { it.close }!!.close
            var percentage = (max - last) / max
            var params = mapOf<String, Any>("code" to it, "date" to date, "userprecentage" to userprecentage, "percentage" to percentage)

            techStrBuilderUtility.addtechstrs(params, ::mypredicate, ::addmesesage, ::getvaluemsg, false).map { xx -> list.add(xx) }

        }
        var end = System.currentTimeMillis()
        println("------DONE------${start - end}-----")
        return list
    }

//    fun process1(data: JsonObject, addDate: Boolean, addNews: Boolean, addFund: Boolean): List<techstr> {
//
//        var period = data.get("time").asString
//        var userprecentage = data.get("percent").asDouble
//        var usersector = data.get("sector").asString
//
//        var setNewsC = ::bind.curried()(addNews)(addNewsR)
//
//        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)
//        return stocks.keys.parallelStream()
//                .map { ignitecache.values(" where code=?  order by date desc  LIMIT ?  ", arrayOf(it, period)) }
//                .map {
//                    var date = it.first().date
//                    var last = it.first().close
//                    var max = it.maxBy { it.close }!!.close
//                    var percentage = (max - last) / max
//                    var message = addmesesage(it.first().date, userprecentage)
//                    var valuemessage = getvaluemsg(percentage)
//
//                    setTechStr(it.first().code, message, valuemessage)
//                }
////                .peek{setNewsC(it)}
//                .toList()
//
//        //.onEach { setNewsC(it) }
////        var cache = rSiIgniteService.getCache(data)
////
////       return cache
////                .asSequence()
////                .filter { it.value.first < rsialgodata && it.value.first != 0.0 }
////                .map { setTechStr(it.key, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)") }
////                //            .onEach { setDateC(it) }
////                .onEach { setNewsC(it) }
////                .onEach { setFundC(it) }
////                .toList()
//
//
//        // return list
//
//        // .map { setTechStr(it.key, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)") }
//
//        // class techstr(var code: String, var date: LocalDate, var type: String, var message: String) {
//        //techstrs(data, addmesesage(data), getvaluemsg(data), (news))
//
//    }


    private fun mypredicate(params: Map<String, Any>): Boolean {
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

    private fun addmesesage(date: LocalDate, userprecentage: Double): String {
        return "Fall below period $date for ${userprecentage * 100}"
    }

    private fun getvaluemsg(percentage: Double): String {
        return "down ${ConvertUtily.round(percentage) * 100}%"
    }

}
