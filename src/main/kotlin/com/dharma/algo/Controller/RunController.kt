package com.dharma.algo.Controller

import com.dhamma.ignitedata.manager.ImportManager
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.service.AlgoService
import com.dharma.algo.service.AlgoService1
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern

@CrossOrigin
@RestController
class RunController {

    @Autowired
    lateinit var algoService: AlgoService

    @Autowired
    lateinit var algoService1: AlgoService1

    @GetMapping("/")
    fun root(): String {
        return "hello world"
    }

    @GetMapping("/ma/{ma}")
    fun ma(@PathVariable ma: String, sectorparam: Optional<String>): List<techstr> {
        return algoService.ma(ma, sectorparam)
    }

    //http://localhost:8080/volumex/60<3
    @GetMapping("/volumex/{algo}")
    fun vol(@PathVariable algo: String, @RequestParam sectorparam: Optional<String>): List<techstr> {
        return algoService.vol(algo, sectorparam)
    }

    @GetMapping("/fallshort")
    fun fallshort(): List<techstr> {
        return algoService.fallshort()
    }

    @GetMapping("/falllong")
    fun falllong(): List<techstr> {
        return algoService.falllong()
    }

    @GetMapping("/abc")
    fun data() {
        algoService.data()
    }

    //http://localhost:8080/rsi/30<14
    //14<30
//    @GetMapping("/rsi/{algo}")
//    fun getbydate(@PathVariable algo: String, sectorparam: Optional<String>): List<techstr> {
//        return algoService.rsi(algo)
//    }

    @GetMapping("/algo/{id}")
    fun algo(
        @PathVariable id: String, @RequestParam date: Optional<String>,
        @RequestParam(name = "page", defaultValue = "0") page: Int,
        @RequestParam(name = "size", defaultValue = "50") size: Int
    ): Page<techstr> {
        return algoService1.process(id, date, true, true, true, true, Pair<Int, Int>(page, size))
    }

//
//
//    @GetMapping("/rsi/{id}")
//    fun getbydate(@PathVariable id: String, sectorparam: Optional<String>): List<techstr> {
//        println("---RSI----------")
//        return algoService1.process(id, true, true, true, true)
//    }


    @GetMapping("/falldaily/{algo}")
    fun price(@PathVariable algo: String, @RequestParam sectorparam: Optional<String>): List<techstr> {
        return algoService.price(algo, sectorparam)
    }

    private fun getThreeElems(zz: String): Triple<String, String, String> {
//        val zz = "30<14"
        val r = Pattern.compile("<|>|=")
        val m = r.matcher(zz)
        m.find()
        println("Start index: " + m.start())
        println("----------------" + zz.substring(0, m.start()))
        println("----------------" + zz.substring(m.start(), m.end()))
        println("----------------" + zz.substring(m.end(), zz.length))
        println(" End index: " + m.end())
        println(" Found: " + m.group())
        return Triple(zz.substring(0, m.start()), zz.substring(m.start(), m.end()), zz.substring(m.end(), zz.length))
    }

    private fun getTwoElems(zz: String): Pair<String, String> {
        val r = Pattern.compile("<|>|=")
        val m = r.matcher(zz)
        m.find()
        println("Start index: " + m.start())
        println("----------------" + zz.substring(0, m.start()))
        println("----------------" + zz.substring(m.start(), m.end()))
        println(" End index: " + m.end())
        println(" Found: " + m.group())
        return Pair(zz.substring(0, m.start()), zz.substring(m.start(), m.end()))
    }

//    @Autowired
//    lateinit var coredatascheduler: CoreDataScheduler
//
//    @Autowired
//    lateinit var summaryService: SummaryService
//

    @Autowired
    lateinit var importManager: ImportManager


    @GetMapping("/reset")
    fun reset() {
        println("-----START IMPORT ALGO---${LocalDate.now()}---")
        importManager.startimport()

        println("-----reset2------")


    }

}
