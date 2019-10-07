package com.learn.ta4j

import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Load {

    @Autowired
    lateinit var ignite: Ignite
//    @Autowired
//    lateinit var dataRepo: DataRepo


//    @Autowired
//    lateinit var igniterepo: IgniteRepo<CoreData>

//    @Autowired
//    lateinit var igniterepo: IgniteRepo<TimeSeries>


    @Autowired
    lateinit var stocklist: List<String>


    fun load() {

        println("-----------------LOAD-----------------")
//        var data = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.now().minusYears(3)).and(
//                QCoreData.coreData.code.eq("BHP.AX")
//
//        ))
//        println("-----------------LOAD--1---------------")
//
//        var igniterepo = IgniteRepo<CoreData>(ignite, Class.forName("com.dhamma.pesistence.entity.data.CoreData").kotlin)

        println("-----------------LOAD---2--------------")

//        data.forEach {
//            igniterepo.save(it.igniteid, it)
//
//
//        }


//        loadlist.forEach {
//
//            var data = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.now().minusYears(2)).and(
//                    QCoreData.coreData.code.eq("$it.AX")
//
//            ))
////            println("-----------------LOAD--------------$it.AX---")
//
//            var series = BaseTimeSeries.SeriesBuilder().withName("$it.AX").build()
//            var time = LocalTime.now()
//            data.forEach {
//                series.addBar(it.date.atStartOfDay(ZoneId.of("Australia/Sydney")), it.open, it.high, it.low, it.close, it.volume)
//            }
//            igniterepo.save("$it.AX", series)
//
//
//        }
//

//        val series = BaseTimeSeries.SeriesBuilder().withName("my_2017_series").build()
//        var time = LocalTime.now()
//        data.forEach {
//            series.addBar(it.date.atStartOfDay(ZoneId.of("Australia/Sydney")), it.open, it.high, it.low, it.close, it.volume)
//
//
//        }


    }


}