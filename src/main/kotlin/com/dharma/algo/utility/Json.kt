package com.dharma.algo.utility

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

object Json {
    var mapper = ObjectMapper()

    fun toJson(obj: Any): JsonNode {
        return mapper.convertValue(obj, JsonNode::class.java)
    }

    //     fun toJson(obj:Map<Any,Any>):JsonNode{
//         return mapper.valueToTree(map)
//     }
//
    fun merge(a: ObjectNode, b: ObjectNode, exclude: List<String>): ObjectNode {
        b.remove(exclude)
        return a.setAll(b) as ObjectNode
    }
}
