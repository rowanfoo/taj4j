package com.dharma.algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.service.algodata.RSI
import com.google.gson.JsonObject
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class Rsitest {
    @Autowired
    lateinit var rsi: RSI
    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>

    @Test
    fun contextLoads() {
        var content = JsonObject()
        content.addProperty("code", "BSL.AX")
        //   content.addProperty("time", 14)
        // rsi.process(content)


    }

    @Test
    fun test1() {
        var series = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf("NHC.AX", "14"))
        println("--------load----SIZE-------------${series.size}")
    }
}