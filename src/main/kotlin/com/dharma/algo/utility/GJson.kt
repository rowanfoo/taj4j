package com.dharma.algo.utility

import com.google.gson.Gson
import com.google.gson.JsonObject


object GJson{
   var gson =  Gson()


    fun toGson(obj:Any): JsonObject {
        return gson.toJson(obj) as JsonObject
    }

//     fun toJson(obj:Map<Any,Any>):JsonNode{
//         return mapper.valueToTree(map)
//     }
//

}