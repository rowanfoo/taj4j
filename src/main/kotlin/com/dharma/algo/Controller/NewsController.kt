package com.dharma.algo.Controller

import com.dhamma.ignitedata.service.HistoryIndicatorService
import com.dhamma.pesistence.entity.data.News
import com.dhamma.pesistence.entity.data.QNews
import com.dhamma.pesistence.entity.repo.NewsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
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

//    @GetMapping("/news/all")
//    fun list(@RequestParam date: Optional<String>, @RequestParam(name = "page", defaultValue = "0") page: Int,
//             @RequestParam(name = "size", defaultValue = "50") size: Int): List<News>? {
//        println("----NewsController-list ${date}-----$page-----$size------")
//
//        //  var mydate = if (date.isPresent()) historyIndicatorService.dateExsits(date.get()) else historyIndicatorService.today()
//        var mydate = LocalDate.parse(date.get())
//        println("----NewsController-mydate ${mydate}-----------")
//
//        val pageRequest = PageRequest.of(page, size)
//        val pageResult: Page<News> = newsRepo.findAll(QNews.news.date.eq(mydate), pageRequest)
//        println("---RESULT---------$pageResult-")
//        return pageResult.content
//
//
//    }


    @GetMapping("/news/all")
    fun list(@RequestParam date: Optional<String>, @RequestParam(name = "page", defaultValue = "0") page: Int,
             @RequestParam(name = "size", defaultValue = "50") size: Int): Page<News>? {
        println("----NewsController-list ${date}-----$page-----$size------")

        var mydate = if (date.isPresent()) historyIndicatorService.dateExsits(date.get()) else historyIndicatorService.today()
        // var mydate = LocalDate.parse(date.get())
        println("----NewsController-mydate ${mydate}-----------")

        val pageRequest = PageRequest.of(page, size)
        val pageResult: Page<News> = newsRepo.findAll(QNews.news.date.eq(mydate), pageRequest)
        println("---RESULT---------$pageResult-")
        return pageResult


    }


}


//@GetMapping
//fun list(@RequestParam(name = "page", defaultValue = "0") page: Int,
//         @RequestParam(name = "size", defaultValue = "10") size: Int): Page<TodoResponse>? {
//    val pageRequest = PageRequest.of(page, size)
//    val pageResult: Page<Todo> = todoRepository.findAll(pageRequest)
//    val todos: List<TodoResponse> = pageResult
//            .stream()
//            .map({ TodoResponse() })
//            .collect(toList())
//    return PageImpl<T>(todos, pageRequest, pageResult.getTotalElements())
//}
