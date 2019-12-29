package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.service.algodata.CoreDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class DataController {
//
//    @Autowired
//    lateinit var dataRepo: DataRepo
//
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>

    @Autowired
    lateinit var coreDataService: CoreDataService


//    @GetMapping("/dategt")
//    fun dategt(@RequestParam date: String , code:String ): Iterable<CoreData> = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.parse(date)).and(QCoreData.coreData.code.eq(code) )   )

//    @GetMapping("/dategt")
//    fun dategt(@RequestParam date: String, code: String): Iterable<CoreData> = ignitecache.values(" where code=?  and  date > ?  ", arrayOf(code, date))

    @GetMapping("/dategt")
    fun dategt(@RequestParam date: String, code: String): Iterable<CoreData> = coreDataService.dategt(code, date)


}