package com.dharma.algo.utility

import com.dhamma.pesistence.entity.data.CoreStock
import com.learn.ta4j.entity.pojo.Stock
import com.learn.ta4j.entity.pojo.techstr


object TechStrUtility {


    fun addStock(tech: techstr, stk: CoreStock): techstr {
        var sector = stk.top ?: ""
        if (filtersector(sector)(stk)) {
            tech.stock = Stock(stk.code, sector, stk.name)
        }
        return tech
    }

    fun filtersector(sector: String): (CoreStock) -> Boolean {
        println("------------filtersector 1  $sector--------")
        lateinit var tech: (CoreStock) -> Boolean
        println("------------filtersector 2--------")
        if (sector == "100") tech = { it -> it.top == "100" || it.top == "50" }
        if (sector == "200") tech = { it -> it.top == "100" || it.top == "50" || it.top == "200" }
        if (sector == "300") tech = { it -> it.top == "100" || it.top == "50" || it.top == "200" || it.top == "300" }
        println("------------filtersector 1  $tech--------")

        return tech


    }
}