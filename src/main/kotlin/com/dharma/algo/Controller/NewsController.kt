package com.dharma.algo.Controller

import com.dhamma.ignitedata.service.HistoryIndicatorService
import com.dhamma.pesistence.entity.data.News
import com.dhamma.pesistence.entity.data.QNews
import com.dhamma.pesistence.entity.repo.NewsRepo
import com.dharma.algo.service.NewsService
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*


@CrossOrigin
@RestController
class NewsController {
    @Autowired
    lateinit var newsRepo: NewsRepo

    @Autowired
    lateinit var historyIndicatorService: HistoryIndicatorService

    @Autowired
    lateinit var newsService: NewsService

    @GetMapping("/news/all")
    fun list(
        @RequestParam date: Optional<String>, @RequestParam(name = "page", defaultValue = "0") page: Int,
        @RequestParam(name = "size", defaultValue = "50") size: Int
    ): Page<ObjectNode>? {

        lateinit var startdate: String
        lateinit var enddate: String

        val pageRequest = PageRequest.of(page, size)
        lateinit var pageResult: Page<News>

        if (date.isPresent()) {
            if (date.get().indexOf(',') > 0) {
                var z = date.get().split(",")
                startdate = z[0]
                enddate = z[0]
                pageResult = newsRepo.findAll(
                    QNews.news.date.between(LocalDate.parse(startdate), LocalDate.parse(enddate)),
                    pageRequest
                )
            } else {
                var date = historyIndicatorService.dateExsits(date.get())
                pageResult = newsRepo.findAll(QNews.news.date.eq(date), pageRequest)
            }
        } else {
            var today = historyIndicatorService.today()
            pageResult = newsRepo.findAll(QNews.news.date.eq(today), pageRequest)
        }

        var x = newsService.addStockInfo(pageResult.content, true, page.toString(), size.toString())
        return PageImpl(x, pageRequest, pageResult.getTotalElements());
    }

    @GetMapping("/news/week")
    fun getThisWeekNews(@PathVariable code: String): List<News> {

        var a = LocalDate.now().dayOfWeek.value
        var b = LocalDate.now().with(DayOfWeek.MONDAY)

        return if (LocalDate.now().dayOfWeek.value == 0) {
            newsRepo.findAll(QNews.news.date.eq(LocalDate.now()).and(QNews.news.code.eq(code))).toList()
        } else {

            var start = LocalDate.now().with(DayOfWeek.MONDAY)
            newsRepo.findAll(QNews.news.date.between(start, LocalDate.now()).and(QNews.news.code.eq(code)))
                .toList()
        }
    }


}


