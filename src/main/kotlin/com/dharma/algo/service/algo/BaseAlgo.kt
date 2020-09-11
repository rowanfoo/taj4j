package com.dharma.algo.service.algo

import arrow.syntax.function.curried
import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.TechStrBuilderUtility
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

open abstract class BaseAlgo : IProcess {

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var techStrBuilderUtility: TechStrBuilderUtility

    @Autowired
    lateinit var fundamentalService: FundamentalService

    @Autowired
    lateinit var allStocks: Map<String, CoreStock>


    lateinit var addStockR: (techstr) -> techstr
    lateinit var setDateR: (techstr) -> techstr
    lateinit var addNewsR: (techstr) -> techstr
    lateinit var addFundamentalR: (techstr) -> techstr

    lateinit var setNewsC: (techstr) -> techstr
    lateinit var setFundC: (techstr) -> techstr
    lateinit var setStockC: (techstr) -> techstr

    @PostConstruct
    fun init() {
        setDateR = ::setDate.curried()(coreDataIgniteService)
        addNewsR = ::addNews.curried()(newsIgniteService)
        addFundamentalR = ::addFundamental.curried()(fundamentalService)
        addStockR = ::addStock.curried()(allStocks)
    }

    fun setFunction(addDate: Boolean, addNews: Boolean, addFund: Boolean, addStock: Boolean) {
        setNewsC = ::bind.curried()(addNews)(addNewsR)
        setFundC = ::bind.curried()(addFund)(addFundamentalR)
        setStockC = ::bind.curried()(addStock)(addStockR)
    }

    override fun process(data: JsonObject, addDate: Boolean, addNews: Boolean, addFund: Boolean, addStock: Boolean): List<techstr> {
        setFunction(addDate, addNews, addFund, addStock)
        return process(data)
    }


    abstract fun process(data: JsonObject): List<techstr>
}





