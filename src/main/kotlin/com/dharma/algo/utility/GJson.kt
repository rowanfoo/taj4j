package com.dharma.algo.utility

import com.google.gson.Gson


object GJson {
    var gson = Gson()

//    fun toGson(obj: Any): JsonObject {
//        return gson.toJsonTree(obj).asJsonObject
//
//    }


    fun toGson(obj: Any) = gson.toJsonTree(obj).asJsonObject


}
