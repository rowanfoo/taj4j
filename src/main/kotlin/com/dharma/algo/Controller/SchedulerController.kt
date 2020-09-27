package com.dharma.algo.Controller

import com.dharma.algo.service.SchdulderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class SchedulerController {

    @Autowired
    lateinit var schdulderService: SchdulderService


    @GetMapping("/scheduler/{userid}")
    fun getalldetails(@PathVariable userid: String) {
        println("-------LOAD ALL SCHEDULE------------------")
        return schdulderService.createHistoricIndicator(userid)
    }
}
