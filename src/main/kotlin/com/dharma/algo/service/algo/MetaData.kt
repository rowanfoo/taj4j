package com.dharma.algo.service.algo;


import arrow.syntax.function.curried
import com.dhamma.ignitedata.manager.MAManager
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.HistoryIndicatorService
import com.dhamma.ignitedata.service.MaIgniteService
import com.dharma.algo.ConvertUtily
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import kotlin.streams.toList


@Component
public class MetaData {
    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var maIgniteService: MaIgniteService

    @Autowired
    lateinit var maManager: MAManager

    @Autowired
    lateinit var historyIndicatorService: HistoryIndicatorService


    public fun getMetaData(date: Optional<String>, code: List<String>): ArrayNode {
        var mapper = ObjectMapper()
        val arrayNode = mapper.createArrayNode()
        var mydate = if (date.isPresent) historyIndicatorService.dateExsits(date.get())
            .toString() else historyIndicatorService.today().toString()
        var maf = ::ma.curried()(mydate)
        var changePercentf = ::changePercent.curried()(mydate)

        //  THREAD ---- ISSUE
        //https://stackoverflow.com/questions/53847517/java-parallelstream-map-misses-records
        //missing record  ----- use map rather than foreach
        //            //! no IDEA why this got thread issue ... sometime code running above doesnt go into arrayNode
        //            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
        //
        var store = code.parallelStream().map {

            var map = mutableMapOf<String, String>()
            map.putAll(maf(it.toUpperCase()))
            map.putAll(changePercentf(it.toUpperCase()))
            map.putAll(thisweekprice(it.toUpperCase()))
            map.putAll(correctionfromHigh(it.toUpperCase()))
            map
            //! no IDEA why this got thread issue ... sometime code running above doesnt go into arrayNode
            //            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
        }.toList()

        //put this out to solve THREAD issue
        store.forEach() {
            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(it)))
        }
        return arrayNode
    }

    private fun stockdate(code: String, date: String) = coreDataIgniteService.dateeq(code, date)

    fun changePercent(date: String, code: String): Map<String, String> {
        var map = mutableMapOf<String, String>()
        var today = stockdate(code, date)
        var todaychange = today.changepercent
        map.put("change", todaychange.toString())
        return map
    }

    fun correctionfromHigh(code: String): Map<String, String> {
        return coreDataIgniteService.correctionfromHigh(code) as Map<String, String>
    }

    fun thisweekprice(code: String) = coreDataIgniteService.pricethisweek(code) as Map<String, String>

    fun ma(date: String, code: String): Map<String, String> {

        var params = JsonObject()
        params.addProperty("ma", "50")
        params.addProperty("mode", "price")
        params.addProperty("code", code)

        var data = maManager.code(code, date, 20, "price")
        val yourHashMap = toMAMap(data, 20)
        //   val yourHashMap = Gson().fromJson(data.toString(), HashMap::class.java) as HashMap<String, String>
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
        //return map
        data = maManager.code(code, date, 50, "price")
        //  yourHashMap.putAll(Gson().fromJson(data.toString(), HashMap::class.java) as Map<String, String>)
        yourHashMap.putAll(toMAMap(data, 50))


        data = maManager.code(code, date, 100, "price")
        //  yourHashMap.putAll(Gson().fromJson(data.toString(), HashMap::class.java) as Map<String, String>)
        yourHashMap.putAll(toMAMap(data, 100))

        data = maManager.code(code, date, 200, "price")
        //  yourHashMap.putAll(Gson().fromJson(data.toString(), HashMap::class.java) as Map<String, String>)
        yourHashMap.putAll(toMAMap(data, 200))
        return yourHashMap
    }

    private fun toMAMap(data: JsonObject, time: Int): MutableMap<String, String> {
        var map = mutableMapOf<String, String>()
        map.put("code", data["code"].asString)
        map.put("percentage${time}", ConvertUtily.round((data["percentage"].asDouble * 100)).toString())
        map.put("maprice${time}", data["maprice"].asString)
        return map
    }
}
