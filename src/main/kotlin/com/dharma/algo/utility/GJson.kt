package com.dharma.algo.utility

import com.google.gson.Gson
import com.google.gson.JsonObject


object GJson {
    var gson = Gson()

//    fun toGson(obj: Any): JsonObject {
//        return gson.toJsonTree(obj).asJsonObject
//
//    }


    fun toGson(obj: Any) = gson.toJsonTree(obj).asJsonObject


    fun merge(a: JsonObject, b: JsonObject, exclude: List<String>): JsonObject {
//       var map = a. toMap();
//        map.putAll(b.toMap());
//        JSONObject combined = new JSONObject(map);


//        val firstObject: HashMap<*, *> = Gson().fromJson(a, HashMap::class.java)
//        val secondObject: Map<Nothing, Nothing> = Gson().fromJson(b, HashMap::class.java)
//        firstObject.putAll(secondObject)
        a.entrySet().forEach {
            if (!exclude.contains(it.key)) {
                b.addProperty(it.key, it.value.asString)
            }

        }
        return a
    }

}
