package com.dharma.algo.service.algo

import com.dhamma.ignitedata.service.MaIgniteService
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.GJson
import com.dharma.algo.utility.Maths
import com.google.gson.JsonObject
import org.apache.ignite.IgniteCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class MAalog : BaseAlgo(), IProcess {
    @Autowired
    lateinit var maIgniteService: MaIgniteService

    override fun process(data: JsonObject): List<techstr> {
        var percent = data.get("percent").asDouble
        var usertop = data.get("sector").asString

        var array: IgniteCache<String, Double> = maIgniteService.getCache(data)
        var list = mutableListOf<techstr>()
        var date = coreDataIgniteService.today("BHP.AX").date



        return array
                .asSequence()
                .map {

                    var today = coreDataIgniteService.today(it.key).close
                    GJson.toGson(mapOf("precentage" to Maths.percent(today, it.value), "code" to it.key, "today" to today, "maprice" to it.value))
                }

                .filter {
                    it["precentage"].asDouble < percent
                }
                .map {
//                    println("----ADD------------MA ALGO ---------${it["code"].asString}------------vs ---------${it["today"].asString}------------------")
                    techstr(it["code"].asString, date, "MA", "today  ${it["today"].asString}----- : ${it["precentage"].asString}%)")
                }
                .onEach { setNewsC(it) }
                .onEach { setFundC(it) }
                .onEach { setStockC(it) }
                .toList()
    }
}
