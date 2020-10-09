package com.dharma.algo.service

import arrow.syntax.function.pipe
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.service.algo.*
import com.dharma.algo.utility.GJson
import com.dharma.algo.utility.StringUtility.threeElems
import com.dharma.algo.utility.StringUtility.twoElems
import com.google.gson.JsonObject
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/*
used by controller , organize some simple logic and also prepare data ,  get params

 */
@Component
class AlgoService {

    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var volume: VolumeX

    @Autowired
    lateinit var pricefall: Pricefall

    @Autowired
    lateinit var fallPeriod: FallPeriod

    @Autowired
    lateinit var rsialgo: RSIAlgo

    @Autowired
    lateinit var maalgo: MAalog

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    @Autowired
    lateinit var fundamentalService: FundamentalService

    fun ma(ma: String, sectorparam: Optional<String>): List<techstr> {

        var (arg1, operator, arg2) = threeElems(ma)
        println("---------------MA-----$arg1--------$arg2-")

        var a = GJson.toGson(mapOf("ma" to arg2, "percent" to arg1, "mode" to "price"))

        lateinit var sector: String;
        if (!sectorparam.isPresent) a.addProperty("sector", "300")
        else a.addProperty("sector", sectorparam.get())
        //return addNews(maalgo.process(a))
//        return maalgo.process(a) pipe ::addNews pipe ::addFundamental


        return maalgo.process(a, true, true, true, true)

    }

    fun vol(algo: String, sectorparam: Optional<String>): List<techstr> {
        var (arg1, operator, arg2) = threeElems(algo)
        var content = GJson.toGson(mapOf("time" to arg2, "volumex" to arg1))
        println("---------------VOL Argument----$arg2------volx--$arg1-")

        lateinit var sector: String;
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())
        return volume.process(content, true, true, true, true)

    }

    fun fallshort(): List<techstr> {
//        var content = JsonObject()
//        content.addProperty("time", "14")
//        content.addProperty("percent", 0.05)
//        content.addProperty("sector", 300)
        var content = GJson.toGson(mapOf("sector" to 300, "percent" to 0.05, "time" to "14"))
        // return fallPeriod.process(content)
        //  return addNews(fallPeriod.process(content))
        return fallPeriod.process(content) pipe ::addNews pipe ::addFundamental
        //return fallPeriod.process(content, false, true, false)

    }

    fun falllong(): List<techstr> {
        var content = GJson.toGson(mapOf("sector" to 300, "percent" to 0.35, "time" to "360"))
        // return fallPeriod.process(content)
        //return addNews(fallPeriod.process(content))
        return fallPeriod.process(content) pipe ::addNews pipe ::addFundamental
    }


    fun rsi(algo: String): List<techstr> {
        println("------------------------rsi--------$algo---------")
        //  var (arg1, operator, arg2) = threeElems(algo)
        //println("------------------------rsi--------$arg1-----------$arg2-----")

        //var a = GJson.toGson(mapOf("time" to arg2, "rsialgo" to arg1))

        lateinit var sector: String;


        // return rsialgo.process(a, true, true, true, true)
        return listOf()
    }

    fun price(algo: String, sectorparam: Optional<String>): List<techstr> {
        println("---------------------RUN----------------------")
        var (arg1, arg2) = twoElems(algo)
//        var content = JsonObject()
//        content.addProperty("price", arg1)
        var content = GJson.toGson(mapOf("price" to arg1))

        lateinit var sector: String;
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        //  return pricefall.process(content)
        //  return addNews(pricefall.process(content))  pipe  addFundamental(data:List<techstr>)
//        return pricefall.process(content) pipe ::addNews pipe ::addFundamental


        return pricefall.process(content, true, true, true, true)


    }


    private fun populateParams(algo: String, sectorparam: Optional<String>): JsonObject {
        var (arg1, operator, arg2) = threeElems(algo)

        var a = GJson.toGson(mapOf("rsi" to arg2, "rsialgo" to arg1))
        lateinit var sector: String;

        if (!sectorparam.isPresent) a.addProperty("sector", "300")
        else a.addProperty("sector", sectorparam.get())
        return a;
    }


    private fun addNews(data: List<techstr>): List<techstr> {
        return data.map {
            it.news = newsIgniteService.newsToday(it.code)
            it
        }.toList()
    }

    private fun addFundamental(data: List<techstr>): List<techstr> {
        return data.map {
            it.fundamental = fundamentalService.code(it.code)
            it
        }.toList()
    }


    fun data() {
        println("---------------------RUN---abc-------------------")
        var cache0 = ignite.getOrCreateCache<String, Double>("MA50:price")
        var cache1 = ignite.getOrCreateCache<String, Double>("MA20:price")
        var cache2 = ignite.getOrCreateCache<String, Double>("MA200:price")
        var cache3 = ignite.getOrCreateCache<String, Double>("MA60:volume")
        println("--------TOTAL CACHE--------${cache0.size()}------${cache1.size()}----${cache2.size()}----${cache3.size()}----")
    }


//    private fun getThreeElems(zz: String): Triple<String, String, String> {
//        val r = Pattern.compile("<|>|=")
//        val m = r.matcher(zz)
//        m.find()
//        println("Start index: " + m.start())
//        println("----------------" + zz.substring(0, m.start()))
//        println("----------------" + zz.substring(m.start(), m.end()))
//        println("----------------" + zz.substring(m.end(), zz.length))
//        println(" End index: " + m.end())
//        println(" Found: " + m.group())
//        return Triple(zz.substring(0, m.start()), zz.substring(m.start(), m.end()), zz.substring(m.end(), zz.length))
//    }
//
//    private fun getTwoElems(zz: String): Pair<String, String> {
//        val r = Pattern.compile("<|>|=")
//        val m = r.matcher(zz)
//        m.find()
//        println("Start index: " + m.start())
//        println("----------------" + zz.substring(0, m.start()))
//        println("----------------" + zz.substring(m.start(), m.end()))
//        println(" End index: " + m.end())
//        println(" Found: " + m.group())
//        return Pair(zz.substring(0, m.start()), zz.substring(m.start(), m.end()))
//    }

}
