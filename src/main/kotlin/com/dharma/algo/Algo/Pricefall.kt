package com.dharma.algo.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.TechStrUtility
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Pricefall {
    //    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>
    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>


    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    fun process(data: JsonObject): List<techstr> {

        var fallpercent = data.get("price").asString
        var sector = data.get("sector").asString

        println("--------------fall date  ${fallpercent}----------------------")
        //this is only to get today stock  , as stock could be on SAT or SUN
        var date = coreDataIgniteService.today("BHP.AX").date
//        var querydata = ignitecache.values(" where  date=? and changepercent < ?", arrayOf(date.toString(), fallpercent))
        var querydata = coreDataIgniteService.changePercentlt(date.toString(), fallpercent)

        return querydata.filter {
            var stk = ignitecachestock.get(it.code)

            TechStrUtility.filtersector(sector)(stk)
        }.map {
            var tech = techstr(it.code, it.date, "fall > 4% ", "fall  ${"%.3f".format((it.changepercent * 100))} %")
            var stk = ignitecachestock.get(it.code)
            tech.stock = Stock(stk.code, sector, stk.name)
            //tech
            var a = JsonObject()
            a.addProperty("code", stk.code)
            a.addProperty("date", date.toString())
            tech.news = newsIgniteService.getCode(a)
            tech


        }.toList()

    }
}
