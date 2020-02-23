package com.dharma.algo.Controller

import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.pesistence.entity.data.QSummary
import com.dhamma.pesistence.entity.data.Summary
import com.dhamma.pesistence.entity.repo.SummaryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class SummaryController {

    @Autowired
    lateinit var summaryRepo: SummaryRepo

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService


    @GetMapping("/summary/today")
    fun summaryToday(): Summary {
        var date = coreDataIgniteService.today().date
        return summaryRepo.findOne(QSummary.summary.date.eq(date)).get()
    }

    @GetMapping("/summary/all")
    fun summaryAll(): Iterable<Summary> {
        var date = coreDataIgniteService.today().date
        return summaryRepo.findAll(QSummary.summary.date.asc())
    }

    @GetMapping("/summary/all/rsi")
    fun summaryRsi(): Iterable<Summary> {
        var date = coreDataIgniteService.today().date
        return summaryRepo.findAll()
    }


}