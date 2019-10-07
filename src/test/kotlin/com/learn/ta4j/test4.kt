package com.learn.ta4j

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class test5 {
//    @Autowired
//    lateinit var dataRepo: DataRepo
//
//    @PersistenceContext
//    lateinit var em: EntityManager
//
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>


    @Test
    fun findsectors() {
//        var stock = ignitecachestock.values(" where code=?  LIMIT ? ", arrayOf("BHP.AX", "1"))
//
//        var stk = stock.first()
//        println("-------------findsectors  ${stk}----------------------")

        var predicate: (String) -> Boolean = { a -> a == ("a") }
        var mylist = listOf("a", "b", "c")
        var ans = mylist.filter(predicate)

        println("--------ans------$ans--")


//        predicate("abc")


    }


}
