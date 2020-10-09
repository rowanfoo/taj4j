package com.dharma.algo.service

import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.data.HistoryIndicators
import com.dhamma.pesistence.service.FundamentalService
import com.dharma.algo.data.pojo.Stock
import com.dharma.algo.data.pojo.techstr


fun historyIndicatorstoTechstrs(list: List<HistoryIndicators>): List<techstr> {
    return list.map {
        techstr(it.code, it.date, it.type.toString(), it.message)
    }.toList()
}


fun bind(toRun: Boolean, fn: (techstr) -> techstr, data: techstr): techstr {
    return when (toRun) {
        true -> fn(data)
        false -> data
    }
}


fun addNews(newsIgniteService: NewsIgniteService, tech: techstr): techstr {
    tech.news = newsIgniteService.newsToday(tech.code)
    return tech
}

fun addFundamental(fundamentalService: FundamentalService, tech: techstr): techstr {
    tech.fundamental = fundamentalService.code(tech.code)
    return tech;
}

fun addStock(stockservice: Map<String, CoreStock>, tech: techstr): techstr {
    var stk = stockservice.get(tech.code)
    tech.stock = Stock(stk!!.code, stk.category ?: "", stk!!.name)
    return tech;
}



