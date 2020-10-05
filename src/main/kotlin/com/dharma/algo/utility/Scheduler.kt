package com.dharma.algo.utility

import org.springframework.stereotype.Component

@Component
class Scheduler {
//
//    @Autowired
//    lateinit var dataRepo: DataRepo
//
//
//    @Autowired
//    lateinit var igniteUtility: IgniteUtility
//
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>
//
//    //    @Scheduled(cron = "0 15 17 ? * MON-FRI")
//    @Scheduled(cron = "0 19 15 ? * MON-FRI", zone = "GMT-8")
//    fun ignitecache() {
//        println("-----------------LOAD---SCHEUDULER-------------")
//        println("-----------------PRE ---SIZE-----${ignitecache.size()}--------")
//        igniteUtility.clearalldata()
//        println("-----------------PRE ---SIZE-----${ignitecache.size()}--------")
//
//
//        var data = dataRepo.findAll(QCoreData.coreData.date.eq(LocalDate.now()))
//        data.forEach { ignitecache.save("${it.code}:${it.date}", it) }
//
//        println("-----------------LOAD---SIZE-----${ignitecache.size()}--------")
//
//
//    }

}