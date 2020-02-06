package com.dharma.algo.service

import com.dharma.algo.data.pojo.techstr
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.regex.Pattern
@Component
class AlgoService {

    @Autowired
    lateinit var ignite: Ignite
    @Autowired
    lateinit var volume: com.dharma.algo.Algo.VolumeX
    @Autowired
    lateinit var pricefall: com.dharma.algo.Algo.Pricefall
    @Autowired
    lateinit var fallPeriod: com.dharma.algo.Algo.FallPeriod
    @Autowired
    lateinit var rsialgo: com.dharma.algo.Algo.RSIAlgo
    @Autowired
    lateinit var maalgo: com.dharma.algo.Algo.MAalog


    fun ma(ma: String, sectorparam: Optional<String>): List<techstr> {

        var (arg1, operator, arg2) = getThreeElems(ma)
        println("---------------MA-----$arg1--------$arg2-")

        var a = JsonObject()
        a.addProperty("ma", arg2)
        a.addProperty("percent", arg1)
        a.addProperty("mode", "price")
        lateinit var sector: String;
        if (!sectorparam.isPresent) a.addProperty("sector", "300")
        else a.addProperty("sector", sectorparam.get())

        return maalgo.process(a)
    }

    fun vol(algo: String, sectorparam: Optional<String>): List<techstr> {
        var (arg1, operator, arg2) = getThreeElems(algo)
        var content = JsonObject()
        content.addProperty("volumema", arg2)
        content.addProperty("volumex", arg1)

        println("---------------MA----vola-$arg2------volx--$arg1-")

        lateinit var sector: String;
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        var tech = volume.process(content)

        return tech
    }

    fun fallshort(): List<techstr> {
        var content = JsonObject()
        content.addProperty("time", "14")
        content.addProperty("percent", 0.05)
        content.addProperty("sector", 300)
        return fallPeriod.process(content)
    }

    fun falllong(): List<techstr> {
        var content = JsonObject()
        content.addProperty("time", "360")
        content.addProperty("percent", 0.3)
        content.addProperty("sector", 300)
        return fallPeriod.process(content)
    }

    fun data() {
        println("---------------------RUN---abc-------------------")
        var cache0 = ignite.getOrCreateCache<String, Double>("MA50:price")
        var cache1 = ignite.getOrCreateCache<String, Double>("MA20:price")
        var cache2 = ignite.getOrCreateCache<String, Double>("MA200:price")
        var cache3 = ignite.getOrCreateCache<String, Double>("MA60:volume")
        println("--------TOTAL CACHE--------${cache0.size()}------${cache1.size()}----${cache2.size()}----${cache3.size()}----")
    }

    fun rsi(algo: String, sectorparam: Optional<String>): List<techstr> {
        println("------------------------rsi--------$algo---------")
        var (arg1, operator, arg2) = getThreeElems(algo)
        println("------------------------rsi--------$arg1-----------$arg2-----")

        var a = JsonObject()
        a.addProperty("rsi", arg2)
        a.addProperty("rsialgo", arg1)

        lateinit var sector: String;
        if (!sectorparam.isPresent) a.addProperty("sector", "300")
        else a.addProperty("sector", sectorparam.get())

        var tech = rsialgo.process(a)
        return tech
    }

    fun price(algo: String, sectorparam: Optional<String>): List<techstr> {
        println("---------------------RUN----------------------")
        var (arg1, arg2) = getTwoElems(algo)
        var content = JsonObject()
        content.addProperty("price", arg1)

        lateinit var sector: String;
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        return pricefall.process(content)
    }

    private fun getThreeElems(zz: String): Triple<String, String, String> {
        val r = Pattern.compile("<|>|=")
        val m = r.matcher(zz)
        m.find()
        println("Start index: " + m.start())
        println("----------------" + zz.substring(0, m.start()))
        println("----------------" + zz.substring(m.start(), m.end()))
        println("----------------" + zz.substring(m.end(), zz.length))
        println(" End index: " + m.end())
        println(" Found: " + m.group())
        return Triple(zz.substring(0, m.start()), zz.substring(m.start(), m.end()), zz.substring(m.end(), zz.length))
    }

    private fun getTwoElems(zz: String): Pair<String, String> {
        val r = Pattern.compile("<|>|=")
        val m = r.matcher(zz)
        m.find()
        println("Start index: " + m.start())
        println("----------------" + zz.substring(0, m.start()))
        println("----------------" + zz.substring(m.start(), m.end()))
        println(" End index: " + m.end())
        println(" Found: " + m.group())
        return Pair(zz.substring(0, m.start()), zz.substring(m.start(), m.end()))
    }

}