package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.CoreStock
import com.dharma.algo.Algo.MetaData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class StockController {
    @Autowired
    lateinit var metadata: MetaData

    @Autowired
    lateinit var allStocks: Map<String, CoreStock>


    @GetMapping("/stocks")
    fun getStocks(): Set<String> {
//        var list = mutableListOf<String>()
        return allStocks.keys

    }


}