package com.dharma.algo.Algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.ignitedata.service.VolumeMaIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.ConvertUtily
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VolumeX {

//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>


    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var stocklist: List<String>

    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>


    @Autowired
    lateinit var volumeMaIgniteService: VolumeMaIgniteService

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    fun process(data: JsonObject): List<techstr> {

        println("-----------------ALGO----------------volumex---------")
      //  var volumema = data.get("volumema").asInt
        var volumex = data.get("volumex").asDouble
        var usersector = data.get("sector").asString
        var list = mutableListOf<techstr>()

        var cache3 = volumeMaIgniteService.getCache(data)
        println("-----------------ALGO----volumex--done----${cache3.size()}---")

        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)

        println("-----------------ALGO----volumex--stocks----${stocks.size}---")

        stocks.keys.forEach {
            //            var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf(it, "1"))
//            var coreData = querydata.first()
            var coreData = coreDataIgniteService.today(it)
            var date = coreData.date
            var volume = coreData.volume
            var avgvol = cache3.get(it)
            if ((volume / avgvol) > volumex) {
                println("-----------------ALGO----volumex--selected ----${(volume / avgvol)}----$volume ---vs -----$avgvol------vs $volumex-------------$it")
                var tech = techstr(it, date, "vol", "volume   ${"%.2f".format((coreData.volume / avgvol))}  **  ${"%.2f".format(coreData.changepercent)}")
                var stk = ignitecachestock.get(it)
                var sector = stk.top ?: ""
                tech.stock = Stock(stk.code, sector, stk.name)
                var a = JsonObject()
                a.addProperty("code", it)
                a.addProperty("date", date.toString())
                tech.news = newsIgniteService.getCode(a)
                list.add(tech)

            }


        }
        return list
    }

}