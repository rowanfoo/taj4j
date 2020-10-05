package com.dharma.algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.data.QCoreData
import com.dhamma.pesistence.entity.repo.DataRepo
import com.dharma.algo.Controller.DataController
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate

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

    @Autowired
    lateinit var dataController: com.dharma.algo.Controller.DataController

    @Autowired
    lateinit var dataRepo: DataRepo

    //    fun dategt( date: String , code:String ): Iterable<CoreData> = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.parse(date)).and(QCoreData.coreData.code.eq(code) )   )
    fun dategt(date: String, code: String): Iterable<CoreData> = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.parse(date)).and(QCoreData.coreData.code.eq(code)))


    @Test
    fun findme() {
        //    var a = dategt ("2017-01-01" , "BHP.AX")
        var stock = ignitecache.values(" where code=?  and  date > ?  ", arrayOf("BHP.AX", "2017-01-01"))
        println("-----------------XXXXX-----------------------")
        stock.forEach {

            println("-----------------$it-----------------------")
        }


    }

}
