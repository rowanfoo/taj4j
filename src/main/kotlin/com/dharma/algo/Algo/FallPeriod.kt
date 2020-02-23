package com.dharma.algo.Algo

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.ConvertUtily
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.GJson
import com.dharma.algo.utility.TechStrBuilderUtility
import com.dharma.algo.utility.TechStrUtility
import com.dharma.algo.work
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

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

//            addtechstrs(params, ::mypredicate,list)
//
//            Option.just("hello").map { it }

            techStrBuilderUtility.addtechstrs(params, ::mypredicate, ::addmesesage, ::getvaluemsg).map { xx -> list.add(xx) }


//            if (percentage > userprecentage) {
//                var tech = techstr(it, date, "Fall below period $date  for ${userprecentage * 100}", "down   ${ConvertUtily.round(percentage) * 100}%")
//                var stk = ignitecachestock.get(it)
//                var sector = stk.top ?: ""
//                if ((TechStrUtility.filtersector(usersector))(stk)) {
//                    tech.stock = Stock(stk.code, sector, stk.name)
//                    list.add(tech)
//                }
//            }
        }
        var end = System.currentTimeMillis()
        println("------DONE------${start - end}-----")
        return list
    }
//    private fun addtechstrs(data: Map<String, Any>, predicate: (params: Map<String, Any>) -> Boolean):Option<techstr> {
//
//        if (predicate(data)) {
//            return Some (techstrs(data,::addmesesage ,::getvaluemsg  ))
//        }
//        return None
//    }


//    private fun addtechstrs(data: Map<String, Any>, predicate: (params: Map<String, Any>) -> Boolean, list: MutableList<techstr>) {
//
//        if (predicate(data)) {
//            list.add(techstrs(data))
//        }
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


//    private fun techstrs(data: Map<String, Any> ,addmesesage:( Map<String, Any>)->String, getvaluemsg:( Map<String, Any>)->String): techstr {
//        var code = data.get("code") as String
//        var date = data.get("date") as LocalDate
//
//        var tech = techstr(code, date, addmesesage(data), getvaluemsg(data))
//        var stk = ignitecachestock.get(code)
//        var sector = stk.top ?: ""
//        tech.stock = Stock(stk.code, sector, stk.name)
//        return tech
//    }


}