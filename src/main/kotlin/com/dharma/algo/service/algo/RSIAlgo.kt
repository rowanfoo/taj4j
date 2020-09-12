package com.dharma.algo.service.algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.ignitedata.service.RSiIgniteService
import com.dhamma.manager.RSIManager
import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.data.pojo.techstr
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class RSIAlgo : BaseAlgo(), IProcess {
    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var ignitecachestock: IgniteRepo<CoreStock>

    @Autowired
    lateinit var rSiIgniteService: RSiIgniteService

    @Autowired
    lateinit var rsimag: RSIManager

    override fun process(data: JsonObject): List<techstr> {
        var rsialgodata = data.get("rsialgo").asInt
        var usertop = data.get("sector").asString

        var cache = rsimag.today(data)

// for now not using filter of sector
        return cache
                .asSequence()
                .filter { it.value.first < rsialgodata && it.value.first != 0.0 }
                .map { setTechStr(it.key, "RSI", "RSI  ${it.value.first}----- : (${it.value.second}%)") }
                //            .onEach { setDateC(it) }
                .onEach { setNewsC(it) }
                .onEach { setFundC(it) }
                .onEach { setStockC(it) }
                .toList()
    }


}

