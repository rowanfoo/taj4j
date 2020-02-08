package com.dharma.algo.utility

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

 object Json{
     var mapper = ObjectMapper()

     fun toJson(obj:Any):JsonNode{
         return mapper.convertValue(obj, JsonNode::class.java)
     }

//     fun toJson(obj:Map<Any,Any>):JsonNode{
//         return mapper.valueToTree(map)
//     }
//

}