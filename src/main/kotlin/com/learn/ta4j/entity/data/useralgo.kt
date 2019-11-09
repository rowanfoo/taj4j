package com.learn.ta4j.entity.data

import com.google.gson.Gson
import com.google.gson.JsonObject

data class UserAlgo(var String name , var String key){

    fun json():JsonObject{
        return    Gson().toJsonTree(this) as JsonObject
    }
}

