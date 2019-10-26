package com.learn.ta4j

import com.dhamma.algodata.algodata.MA
import com.dhamma.algodata.algodata.RSI
import com.google.gson.JsonObject
import com.learn.ta4j.Algo.FallPeriod
import com.learn.ta4j.Algo.Pricefall
import com.learn.ta4j.Algo.RSIAlgo
import com.learn.ta4j.Algo.VolumeX
import com.learn.ta4j.entity.data.User
import com.learn.ta4j.entity.pojo.techstr
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@CrossOrigin
@RestController
class RunController {
    @Autowired
    lateinit var ma: MA
    @Autowired
    lateinit var ignite: Ignite
    @Autowired
    lateinit var volume: VolumeX

    @Autowired
    lateinit var pricefall: Pricefall


    @Autowired
    lateinit var fallPeriod: FallPeriod


    @Autowired
    lateinit var rsi: RSI


    @GetMapping("/")
    fun run() {
//        println("---------------------RUN----------------------")
//        //  ma.loadall()
//        //  println("---------------------RUN--------------DONE ALL---------------------")
//        var content = JsonObject()
//        content.addProperty("date", "2019-08-05")
//        content.addProperty("volume", 3)
//
//        rsi.loadall()

    }

    @Autowired
    lateinit var rsialgo: RSIAlgo

    @GetMapping("/rsi")
    fun rsi(@RequestParam sectorparam: Optional<String>): List<techstr> {
        println("---------------------RUN------RSI----------------")
        var a = JsonObject()
        a.addProperty("rsi", User.rsi)
        a.addProperty("rsialgo", User.rsialgo)


        lateinit var sector: String;
        if (!sectorparam.isPresent) a.addProperty("sector", "300")
        else a.addProperty("sector", sectorparam.get())

        var tech = rsialgo.process(a)

//        if (sector == "100") tech = tech.filter { it.stock?.sector == "100" || it.stock?.sector == "50" }
//        if (sector == "200") tech = tech.filter { it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
//        if (sector == "300") tech = tech.filter { it.stock?.sector == "300" || it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
//

//        return filter(tech, sector)
        return tech

    }


//    private fun filter(data: List<techstr>, sector: String): List<techstr> {
//
//        lateinit var tech: List<techstr>
//
//        if (sector == "100") tech = data.filter { it.stock?.sector == "100" || it.stock?.sector == "50" }
//        if (sector == "200") tech = data.filter { it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
//        if (sector == "300") tech = data.filter { it.stock?.sector == "300" || it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
//
//
//        return tech
//
//    }


    @GetMapping("/vol")
    fun vol(@RequestParam sectorparam: Optional<String>): List<techstr> {
        println("---------------------RUN----------------------")
        var content = JsonObject()
//        content.addProperty("date", "2019-08-05")
//        content.addProperty("volume", 3)
//
        content.addProperty("volumema", User.volumema)
        content.addProperty("volumex", User.volumex)


        lateinit var sector: String;
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        var tech = volume.process(content)


//        return filter(tech, sector)
        return tech

    }


    @GetMapping("/price")
    fun price(@RequestParam sectorparam: Optional<String>): List<techstr> {
        println("---------------------RUN----------------------")
        var content = JsonObject()
//        content.addProperty("date", "2019-08-05")
        content.addProperty("price", User.pricefall)

        lateinit var sector: String;
//        if (!sectorparam.isPresent) sector = "300"
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        var tech = pricefall.process(content)
//        return filter(tech, sector)
        return tech


    }

    @GetMapping("/fallshort")
    fun fallshort(): List<techstr> {
        println("---------------------RUN----------fall------------")

        var content = JsonObject()
        content.addProperty("time", "14")
        content.addProperty("percent", 0.05)
        content.addProperty("sector", 300)

        return fallPeriod.process(content)

    }


    @GetMapping("/falllong")
    fun falllong(): List<techstr> {
        // find all that has fallen 30% in 1.5 years
        println("---------------------RUN----------fall------------")

        var content = JsonObject()
        content.addProperty("time", "360")
        content.addProperty("percent", 0.3)
        content.addProperty("sector", 300)

        return fallPeriod.process(content)

    }


    @GetMapping("/abc")
    fun data() {
        println("---------------------RUN---abc-------------------")
        var cache0 = ignite.getOrCreateCache<String, Double>("MA50:price")
        var cache1 = ignite.getOrCreateCache<String, Double>("MA20:price")
        var cache2 = ignite.getOrCreateCache<String, Double>("MA200:price")
        var cache3 = ignite.getOrCreateCache<String, Double>("MA60:volume")

        println("--------TOTAL CACHE--------${cache0.size()}------${cache1.size()}----${cache2.size()}----${cache3.size()}----")


    }

}