package com.learn.ta4j.Algo

import org.springframework.stereotype.Component


@Component
class MA {


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
//        var ma = data.get("ma").asInt
//        var percentfall= data.get("precent").asDouble
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


}