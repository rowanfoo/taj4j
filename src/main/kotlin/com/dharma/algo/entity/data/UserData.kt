package com.dharma.algo.entity.data

import com.google.gson.JsonObject

object UserData {

    var rsi: Int = 14
    var rsialgo: Int = 25
    var volumex: Double = 3.0
    var volumema: Int = 60

    var pricefall: Double = -0.04


    val fallpriod = 10
    var fallperiodval = -0.08

    //fall in 2 years > 35%
    var percentperiod = 240
    var percentperiodfall = 0.60

    lateinit var indicators: MutableMap<String, UserIndicator>


    fun downperiodalgo(): DownPeriodAlgo {
        var a = JsonObject()
        a.addProperty("key_downPeriod", UserData.percentperiod.toString())
        a.addProperty("fall_downPeriod", UserData.percentperiodfall.toString())
        return DownPeriodAlgo(a)
    }

    init {
        println("Initializing object: $this")
        indicators = mutableMapOf<String, UserIndicator>()

        indicators.put("rsi", UserIndicator("rsi", "14"))
        indicators.put("volumema", UserIndicator("volumema", "25"))
        //  indicators.put("rsi" ,  UserIndicator("rsi", "14") )


    }

}