package com.dharma.algo.Controller

import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.Algo.MetaData
import com.dharma.algo.service.WishlistService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class WishlistController {
    @Autowired
    lateinit var metadata: MetaData

    @Autowired
    lateinit var fundamentalService: FundamentalService

    @Autowired
    lateinit var wishlistService: WishlistService

    @GetMapping("/wishlist/metadata/{stocks}")
//    fun getMetaData(@PathVariable stocks: String): ArrayNode {
    fun getMetaData(@PathVariable stocks: String): Iterable<JsonNode> {

        var list = stocks.split(",")
        var funds = fundamentalService.codes(list)
        var z = metadata.getMetaData(list)


        var map = z.map {
            println("----*******------${it.get("code").asText()}---------")

            it.get("code").asText() to it
        }.toMap().toMutableMap()


        funds.forEach {
            println("----------${it.code}---------")
            var towork = map[it.code]
            (towork as ObjectNode).put("marketcap", it.marketcap)
            (towork as ObjectNode).put("pe", it.pe)
            (towork as ObjectNode).put("yield", it.annualYied)
            (towork as ObjectNode).put("yearchange", it.yearchange)
            map[it.code] = towork
        }




        println("---------------------RUN----------JSON--------$z----")
//        return z
        return map.values.asIterable()
    }

    @GetMapping("/wishlist/alldetails/{userid}")
    fun getalldetails(@PathVariable userid: String): ArrayNode {
        return wishlistService.wishlistsummary(userid)
    }
}