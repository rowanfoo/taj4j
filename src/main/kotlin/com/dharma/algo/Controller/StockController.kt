package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.repo.StockRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
class StockController {
    @Autowired
    lateinit var allStocks: Map<String, CoreStock>

    @Autowired
    lateinit var stockRepo: StockRepo

    @GetMapping("/stocks")
    fun getStocks(): Set<String> {
        return allStocks.keys
    }

    @GetMapping("/stocks/stock/{code}")
    fun stock(@PathVariable code: String) = allStocks[code]

    @GetMapping("/corestocks")
    fun getCoreStocks(): List<CoreStock> {
        return allStocks.entries.map { it.value }.toList()
    }

    //!!!!  Need to refresh BEAN allStocks
    @PutMapping("/stocks")
    fun set(@RequestBody stock: CoreStock) {
        var stk = allStocks[stock.code]!!
        stk.category = stock.category
        stk.subcategory = stock.subcategory
        stockRepo.save(stk)
    }


}
