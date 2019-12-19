package com.dharma.algo.entity.data

import com.google.gson.Gson
import com.google.gson.JsonObject

//data class UserIndicator(var String name, var String key, var data: JsonObject) {
data class UserIndicator(var name: String, var key: String) {

    fun json(): JsonObject {
        return Gson().toJsonTree(this) as JsonObject
    }
}

