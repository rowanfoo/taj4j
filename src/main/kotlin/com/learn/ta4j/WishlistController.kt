package com.learn.ta4j

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
    //  @GetMapping("/wishlist/metadata")
//    fun getMetaData(@PathVariable stocks: String): List<JsonNode> {
    fun getMetaData(@PathVariable stocks: String): ArrayNode {
//    fun getMetaData(@PathVariable stocks: String): ObjectNode {
//    fun getMetaData(@PathVariable stocks: String): List<ObjectNode> {
        //fun getMetaData(): ArrayNode {
        println("---------------------RUN----------getMetaData------------")
//        println("---------------------RUN----------getMetaData--------$stocks----")
//
        var list = stocks.split(",")
        var z = metadata.getMetaData(list)
        println("---------------------RUN----------JSON--------$z----")
        return z

//        var l = mutableListOf<ObjectNode>()
//
//        var mapper = ObjectMapper()
//        val gamesNode = mapper.createArrayNode()
//
//        val game1 = mapper.createObjectNode().objectNode()
//        game1.put("name", "Fall Out 4")
//        game1.put("price", 49.9)
//
//        val game2 = mapper.createObjectNode().objectNode()
//        game2.put("name", "Dark Soul 3")
//        game2.put("price", 59.9)
//
//        gamesNode.add(game1)
//        gamesNode.add(game2)
//
//        l.add(game1)
//        l.add(game2)
//
//        return Test.getNodes()
//        return gamesNode
//        return l

    }


}