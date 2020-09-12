package com.dharma.algo.service.algo

import arrow.syntax.function.curried
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.VolumeMaIgniteService
import com.dhamma.manager.VolManager
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.GJson
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VolumeX : BaseAlgo(), IProcess {
    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var stocklist: List<String>

    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>

    @Autowired
    lateinit var volumeMaIgniteService: VolumeMaIgniteService

    @Autowired
    lateinit var volmag: VolManager

    override fun process(data: JsonObject): List<techstr> {

        var usertop = data.get("sector").asString

        var volumex = data.get("volumex").asDouble

        var mypredicateR = ::mypredicate.curried()(volumex)
        var cache = volmag.today(data)

        // for now not using filter of sector
        return cache
                .asSequence()
                .map {
                    var z = getPrice(it.key)
                    z.put("avgvol", it.value)
                    GJson.toGson(z)
                }
                .filter(mypredicateR)
                .map { setTechStr(it["code"].asString, "VOl", getvaluemsg(it)) }
                //            .onEach { setDateC(it) }
                .onEach { setNewsC(it) }
                .onEach { setFundC(it) }
                .onEach { setStockC(it) }
                .toList()
    }


    private fun getPrice(code: String): MutableMap<String, Any> {
        var coreData = coreDataIgniteService.today(code)
        var date = coreData.date
        var volume = coreData.volume.toDouble()
        return mutableMapOf<String, Any>("code" to code, "date" to date as Any, "volume" to volume, "changepercent" to coreData.changepercent)
    }

    private fun mypredicate(volumex: Double, params: JsonObject): Boolean {
        var volume = params["volume"].asDouble
        var avgvol = params["avgvol"].asDouble
        return ((volume / avgvol) > volumex)
    }

    private fun getvaluemsg(params: JsonObject): String {
        var volume = params["volume"].asDouble
        var avgvol = params["avgvol"].asDouble
        var changepercent = params["changepercent"].asDouble
        return "volume   ${"%.2f".format((volume / avgvol))}  **  ${"%.2f".format(changepercent)}"
    }
}



