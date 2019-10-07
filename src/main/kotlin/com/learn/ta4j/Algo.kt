//
//
//package com.learn.ta4j
//
//import com.dhamma.algodata.algodata.RSI
//import com.dhamma.algodata.algodata.VolumeMA
//import com.dhamma.base.ignite.IgniteRepo
//import com.dhamma.pesistence.entity.data.CoreData
//import com.dhamma.pesistence.entity.data.CoreStock
//import com.google.gson.JsonObject
//import com.learn.ta4j.entity.pojo.Stock
//import com.learn.ta4j.entity.pojo.techstr
//import com.learn.ta4j.utility.TechStrUtility
//import org.apache.ignite.Ignite
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//import org.ta4j.core.TimeSeries
//import java.time.LocalDate
//
//class Volume(var nox: Int, series: TimeSeries) : Runnable {
//
//
//    override fun run() {
//
//
//    }
//
//
////    fun execute(): techstr{
////
////
////
////
////    }
//
//
//}
//
////
////@Component
////class AlgoDropGreater4(var date: String) {
////
////    @Autowired
////    lateinit var ignitecache: IgniteRepo<CoreData>
////    @Autowired
////    lateinit var stocklist: List<String>
////
////
////    fun process(): List<techstr> {
////
////        var querydata = ignitecache.values(" where  date=? and changepercent < -0.04 ", arrayOf(date))
////
////        var list = querydata.map {
////            techstr(it.code, it.date, "drop more than 4%", "drop  ${it.changepercent * 100}")
////
////        }.toList()
////        return list
////    }
////
////
////}
////
////
//@Component
//class VolumeX {
//
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>
//
//    @Autowired
//    lateinit var ignite: Ignite
//
//    @Autowired
//    lateinit var stocklist: List<String>
//
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>
//
//
//    @Autowired
//    lateinit var volumeMA: VolumeMA
//
//    fun process(data: JsonObject): List<techstr> {
//
//        println("-----------------ALGO----------------volumex---------")
//        var volumema = data.get("volumema").asInt
//        var volumex = data.get("volumex").asDouble
//        var usersector = data.get("sector").asString
//
//        //var querydata = ignitecache.values(" where  date=?  ", arrayOf(date))
//
//        var cache3 = ignite.getOrCreateCache<String, Double>("MA$volumema:vol")
//        println("-----------------ALGO----volumex------${cache3.size()}---")
//        if (cache3.size() == 0) {
//            volumeMA.process(data)
//        }
//        println("-----------------ALGO----volumex--done----${cache3.size()}---")
//
//        var list = mutableListOf<techstr>()
//
//        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)
//
//        stocks.keys.forEach {
//
//            var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf(it, "1"))
//            var coreData = querydata.first()
//            var volume = coreData.volume
//            var avgvol = cache3.get(it)
//            if ((volume / avgvol) > volumex) {
//                println("-----------------ALGO----volumex--selected ----${(volume / avgvol)}----vs $volumex-------------$it")
//
//
//                var tech = techstr(it, coreData.date, "vol", "volume   ${"%.2f".format((coreData.volume / avgvol))}  **  ${"%.2f".format(coreData.changepercent)}")
//                var stk = ignitecachestock.get(it)
//                var sector = stk.top ?: ""
////                if ((TechStrUtility.filtersector(sector))(stk)) {
//                tech.stock = Stock(stk.code, sector, stk.name)
//                list.add(tech)
//                // }
//
//
//            }
//
//
//        }
//
//
//
//
//        return list
//    }
//
//
//}
//
//
//@Component
//class Pricefall {
//
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>
//
//
//    fun process(data: JsonObject): List<techstr> {
//
//        var fallpercent = data.get("price").asString
//        var sector = data.get("sector").asString
//
//        println("--------------fall date  ${fallpercent}----------------------")
//
//
//        var stock = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf("BHP.AX", "1"))
//        println("--------------fall  bhp date  ${stock.size}----------------------")
//
//        var date = stock.first().date
//        println("--------------fall date  ${date}----------------------")
//
//
//        var querydata = ignitecache.values(" where  date=? and changepercent < ?", arrayOf(date.toString(), fallpercent))
//        println("--------------fall size ${querydata.size}----------------------")
//
//        return querydata.filter {
//            var stk = ignitecachestock.get(it.code)
//
//            TechStrUtility.filtersector(sector)(stk)
//        }.map {
//            var tech = techstr(it.code, it.date, "fall > 4% ", "fall  ${"%.3f".format((it.changepercent * 100))} %")
//            var stk = ignitecachestock.get(it.code)
//            tech.stock = Stock(stk.code, sector, stk.name)
//            tech
//
//
//        }.toList()
//
//    }
//
//
//}
//
//@Component
//class RSIAlgo {
//
//
//    @Autowired
//    lateinit var ignite: Ignite
//
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>
//
//    @Autowired
//    lateinit var rsialgo: RSI
//
//    fun process(data: JsonObject): List<techstr> {
//
////        var date = data.get("time").asString
////        var price = data.get("precent").asString
//        var list = mutableListOf<techstr>()
//        var rsidata = data.get("rsi").asInt
//        var rsialgodata = data.get("rsialgo").asInt
//        var usertop = data.get("sector").asString
//
//        if (ignite.getOrCreateCache<String, Pair<Double, String>>("RSI$rsidata").size() == 0) {
//            rsialgo.loadall(data)
//        }
//
//
//
//        println("----RSI----SIZE ---------${ignite.getOrCreateCache<String, Double>("RSI14").size()}------------vs ------------------------")
//
//        ignite.getOrCreateCache<String, Pair<Double, String>>("RSI14").forEach {
//            println("----RSI ---------${it.key}------------vs ---------${it.value}---------------")
//
//            if (it.value.first < rsialgodata && it.value.first != 0.0) {
//                println("----ADD------------RSI ---------${it.key}------------vs ---------${it.value}---------------")
//                var tech = techstr(it.key, LocalDate.now(), "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)")
//                var stk = ignitecachestock.get(it.key)
//                var sector = stk.top ?: ""
//                if ((TechStrUtility.filtersector(usertop))(stk)) {
//                    tech.stock = Stock(stk.code, sector, stk.name)
//                    list.add(tech)
//                }
//
//
////                tech.stock = Stock(stk.code, sector, stk.name)
////                list.add(tech)
//
//            }
//
//
//        }
//
//        return list
//
//    }
//
//
//}
//
//
//@Component
//class FallPeriod {
//    @Autowired
//    lateinit var ignite: Ignite
//
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>
//
//
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>
//
//    fun process(data: JsonObject): List<techstr> {
//
//        var date = data.get("time").asString
//        var userprecentage = data.get("percent").asDouble
//        var usersector = data.get("sector").asString
//
//
//        var list = mutableListOf<techstr>()
//
//
//        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)
////        println("------sectors------${stocks.count()}-----")
//        println("------sectors------${userprecentage}-----")
//        var start = System.currentTimeMillis()
//
//        stocks.keys.parallelStream().forEach {
//
//
//            var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ?  ", arrayOf(it, date))
//            var last = querydata.first().close
//            var max = querydata.maxBy { it.close }!!.close
//
//            var percentage = (max - last) / max
//            println("------stocks------$it--------$last--------vs---$max------$percentage---  ${percentage > userprecentage}")
//
//            if (percentage > userprecentage) {
//
//
//                var tech = techstr(it, LocalDate.now(), "Fall below period $date  for ${userprecentage * 100}", "down   ${ConvertUtily.round(percentage) * 100}%")
//                var stk = ignitecachestock.get(it)
//                var sector = stk.top ?: ""
//                if ((TechStrUtility.filtersector(usersector))(stk)) {
//                    tech.stock = Stock(stk.code, sector, stk.name)
//                    list.add(tech)
//                }
//
//
//            }
//        }
//
//
//        var end = System.currentTimeMillis()
//
//        println("------DONE------${start - end}-----")
//        return list
//
//
//    }
//
//
//    private fun filtersector(sector: String): (CoreStock) -> Boolean {
//
//        lateinit var tech: (CoreStock) -> Boolean
//
//        if (sector == "100") tech = { it -> it.top == "100" || it.top == "50" }
//        if (sector == "200") tech = { it -> it.top == "100" || it.top == "50" || it.top == "200" }
//        if (sector == "300") tech = { it -> it.top == "100" || it.top == "50" || it.top == "200" || it.top == "300" }
//
//
//        return tech
//
//    }
//
//}