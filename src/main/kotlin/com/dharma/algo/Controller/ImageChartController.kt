package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.ImageChart
import com.dhamma.pesistence.entity.repo.ImageChartRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class ImageChartController {
    @Autowired
    lateinit var imageChartRepo: ImageChartRepo

    @PutMapping("/imagechart")
    fun insert(@RequestBody imageChart: ImageChart) {
        println("----insert-------------$imageChart")
        imageChartRepo.save(imageChart)
    }

}


