package com.dharma.algo.utility

import com.google.gson.Gson
import com.google.gson.JsonObject


object GJson {
    var gson = Gson()
    //var builder =  GsonBuilder().create()


    fun toGson(obj: Any): JsonObject {
//        JsonParser().
//        JsonObject().
//        return JsonObject()

        // return gson.fromJson(obj, JsonObject::class.java)
        return gson.toJsonTree(obj).asJsonObject

    }

//     fun toJson(obj:Map<Any,Any>):JsonNode{
//         return mapper.valueToTree(map)
//     }
//

}