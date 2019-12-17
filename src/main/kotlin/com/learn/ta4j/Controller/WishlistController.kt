package com.learn.ta4j.Controller

import com.fasterxml.jackson.databind.node.ArrayNode
import com.learn.ta4j.data.MetaData
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


    @GetMapping("/wishlist/metadata/{stocks}")
    fun getMetaData(@PathVariable stocks: String): ArrayNode {
        var list = stocks.split(",")
        var z = metadata.getMetaData(list)
        println("---------------------RUN----------JSON--------$z----")
        return z
    }


}