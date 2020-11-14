package com.dharma.algo.service

import com.dhamma.ignitedata.manager.MAManager
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.data.HistoryIndicators
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.Json
import com.dharma.algo.utility.Maths
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.JsonObject


fun historyIndicatorstoTechstrs(list: List<HistoryIndicators>): List<techstr> {
    return list.map {
        techstr(it.code, it.date, it.type.toString(), it.message)
    }.toList()
}


fun bind(toRun: Boolean, fn: (techstr) -> techstr, data: techstr): techstr {
    return when (toRun) {
        true -> fn(data)
        false -> data
    }
}


fun addNews(newsIgniteService: NewsIgniteService, tech: techstr): techstr {
    tech.news = newsIgniteService.newsToday(tech.code)
    return tech
}

fun addFundamental(fundamentalService: FundamentalService, tech: techstr): techstr {
    tech.fundamental = fundamentalService.code(tech.code)
    return tech;
}

fun addStock(stockservice: Map<String, CoreStock>, tech: techstr): techstr {
    var stk = stockservice.get(tech.code)
    tech.stock = Stock(stk!!.code, stk.category ?: "", stk!!.name)
    return tech;
}


fun stockinfo(stockservice: Map<String, CoreStock>, code: String): JsonNode {
    val mapper = ObjectMapper()
    var stk = stockservice.get(code)
    return if (stk == null) mapper.createObjectNode() else Json.toJson(stk!!);
}

var mapper = ObjectMapper()

fun stocktojson(stockservice: Map<String, CoreStock>, code: String): JsonNode {
    var stk = stockservice.get(code)

    var rootNode = mapper.createObjectNode()
    (rootNode as ObjectNode).put("code", stk?.code)
    (rootNode as ObjectNode).put("name", stk?.name)
    (rootNode as ObjectNode).put("shares", stk?.shares)
    (rootNode as ObjectNode).put("marketcap", stk?.marketcap)
    (rootNode as ObjectNode).put("category", stk?.category)
    (rootNode as ObjectNode).put("subcategory", stk?.subcategory)
    (rootNode as ObjectNode).put("tags", stk?.tags)
    return rootNode
}

fun pricedatajson(coreDataIgniteService: CoreDataIgniteService, date: String, code: String): ObjectNode {
    var rootNode = mapper.createObjectNode()
    (rootNode as ObjectNode).put("code", code)
    //println("====================pricedatajson==================$code===++++++$date==")
    var data = coreDataIgniteService.dateeq(code, date)
    println("----pricedatajson data------$data")

    (rootNode as ObjectNode).put("price", data.close)
    (rootNode as ObjectNode).put("change", data.changepercent)
    (rootNode as ObjectNode).put("volume", data.volume)
    (rootNode as ObjectNode).put("date", data.date.toString())
    return rootNode
}

fun perioddatajson(coreDataIgniteService: CoreDataIgniteService, code: String): ObjectNode {

    var rootNode = mapper.createObjectNode()
    (rootNode as ObjectNode).put("code", code)
    (rootNode as ObjectNode).put("weeky", Maths.percentformat(coreDataIgniteService.priceperiodprecent(code, "week") * 100))
    (rootNode as ObjectNode).put("month", Maths.percentformat(coreDataIgniteService.priceperiodprecent(code, "month") * 100))
    (rootNode as ObjectNode).put("month3", Maths.percentformat(coreDataIgniteService.priceperiodprecent(code, "3month") * 100))
    return rootNode
}


fun madatajson(ma: MAManager, obj: JsonObject, stocks: List<String>): List<ObjectNode> {

    var rootNode = mapper.createObjectNode()
    var time = obj.get("time").asString

    var z = ma.ma(obj, stocks)

    var t = z.map {
        (rootNode as ObjectNode).put("maprice$time", it["maprice"].asString)
        (rootNode as ObjectNode).put("mapercentage$time", Maths.percentformat(it["percentage"].asDouble * 100))
    }.toList() as List<ObjectNode>
    return t
}


fun fundamentaljson(fundamentalService: FundamentalService, code: String): ObjectNode {
    var fundamental = fundamentalService.code(code)
    println("----------fundamentaljson------xxxx-----$fundamental----")
    var rootNode = mapper.createObjectNode()

    (rootNode as ObjectNode).put("code", code)
    (rootNode as ObjectNode).put("annualyield", fundamental.annualYied)
    (rootNode as ObjectNode).put("eps", fundamental.eps)
    (rootNode as ObjectNode).put("marketcap", fundamental.marketcap)
    (rootNode as ObjectNode).put("pe", fundamental.pe)
    (rootNode as ObjectNode).put("shares", fundamental.shares)
    (rootNode as ObjectNode).put("yearhighprice", fundamental.yearHighPrice)
    (rootNode as ObjectNode).put("yearlowprice", fundamental.yearLowPrice)
    (rootNode as ObjectNode).put("yearchange", fundamental.yearchange ?: 0.0)
    return rootNode
}





