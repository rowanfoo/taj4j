package com.learn.ta4j

import com.dhamma.algodata.algodata.MA
import com.dhamma.algodata.algodata.RSI
import com.dhamma.pesistence.entity.data.User
import com.dhamma.pesistence.entity.repo.UserRepo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.google.gson.JsonObject
import com.learn.ta4j.Algo.FallPeriod
import com.learn.ta4j.Algo.Pricefall
import com.learn.ta4j.Algo.RSIAlgo
import com.learn.ta4j.Algo.VolumeX
import com.learn.ta4j.entity.data.UserData
import com.learn.ta4j.entity.pojo.techstr
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern


@CrossOrigin
@RestController
class WishlistController {
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

//    @GetMapping("/rsi")
//    fun rsi(@RequestParam sectorparam: Optional<String>): List<techstr> {
//        println("---------------------RUN------RSI----------------")
//        var a = JsonObject()
//        a.addProperty("rsi", UserData.rsi)
//        a.addProperty("rsialgo", UserData.rsialgo)
//
//
//        lateinit var sector: String;
//        if (!sectorparam.isPresent) a.addProperty("sector", "300")
//        else a.addProperty("sector", sectorparam.get())
//
//        var tech = rsialgo.process(a)
//
////        if (sector == "100") tech = tech.filter { it.stock?.sector == "100" || it.stock?.sector == "50" }
////        if (sector == "200") tech = tech.filter { it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
////        if (sector == "300") tech = tech.filter { it.stock?.sector == "300" || it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
////
//
////        return filter(tech, sector)
//        return tech
//
//    }


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
        content.addProperty("volumema", UserData.volumema)
        content.addProperty("volumex", UserData.volumex)


        lateinit var sector: String;
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        var tech = volume.process(content)


//        return filter(tech, sector)
        return tech

    }

// fall daily
//    @GetMapping("/price")
//    fun price(@RequestParam sectorparam: Optional<String>): List<techstr> {
//        println("---------------------RUN----------------------")
//        var content = JsonObject()
////        content.addProperty("date", "2019-08-05")
//        content.addProperty("price", UserData.pricefall)
//
//        lateinit var sector: String;
////        if (!sectorparam.isPresent) sector = "300"
//        if (!sectorparam.isPresent) content.addProperty("sector", "300")
//        else content.addProperty("sector", sectorparam.get())
//
//        var tech = pricefall.process(content)
////        return filter(tech, sector)
//        return tech
//
//
//    }

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

    @GetMapping("/rsi/{algo}")
    fun getbydate(@PathVariable algo: String, sectorparam: Optional<String>): List<techstr> {
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

        //  return "xxx "


    }


    @GetMapping("/falldaily/{algo}")
    fun price(@PathVariable algo: String, @RequestParam sectorparam: Optional<String>): List<techstr> {
        println("---------------------RUN----------------------")
        var (arg1, arg2) = getTwoElems(algo)
        var content = JsonObject()
//        content.addProperty("date", "2019-08-05")
        content.addProperty("price", arg1)

        lateinit var sector: String;
//        if (!sectorparam.isPresent) sector = "300"
        if (!sectorparam.isPresent) content.addProperty("sector", "300")
        else content.addProperty("sector", sectorparam.get())

        return pricefall.process(content)
//        return filter(tech, sector)


    }


    //    @GetMapping("/rsi")
//    fun rsi(@RequestParam sectorparam: Optional<String>): List<techstr> {
//        println("---------------------RUN------RSI----------------")
//        var a = JsonObject()
//        a.addProperty("rsi", UserData.rsi)
//        a.addProperty("rsialgo", UserData.rsialgo)
//
//
//        lateinit var sector: String;
//        if (!sectorparam.isPresent) a.addProperty("sector", "300")
//        else a.addProperty("sector", sectorparam.get())
//
//        var tech = rsialgo.process(a)
//
////        if (sector == "100") tech = tech.filter { it.stock?.sector == "100" || it.stock?.sector == "50" }
////        if (sector == "200") tech = tech.filter { it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
////        if (sector == "300") tech = tech.filter { it.stock?.sector == "300" || it.stock?.sector == "200" || it.stock?.sector == "100" || it.stock?.sector == "50" }
////
//
////        return filter(tech, sector)
//        return tech
//
//    }
    @Autowired
    lateinit var userrepo: UserRepo

    @CrossOrigin
    @PutMapping("/user")
    fun updateInvestigatem(@RequestBody node: JsonNode): ResponseEntity<JsonNode> {
        println("-----------------------CreateUser Algo-----------------$node")


        val usr = User.builder().algo(node.asText()).date(LocalDate.now()).build()
        userrepo.save(usr)
        return ResponseEntity(node, HttpStatus.OK)


    }

    @CrossOrigin
    @GetMapping("/user")
    public fun getAlgoAsJsonArray(): ArrayNode {
        println("----------------------GETUser Algo-----------------")
        println("-----------ITEMS---------")
        var a = userrepo.findAll()[0]
        println("-----------ITEMS-XX-----$a---")
        return a.algoAsJsonArrayJackson
    }

    private fun getThreeElems(zz: String): Triple<String, String, String> {
//        val zz = "30<14"
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