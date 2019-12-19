package com.dharma.algo.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.google.gson.JsonObject
import com.learn.ta4j.entity.pojo.Stock
import com.learn.ta4j.entity.pojo.techstr
import com.learn.ta4j.utility.TechStrUtility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Pricefall {

    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>
    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>


    fun process(data: JsonObject): List<techstr> {

        var fallpercent = data.get("price").asString
        var sector = data.get("sector").asString

        println("--------------fall date  ${fallpercent}----------------------")


        var stock = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf("BHP.AX", "1"))
        println("--------------fall  bhp date  ${stock.size}----------------------")

        var date = stock.first().date
        println("--------------fall date  ${date}----------------------")


        var querydata = ignitecache.values(" where  date=? and changepercent < ?", arrayOf(date.toString(), fallpercent))
        println("--------------fall size ${querydata.size}----------------------")

        return querydata.filter {
            var stk = ignitecachestock.get(it.code)

            TechStrUtility.filtersector(sector)(stk)
        }.map {
            var tech = techstr(it.code, it.date, "fall > 4% ", "fall  ${"%.3f".format((it.changepercent * 100))} %")
            var stk = ignitecachestock.get(it.code)
            tech.stock = Stock(stk.code, sector, stk.name)
            tech


        }.toList()

    }


//fun getdata:String(data:Json)


}