package com.dharma.algo.Controller

import com.dhamma.ignitedata.manager.ImportManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class SchedulerController {

    //    @Autowired
//    lateinit var schdulderService: SchdulderService
    @Autowired
    lateinit var importManager: ImportManager


    @GetMapping("/scheduler/{userid}")
    fun getalldetails(@PathVariable userid: String) {
        println("-------LOAD ALL SCHEDULE------------------")
        importManager.startimport()
        //  return schdulderService.createHistoricIndicator(userid)
    }
}
