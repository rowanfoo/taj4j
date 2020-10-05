package com.dharma.algo.service.algo;


import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.MaIgniteService
import com.dharma.algo.utility.Maths
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
public class MetaData {
    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var maIgniteService: MaIgniteService


    public fun getMetaData(code: List<String>): ArrayNode {
        var mapper = ObjectMapper()
        val arrayNode = mapper.createArrayNode()
        var map = mutableMapOf<String, String>()

        code.map { it.toUpperCase() }.forEach {
            map.putAll(ma(it.toUpperCase()))
            map.putAll(yearprice(it.toUpperCase()))
            map.putAll(changePercent(it.toUpperCase()))
            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
        }
        return arrayNode
    }

    fun yearprice(code: String): Map<String, String> {
        var counter = 0
        var map = mutableMapOf<String, String>()
        var today = coreDataIgniteService.today(code)

        var todayPrice = today.close
        var dates = listOf<LocalDate>(LocalDate.now().minusYears(1), LocalDate.now().minusYears(2))
        dates.forEach {
            var series = coreDataIgniteService.dategt(code, it.toString())
            var oneYearPrice = series.first().close
            if (counter == 0) map.put("oneyear", Maths.percent(todayPrice, oneYearPrice).toString())
            else map.put("twoyear", Maths.percent(todayPrice, oneYearPrice).toString())
            counter++

        }
        return map
    }


    fun changePercent(code: String): Map<String, String> {
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
//
//        var params = JsonObject()
//        params.addProperty("ma", "50")
//        params.addProperty("mode", "price")
//        params.addProperty("code", code)
//        var maprice = maIgniteService.getCode(params)
//        map.put("fifty", Maths.percent(todayPrice, maprice).toString())
//
//        params.addProperty("ma", "100")
//        maprice = maIgniteService.getCode(params)
//        map.put("onehundred", Maths.percent(todayPrice, maprice).toString())
//
//        params.addProperty("ma", "200")
//        maprice = maIgniteService.getCode(params)
//        map.put("twohundred", Maths.percent(todayPrice, maprice).toString())
//
//        params.addProperty("ma", "20")
//        maprice = maIgniteService.getCode(params)
//        map.put("twenty", Maths.percent(todayPrice, maprice).toString())
        return map
    }
}
