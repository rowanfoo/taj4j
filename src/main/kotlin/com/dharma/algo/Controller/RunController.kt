package com.dharma.algo.Controller

import com.dhamma.ignitedata.utility.CoreDataScheduler
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.service.AlgoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.regex.Pattern

@CrossOrigin
@RestController
class RunController {

    @Autowired
    lateinit var algoService: AlgoService


    @GetMapping("/")
    fun root(): String {
        return "hello world"
    }

    @GetMapping("/ma/{ma}")
    fun ma(@PathVariable ma: String, sectorparam: Optional<String>): List<techstr> {
        return algoService.ma(ma, sectorparam)
    }

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

    //14<30
    @GetMapping("/rsi/{algo}")
    fun getbydate(@PathVariable algo: String, sectorparam: Optional<String>): List<techstr> {
        return  algoService.rsi(algo, sectorparam)
    }

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

    @Autowired
    lateinit var coredatascheduler: CoreDataScheduler

    @GetMapping("/reset")
    fun reset() {
        println("-----reset1------")
        coredatascheduler.ignitecache()
        println("-----reset2------")

    }
}
