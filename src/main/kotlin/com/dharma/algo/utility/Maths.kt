package com.dharma.algo.utility

object Maths {
    fun percent(currentprrice: Double, vsprrice: Double) = String.format("%.3f", (currentprrice - vsprrice) / vsprrice).toDouble()

    fun percentformat(d: Double) = String.format("%.3f", d)
}
