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
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    fun list(@RequestParam date: Optional<String>, @RequestParam(name = "page", defaultValue = "0") page: Int,
             @RequestParam(name = "size", defaultValue = "50") size: Int): Page<ObjectNode>? {

        var mydate = if (date.isPresent()) historyIndicatorService.dateExsits(date.get()) else historyIndicatorService.today()

        val pageRequest = PageRequest.of(page, size)
        val pageResult: Page<News> = newsRepo.findAll(QNews.news.date.eq(mydate), pageRequest)

        var x = newsService.addStockInfo(pageResult.content, true, page.toString(), size.toString())
        return PageImpl(x, pageRequest, pageResult.getTotalElements());


    }


}


