package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.Fundamental
import com.dhamma.pesistence.entity.data.QFundamental
import com.dhamma.pesistence.entity.repo.FundamentalRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
class FundamentalController {

    @Autowired
    lateinit var fundamentalRepo: FundamentalRepo

    @GetMapping("/fundamental/{code}")
    fun getCode(@PathVariable code: String): Fundamental = fundamentalRepo.findOne(QFundamental.fundamental.code.eq(code)).get()

    @PutMapping("/fundamental")
    fun insert(@RequestBody fundamental: Fundamental) {
        println("----insert-------------$fundamental")
        fundamentalRepo.save(fundamental)

    }


}