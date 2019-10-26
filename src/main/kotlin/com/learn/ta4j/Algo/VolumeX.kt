package com.learn.ta4j.Algo

import com.dhamma.algodata.algodata.VolumeMA
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.google.gson.JsonObject
import com.learn.ta4j.ConvertUtily
import com.learn.ta4j.entity.pojo.Stock
import com.learn.ta4j.entity.pojo.techstr
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VolumeX {

    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>

    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var stocklist: List<String>

    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>


    @Autowired
    lateinit var volumeMA: VolumeMA

    fun process(data: JsonObject): List<techstr> {

        println("-----------------ALGO----------------volumex---------")
        var volumema = data.get("volumema").asInt
        var volumex = data.get("volumex").asDouble
        var usersector = data.get("sector").asString

        //var querydata = ignitecache.values(" where  date=?  ", arrayOf(date))

        var cache3 = ignite.getOrCreateCache<String, Double>("MA$volumema:vol")
        println("-----------------ALGO----volumex------${cache3.size()}---")
        if (cache3.size() == 0) {
            volumeMA.process(data)
        }
        println("-----------------ALGO----volumex--done----${cache3.size()}---")

        var list = mutableListOf<techstr>()

        var stocks = ConvertUtily.filterTop(ignitecachestock, usersector)

        stocks.keys.forEach {

            var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf(it, "1"))
            var coreData = querydata.first()
            var date = coreData.date
            var volume = coreData.volume
            var avgvol = cache3.get(it)
            if ((volume / avgvol) > volumex) {
                println("-----------------ALGO----volumex--selected ----${(volume / avgvol)}----vs $volumex-------------$it")


                var tech = techstr(it, date, "vol", "volume   ${"%.2f".format((coreData.volume / avgvol))}  **  ${"%.2f".format(coreData.changepercent)}")
                var stk = ignitecachestock.get(it)
                var sector = stk.top ?: ""
                tech.stock = Stock(stk.code, sector, stk.name)
                list.add(tech)
            }


        }




        return list
    }


}