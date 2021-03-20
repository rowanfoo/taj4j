package com.dharma.algo.service.algo;


import arrow.syntax.function.curried
import com.dhamma.ignitedata.manager.MAManager
import com.dhamma.ignitedata.service.*
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

    @Autowired
    lateinit var ta4JService: TA4JService

    @Autowired
    lateinit var covidService: CovidService

    public fun getMetaData(date: Optional<String>, code: List<String>): ArrayNode {
        var mapper = ObjectMapper()
        val arrayNode = mapper.createArrayNode()
        var mydate = if (date.isPresent) historyIndicatorService.dateExsits(date.get())
            .toString() else historyIndicatorService.today().toString()
        var maf = ::ma.curried()(mydate)
        var changePercentf = ::changePercent.curried()(mydate)

////        code.map { it.toUpperCase() }.forEach {
//        code.parallelStream().forEach {
//
//            var map = mutableMapOf<String, String>()
//            map.putAll(maf(it.toUpperCase()))
//            map.putAll(changePercentf(it.toUpperCase()))
//            store.add(map)
//            //! no IDEA why this got thread issue ... sometime code running above doesnt go into arrayNode
//            //            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
//        }
//        //put this out to solve THREAD issue
        //  THREAD ---- ISSUE
        //https://stackoverflow.com/questions/53847517/java-parallelstream-map-misses-records
        //missing record  ----- use map rather than foreach
        //            //! no IDEA why this got thread issue ... sometime code running above doesnt go into arrayNode
        //            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
        //
        var store = code.parallelStream().map {
//        var store = code.map {
//        code.map { it.toUpperCase() }.forEach {
            var map = mutableMapOf<String, String>()
            map.putAll(maf(it.toUpperCase()))
            map.putAll(changePercentf(it.toUpperCase()))
            map.putAll(thisweekprice(it.toUpperCase()))
            map.putAll(correctionfromHigh(it.toUpperCase()))
            map.putAll(countMaPercentage100(it.toUpperCase()))
            map.putAll(countMaPercentage200(it.toUpperCase()))
            map.putAll(covidService.getResult(it.toUpperCase()))
            map
        }.toList()

        //put this out to solve THREAD issue
        //! no IDEA why this got thread issue ... sometime code running above doesnt go into arrayNode
        //            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(map)))
        store.forEach() {
            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(it)))
        }


        //        store.forEach() {
//            arrayNode.add(ObjectMapper().readTree(ObjectMapper().writeValueAsString(it)))
//        }
        return arrayNode
    }

    //    fun yearprice(code: String): Map<String, String> {
//        var counter = 0
//        var map = mutableMapOf<String, String>()
//        var today = coreDataIgniteService.today(code)
////        var today =stockdate(date, code)
//
//        var todayPrice = today.close
//        var dates = listOf<LocalDate>(LocalDate.now().minusYears(1), LocalDate.now().minusYears(2))
//        dates.forEach {
//            var series = coreDataIgniteService.dategt(code, it.toString())
//            var oneYearPrice = series.first().close
//            if (counter == 0) map.put("oneyear", Maths.percent(todayPrice, oneYearPrice).toString())
//            else map.put("twoyear", Maths.percent(todayPrice, oneYearPrice).toString())
//            counter++
//
//        }
//        return map
//    }

    private fun stockdate(code: String, date: String) = coreDataIgniteService.dateeq(code, date)
    fun correctionfromHigh(code: String) = coreDataIgniteService.correctionfromHigh(code) as Map<String, String>

    fun thisweekprice(code: String) = coreDataIgniteService.pricethisweek(code) as Map<String, String>

    fun changePercent(date: String, code: String): Map<String, String> {
        println("------------------changePercent---------$code-----------$date---")
        var map = mutableMapOf<String, String>()
        var today = stockdate(code, date)
        println("------------------changePercent-List-------------$today---")

        var todaychange = today.changepercent
        map.put("change", todaychange.toString())
        return map
    }

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

    private fun countMaPercentage100(code: String) =
        mapOf("countMA100" to ta4JService.getCountAboveMA(code, 100, 160.0, 0.03))

    private fun countMaPercentage200(code: String) =
        mapOf("countMA200" to ta4JService.getCountAboveMA(code, 200, 160.0, 0.03))
}
