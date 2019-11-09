package com.learn.ta4j.entity.data

import com.google.gson.JsonObject

data class DownPeriodAlgo(var data: JsonObject) {

    fun generateSql() {
        var period = data.get("key").asInt
        var d = data.get("fall").asDouble
        var sql = "where code=?  order by date  desc  LIMIT $period  "


    }


    fun predicate(): (Double) -> Boolean {
//return         {a -> a >  data.get("fall").asDouble  }
        return when (data.get("operator").asString) {
            (">") -> { a -> a > data.get("fall").asDouble }
            ("=") -> { a -> a > data.get("fall").asDouble }
            else -> { a -> a < data.get("fall").asDouble }
        }


    }
}