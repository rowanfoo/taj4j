package com.learn.ta4j.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.google.gson.JsonObject
import com.learn.ta4j.ConvertUtily
import com.learn.ta4j.entity.pojo.Stock
import com.learn.ta4j.entity.pojo.techstr
import com.learn.ta4j.utility.TechStrUtility
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

    fun process(data: JsonObject): List<techstr> {

        var date = data.get("time").asString
        var userprecentage = data.get("percent").asDouble
        var usersector = data.get("sector").asString


        var list = mutableListOf<techstr>()


        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)
//        println("------sectors------${stocks.count()}-----")
        println("------sectors------${userprecentage}-----")
        var start = System.currentTimeMillis()

        stocks.keys.parallelStream().forEach {


            var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ?  ", arrayOf(it, date))
            var last = querydata.first().close
            var max = querydata.maxBy { it.close }!!.close

            var percentage = (max - last) / max
            println("------stocks------$it--------$last--------vs---$max------$percentage---  ${percentage > userprecentage}")

            if (percentage > userprecentage) {


                var tech = techstr(it, LocalDate.now(), "Fall below period $date  for ${userprecentage * 100}", "down   ${ConvertUtily.round(percentage) * 100}%")
                var stk = ignitecachestock.get(it)
                var sector = stk.top ?: ""
                if ((TechStrUtility.filtersector(usersector))(stk)) {
                    tech.stock = Stock(stk.code, sector, stk.name)
                    list.add(tech)
                }


            }
        }


        var end = System.currentTimeMillis()

        println("------DONE------${start - end}-----")
        return list


    }


    private fun filtersector(sector: String): (CoreStock) -> Boolean {

        lateinit var tech: (CoreStock) -> Boolean

        if (sector == "100") tech = { it -> it.top == "100" || it.top == "50" }
        if (sector == "200") tech = { it -> it.top == "100" || it.top == "50" || it.top == "200" }
        if (sector == "300") tech = { it -> it.top == "100" || it.top == "50" || it.top == "200" || it.top == "300" }


        return tech

    }

}