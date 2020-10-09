package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.QWishlist
import com.dhamma.pesistence.entity.data.Wishlist
import com.dhamma.pesistence.entity.repo.WishlistRepo
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.service.WishlistService
import com.dharma.algo.service.algo.MetaData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@CrossOrigin
@RestController
class WishlistController {
    @Autowired
    lateinit var metadata: MetaData

    @Autowired
    lateinit var fundamentalService: FundamentalService

    @Autowired
    lateinit var wishlistService: WishlistService

    @Autowired
    lateinit var wishlistRepo: WishlistRepo


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
            (towork as ObjectNode).put("yearchange", it.yearchange ?: 0.00)
            map[it.code] = towork
        }
        println("---------------------RUN----------JSON--------$z----")
//        return z
        return map.values.asIterable()
    }
//    ---------getWishlisSummary-------http://localhost:8080/wishlist/alldetails/rowan

//    @GetMapping("/wishlist/alldetails/{userid}")
//    fun getalldetails(@PathVariable userid: String): ArrayNode {
//        return wishlistService.wishlistsummary(userid)
//    }

    @GetMapping("/wishlist/alldetails/{userid}")
    fun getalldetails(@PathVariable userid: String, @RequestParam date: Optional<String>): ArrayNode {
        return wishlistService.wishlistsummary(userid, date)
    }

    @GetMapping("/wishlist/wishlistcategorys/{userid}")
    fun wishlistcategorys(@PathVariable userid: String): List<Wishlist> {
        return wishlistRepo.findAll(QWishlist.wishlist.userid.eq(userid)).toList()
    }


}

