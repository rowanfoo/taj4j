package com.dharma.algo.Algo;


import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.MaIgniteService
import com.dharma.algo.utility.Maths
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
public class MetaData {
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>


    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService


    @Autowired
    lateinit var maIgniteService: MaIgniteService


    //    public fun getMetaData(code: List<String>): JsonObject {
    public fun getMetaData(code: List<String>): ArrayNode {
//        var data = JsonObject()
//        data.addProperty("rsi", User.rsi)
//        data.addProperty("rsialgo", User.rsialgo)
        //public fun getMetaData(code: List<String>): ArrayNode {

        var mapper = ObjectMapper()
        val arrayNode = mapper.createArrayNode()

//        var list = mutableListOf<JsonNode>()
        var map = mutableMapOf<String, String>()
        //val gamesNode = ObjectMapper().createArrayNode()

        code.map { it.toUpperCase() }.forEach {

            println("------code-------$it")

            map.putAll(ma(it.toUpperCase()))
            map.putAll(yearprice(it.toUpperCase()))
            map.putAll(changePercent(it.toUpperCase()))
            //  gamesNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))

//            list.add( ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))

//            ObjectMapper().writeValueAsString(ma(it) )

            //          list.add(ObjectMapper().readTree(ma(it) ))
//            println(ma(it))

        }
        //      val json = ObjectMapper().writeValueAsString(map)
        //    println(json)

        return arrayNode
    }

    fun yearprice(code: String): Map<String, String> {
        var counter = 0
        var map = mutableMapOf<String, String>()

//        var today = ignitecache.values(" where code=?   order by date desc limit ?  ", arrayOf(code, "1"))


        var today = coreDataIgniteService.today(code)

//        var todayPrice = today[0].close
        var todayPrice = today.close
        var dates = listOf<LocalDate>(LocalDate.now().minusYears(1), LocalDate.now().minusYears(2))
        dates.forEach {
            //            var series = ignitecache.values(" where code=?  and date > ? order by date asc  ", arrayOf(code, it.toString()))
            println("------------$code----------------${it.toString()}----")
            var series = coreDataIgniteService.dategt(code, it.toString())
            println("------------$code----------------${series.size}----")
//            var obj = series[0]
            var oneYearPrice = series.first().close
            if (counter == 0) map.put("oneyear", Maths.percent(todayPrice, oneYearPrice).toString())
            else map.put("twoyear", Maths.percent(todayPrice, oneYearPrice).toString())
            counter++

        }
        println("------------$$$--map------------$map")
        return map
    }


    fun changePercent(code: String): Map<String, String> {
       // var mapper = ObjectMapper()

        var map = mutableMapOf<String, String>()
        var today = coreDataIgniteService.today(code)
        var todaychange = today.changepercent
        map.put("change", todaychange.toString())
        return map

    }

    fun ma(code: String): Map<String, String> {
        var map = mutableMapOf<String, String>()
        map.put("code", code)
        var today = coreDataIgniteService.today(code)
        var todayPrice = today.close

        var params = JsonObject()
        params.addProperty("ma", "50")
        params.addProperty("mode", "price")
        params.addProperty("code", code)
        var maprice = maIgniteService.getCode(params)
        map.put("fifty", Maths.percent(todayPrice, maprice).toString())

        params.addProperty("ma", "100")
        maprice = maIgniteService.getCode(params)
        map.put("onehundred", Maths.percent(todayPrice, maprice).toString())

        params.addProperty("ma", "200")
        maprice = maIgniteService.getCode(params)
        map.put("twohundred", Maths.percent(todayPrice, maprice).toString())

        params.addProperty("ma", "20")
        maprice = maIgniteService.getCode(params)
        map.put("twenty", Maths.percent(todayPrice, maprice).toString())
        return map

    }


}