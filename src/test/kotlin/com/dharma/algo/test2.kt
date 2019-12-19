//package com.learn.ta4j
//
//import com.dhamma.api.IgniteRepo
//import com.dhamma.pesistence.data.data.CoreData
//import com.dhamma.pesistence.data.data.CoreStock
//import com.dhamma.pesistence.data.data.QCoreData
//import com.dhamma.pesistence.data.repo.DataRepo
//import com.querydsl.jpa.impl.JPAQueryFactory
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//import javax.persistence.EntityManager
//import javax.persistence.PersistenceContext
//
//
//@RunWith(SpringRunner::class)
//@SpringBootTest
//class test2 {
//    @Autowired
//    lateinit var dataRepo: DataRepo
//
//    @PersistenceContext
//    lateinit var em: EntityManager
//
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>
//
//
//    @Test
//    fun findsectors() {
//        var stock = ignitecachestock.values(" where code=?  LIMIT ? ", arrayOf("BHP.AX", "1"))
//
//        var stk = stock.first()
//        println("-------------findsectors  ${stk}----------------------")
//    }
//
//
//    @Test
//    fun contextLoads() {
//        val query = JPAQueryFactory(em)
//        var arr: List<CoreData> = query.from(QCoreData.coreData)
//                .where(QCoreData.coreData.code.eq("ptm.ax"))
//                .orderBy(QCoreData.coreData.date.desc()).limit(15).fetch() as List<CoreData>
//
//        // var arr2 = arr.subList(0,14)
//
//        arr.forEach {
//
//            println("-------------------------$it")
//        }
//
//        var a = Calc().calculateRsi(arr.reversed())
//
//        println("---------VAR----------------$a")
//    }
//
//}
