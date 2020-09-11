package com.dharma.algo.service.algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.data.pojo.techstr
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Pricefall : BaseAlgo(), IProcess {
    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>

    override fun process(data: JsonObject): List<techstr> {
        var fallpercent = data.get("price").asString
        var sector = data.get("sector").asString

        println("--------------fall date  ${fallpercent}----------------------")
        //this is only to get today stock  , as stock could be on SAT or SUN
        var date = coreDataIgniteService.today("BHP.AX").date
        var cache = coreDataIgniteService.changePercentlt(date.toString(), fallpercent)

        return cache
                .asSequence()
                .map { setTechStr(it.code, "fall > 4%", "fall  ${"%.3f".format((it.changepercent * 100))} %") }
                .onEach { setNewsC(it) }
                .onEach { setFundC(it) }
                .onEach { setStockC(it) }
                .toList()

    }
}

