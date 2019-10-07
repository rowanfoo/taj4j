package com.learn.ta4j.utility

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.QCoreData
import com.dhamma.pesistence.entity.repo.DataRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Scheduler {

    @Autowired
    lateinit var dataRepo: DataRepo


    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>

    @Scheduled(cron = "0 15 17 ? * MON-FRI")
    fun ignitecache() {
        println("-----------------LOAD---SCHEUDULER-------------")
        println("-----------------PRE ---SIZE-----${ignitecache.size()}--------")

        var data = dataRepo.findAll(QCoreData.coreData.date.eq(LocalDate.now()))
        data.forEach { ignitecache.save("${it.code}:${it.date}", it) }

        println("-----------------LOAD---SIZE-----${ignitecache.size()}--------")


    }

}