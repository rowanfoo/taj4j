package com.dharma.algo.entity.data

import com.google.gson.JsonObject

data class DownPeriodAlgo(var data: JsonObject) {

    fun generateSql(): String {
        var period = data.get("key_downPeriod").asInt
        var d = data.get("fall_downPeriod").asDouble
        var sql = "where code=?  order by date  desc  LIMIT $period  "
        return sql


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