package com.dharma.algo.utility

import com.dhamma.pesistence.entity.data.CoreStock
import com.learn.ta4j.entity.pojo.Stock
import com.learn.ta4j.entity.pojo.techstr

object Maths {
    fun percent(currentprrice:Double, vsprrice:Double)= String.format("%.3f", (currentprrice - vsprrice)/vsprrice).toDouble()
}