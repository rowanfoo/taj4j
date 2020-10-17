package com.dharma.algo.service


import arrow.syntax.function.curried
import com.dhamma.ignitedata.service.HistoryIndicatorService
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.data.News
import com.dhamma.pesistence.entity.repo.NewsRepo
import com.dharma.algo.utility.Json
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class NewsService {
    @Autowired
    lateinit var allStocks: Map<String, CoreStock>


//    @Autowired
//    lateinit var newsIgniteService: NewsIgniteService

//    @Autowired
//    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var historyIndicatorService: HistoryIndicatorService

    @Autowired
    lateinit var newsRepo: NewsRepo

//    fun news(date: Optional<String>, addstockinfo: Boolean, page: String, size: String): List<JsonObject> {
//
//        var addstockinfofn = ::stockinfo.curried()(allStocks)
//
//        val pageRequest = PageRequest.of(page.toInt(), size.toInt())
//        var mydate = if (date.isPresent) LocalDate.parse(date.get()) else historyIndicatorService.today()
//
//        val pageResult: Page<News> = newsRepo.findAll(QNews.news.date.eq(mydate), pageRequest)
//
//        var allnews = pageResult.content
//        return allnews.map {
//            var new = GJson.toGson(it)
//
//            if (addstockinfo) {
//                var stks = addstockinfofn(it.code)
//                GJson.merge(stks, new, listOf("date"))
//
//            }
//            new
//        }.toList()
//
//
//    }

    fun addStockInfo(allnews: List<News>, addstockinfo: Boolean, page: String, size: String): List<ObjectNode> {
        var addstockinfofn = ::stockinfo.curried()(allStocks)

        return allnews.map {
            var new = Json.toJson(it) as ObjectNode
            // println("--------mydate-----$new---")
            new.remove("date")

            new.put("date", it.date.toString())

            //println("--------NEW mydate-----$new---")

            if (addstockinfo) {
                var stks = addstockinfofn(it.code)
                new = Json.merge(new, stks as ObjectNode, listOf("date"))
            }
            new
        }.toList()
    }

}
