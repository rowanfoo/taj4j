package com.learn.ta4j.entity.data

object User {

    var rsi: Int = 14
    var rsialgo: Int = 25
    var volumex: Double = 3.0
    var volumema: Int = 60

    var pricefall: Double = -0.04


    val fallpriod = 10
    var fallperiodval = -0.08

    //fall in 2 years > 35%
    var percentperiod = 240
    var percentperiodfall = 0.35


    init {
        println("Initializing object: $this")
    }

}