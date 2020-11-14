package com.dharma.algo.service

import arrow.syntax.function.curried
import com.dhamma.ignitedata.manager.MAManager
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.HistoryIndicatorService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.data.QCoreStock
import com.dhamma.pesistence.entity.repo.StockRepo
import com.dhamma.pesistence.service.FundamentalService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class CategoryService {
    @Autowired
    lateinit var stockrepo: StockRepo


    @Autowired
    lateinit var allStocks: Map<String, CoreStock>

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var ma: MAManager


    @Autowired
    lateinit var fundamentalservice: FundamentalService


    lateinit var addPrice: (String) -> JsonNode
    lateinit var addStockR: (String) -> JsonNode
    lateinit var addfundamentalR: (String) -> JsonNode


    @Autowired
    lateinit var historyIndicatorService: HistoryIndicatorService

    @PostConstruct
    fun init() {
        addStockR = ::stocktojson.curried()(allStocks)
    }

    fun category(name: String): List<JsonNode> {
        var z = stockrepo.findAll(QCoreStock.coreStock.category.like("%$name%")).toList()
        println("----------category---${z.size}-----------")
        return addAdditionInfo(z)
    }

    fun subcategory(name: String): List<JsonNode> {
        var z = stockrepo.findAll(QCoreStock.coreStock.subcategory.like("%$name%")).toList()
        println("----------subcategory---${z.size}-----------")
        return addAdditionInfo(z)
    }

    fun tag(name: String): List<JsonNode> {
        var z = stockrepo.findAll(QCoreStock.coreStock.tags.like("%$name%")).toList()
        println("----------tag---${z.size}-----------")
        return addAdditionInfo(z)
    }

    fun addAdditionInfo(ls: List<CoreStock>): List<JsonNode> {
        addPrice = ::pricedatajson.curried()(coreDataIgniteService)(historyIndicatorService.today().toString())
        var perioddatajson = ::perioddatajson.curried()(coreDataIgniteService)

        addfundamentalR = ::fundamentaljson.curried()(fundamentalservice)

        var MA50 = JsonObject()
        MA50.addProperty("mode", "price")
        MA50.addProperty("time", "50")

        var majson = ::madatajson.curried()(ma)(MA50)


//        fun madatajson(ma: MAManager, obj: JsonObject, stocks: List<String>): List<ObjectNode> {
        println("-----------start------------")

        var t =
                ls.map {
                    println("--------start stock------------${it.code}")
                    it.code
                }
                        .map(addStockR)
                        .map {
                            var z = addPrice(it["code"].asText())
                            //        println("----------xxxx1---${z}-----------")
                            (it as ObjectNode).setAll(z as ObjectNode)
//                            println("----------yyyyy2---${it}-----------")
                            it
                        }
                        .map {
                            var z = perioddatajson(it["code"].asText())
                            //   println("----------xxxx2---${z}-----------")
                            (it as ObjectNode).setAll(z as ObjectNode)
//                            println("----------yyyyy4---${it}-----------")
                            it
                        }
                        .map {

                            var z = majson(listOf(it["code"].asText()))
                            //  println("---------zzzzz3---${z}-----------")
                            (it as ObjectNode).setAll(z[0] as ObjectNode)
//                            println("----------yyyyy5---${it}-----------")
                            it
                        }

                        .map {
                            //  println("----------zzzzz4---${it}-----------")
                            var z = addfundamentalR(it["code"].asText())
                            (it as ObjectNode).setAll(z as ObjectNode)
                            //    println("----------yyyyy6---${it}-----------")
                            it
                        }
                        .toList()
        return t
    }
}



