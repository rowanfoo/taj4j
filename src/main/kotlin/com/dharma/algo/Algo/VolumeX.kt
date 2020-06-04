package com.dharma.algo.Algo

import arrow.syntax.function.curried
import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.ignitedata.service.VolumeMaIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.TechStrBuilderUtility
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class VolumeX {
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

    @Autowired
    lateinit var techStrBuilderUtility: TechStrBuilderUtility


    fun process1(data: JsonObject): List<techstr> {

        var volumex = data.get("volumex").asDouble
        var usersector = data.get("sector").asString
        var list = mutableListOf<techstr>()

        var cache3 = volumeMaIgniteService.getCache(data)

        var mypredicateR = ::mypredicate.curried()(volumex)

        var ls = cache3
                .map {
                    var z = getPrice(it.key)
                    z.put("avgvol", it.value)
                    z
                }
                .filter(mypredicateR)
                .map {
                    techstr(it["code"] as String, it["date"] as LocalDate,
                            "vol", getvaluemsg(it))
                }
                .toList()
        return ls
    }


    private fun getPrice(code: String): MutableMap<String, Any> {
        var coreData = coreDataIgniteService.today(code)
        var date = coreData.date
        var volume = coreData.volume.toDouble()
        return mutableMapOf<String, Any>("code" to code, "date" to date as Any, "volume" to volume, "changepercent" to coreData.changepercent)
    }


    private fun mypredicate(volumex: Double, params: MutableMap<String, Any>): Boolean {
        var volume = params["volume"] as Double
        var avgvol = params["avgvol"] as Double
        return ((volume / avgvol) > volumex)
    }


    private fun addmesesage(params: Map<String, Any>): String {
        return "vol"
    }

    private fun getvaluemsg(params: Map<String, Any>): String {
        var volume = params.get("volume") as Double
        var avgvol = params.get("avgvol") as Double
        var changepercent = params.get("changepercent") as Double
        return "volume   ${"%.2f".format((volume / avgvol))}  **  ${"%.2f".format(changepercent)}"
    }
}
