package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.RandomChart
import com.dhamma.pesistence.entity.repo.RandomChartRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class RandomChartController {
    @Autowired
    lateinit var randomChartRepo: RandomChartRepo


//    @GetMapping("/stocks")
//    fun getStocks(): Set<String> {
////        var list = mutableListOf<String>()
//        return allStocks.keys
//
//    }


    @PutMapping("/randomChart")
    fun set(@RequestBody randomChart: RandomChart) {
        println("------------------$randomChart-----------------")
//        randomChartRepo.save(randomChart)
    }


}
