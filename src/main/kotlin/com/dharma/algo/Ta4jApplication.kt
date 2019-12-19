package com.dharma.algo

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import com.dhamma.pesistence.entity.data.QCoreData
import com.dhamma.pesistence.entity.repo.DataRepo
import com.dhamma.pesistence.entity.repo.StockRepo
import org.apache.ignite.Ignite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDate

@SpringBootApplication
@ComponentScan(basePackages = ["com.dhamma.base.ignite", "com.learn.ta4j", "com.dhamma.pesistence", "com.dhamma.algodata"])
@EnableScheduling

class Ta4jApplication : CommandLineRunner {
//    @Autowired
//    lateinit var load: Load

    @Autowired
    lateinit var stockrepo: StockRepo

    @Autowired
    lateinit var dataRepo: DataRepo

    override fun run(vararg args: String?) {
        println("----------------will load now--------")
//        load.load()

    }

    @Bean
    fun allStocks(): Map<String, CoreStock> {

        var list = stockrepo.findAll()
        return list.map { it.code to it }.toMap()

    }


    @Bean
    fun stocklist(): List<String> {
        println("------------------------------STOCKLIST--------------")
//        var d = dataRepo.findOne(QCoreData.coreData.id.eq(1L))
//        println("------------------------------COREDATA----------$d----")
        var list = stockrepo.findAll()

        return list.map { it.code }.toList()

//        return listOf("BHP.AX", "RIO.AX", "NAB.AX", "WBC.AX", "CBA.AX")
//        return listOf("ABC.AX", "BHP.AX", "WBC.AX")
        //   return listOf("BHP.AX")

    }


    @Bean
    fun ignitecache(ignite: Ignite): IgniteRepo<CoreData> {

        println("-----------------LOAD---ignitecache-------------")

//        var igniterepo = IgniteRepo<CoreData>(ignite, Class.forName("com.dhamma.pesistence.data.data.CoreData").kotlin)
        var igniterepo = IgniteRepo<CoreData>(ignite, CoreData())

        var mydata = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.now().minusYears(2)))
        mydata.forEach { igniterepo.save("${it.code}:${it.date}", it) }

        println("-----------------LOAD---SIZE-----${igniterepo.size()}--------")


        var querydata = igniterepo.values(" where code=? ", arrayOf("BHP.AX"))
        println("----------------------data---------------------${querydata.size}-")


        return return igniterepo


    }

    @Bean
    fun ignitecachestock(ignite: Ignite): IgniteRepo<CoreStock> {

        println("-----------------LOAD-----STOCK-------ignitecache-------------")

//        var igniterepo = IgniteRepo<CoreData>(ignite, Class.forName("com.dhamma.pesistence.data.data.CoreData").kotlin)
        var igniterepo = IgniteRepo<CoreStock>(ignite, CoreStock())
        var list = stockrepo.findAll().forEach {
            igniterepo.save("${it.code}", it)
        }

        println("-----------------LOAD---SIZE-----${igniterepo.size()}--------")
        return return igniterepo


    }


}

fun main(args: Array<String>) {
    runApplication<Ta4jApplication>(*args)
}
