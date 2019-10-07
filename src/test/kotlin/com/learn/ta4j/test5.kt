package com.learn.ta4j

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class test4 {

    @Autowired
    lateinit var allStocks: Map<String, CoreStock>
    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>


    @Test
    fun findsectors() {
        println("-------------findsectors  ${ignitecache}----------------------")


    }

}
