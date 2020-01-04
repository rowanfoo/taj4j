package com.dharma.algo.Controller

import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.pesistence.entity.data.CoreData
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
    lateinit var coreDataIgniteService: CoreDataIgniteService



//    @GetMapping("/dategt")
//    fun dategt(@RequestParam date: String , code:String ): Iterable<CoreData> = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.parse(date)).and(QCoreData.coreData.code.eq(code) )   )

//    @GetMapping("/dategt")
//    fun dategt(@RequestParam date: String, code: String): Iterable<CoreData> = ignitecache.values(" where code=?  and  date > ?  ", arrayOf(code, date))

    @GetMapping("/dategt")
    fun dategt(@RequestParam date: String, code: String): Iterable<CoreData> = coreDataIgniteService.dategt(code, date)


}