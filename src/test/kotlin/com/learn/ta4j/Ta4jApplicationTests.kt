//package com.learn.ta4j
//
//import com.dhamma.api.IgniteRepo
//import com.dhamma.pesistence.entity.data.CoreData
//import com.dhamma.pesistence.entity.data.CoreStock
//import com.dhamma.pesistence.entity.data.QCoreData
//import com.dhamma.pesistence.entity.repo.DataRepo
//import com.google.gson.JsonObject
//import com.learn.ta4j.data.MA
//import org.apache.ignite.Ignite
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//import org.ta4j.core.BaseTimeSeries
//import org.ta4j.core.indicators.SMAIndicator
//import org.ta4j.core.indicators.helpers.ClosePriceIndicator
//import org.ta4j.core.indicators.helpers.VolumeIndicator
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.ZoneId
//import java.util.concurrent.TimeUnit
//
//
//@RunWith(SpringRunner::class)
//@SpringBootTest
//class Ta4jApplicationTests {
//    @Autowired
//    lateinit var dataRepo: DataRepo
//    @Autowired
//    lateinit var ignitecachestock: IgniteRepo<CoreStock>
//
//    @Autowired
//    lateinit var ignitecache: IgniteRepo<CoreData>
//
//
//
//
//    @Test
//    fun contextLoads() {
//        var data = dataRepo.findAll(QCoreData.coreData.date.gt(LocalDate.now().minusYears(3)).and(
//                QCoreData.coreData.code.eq("BHP.AX")
//
//        ))
//
//        val series = BaseTimeSeries.SeriesBuilder().withName("my_2017_series").build()
//        var time = LocalTime.now()
//        var count = 0
//        data.forEach {
//            series.addBar(it.date.atStartOfDay(ZoneId.of("Australia/Sydney")), it.open, it.high, it.low, it.close, it.volume)
////            fun addBar  (endTime: ZonedDateTime, openPrice: Number, highPrice: Number, lowPrice: Number, closePrice: Number, volume: Number) {
////                this.addBar(endTime, this.numOf(openPrice), this.numOf(highPrice), this.numOf(lowPrice), this.numOf(closePrice), this.numOf(volume))
////            }
//            count++
//        }
//        println("--------count++----$count  ")
//
//        val firstClosePrice = series.getBar(299).closePrice
//
//        println("----total- ${series.barCount}-")
//        println("----price - ${series.getBar(series.barCount - 1).closePrice}-")
//
//        println("First close price: ${firstClosePrice.doubleValue()}")
//
//        val closePrice = ClosePriceIndicator(series)
//// Here is the same close price:
//        println("---------or ${closePrice.getValue(299)}---")
//        println("---------latest ${closePrice.getValue(series.endIndex)}---")
//
//
//        val shortSma = SMAIndicator(closePrice, 50)
//        println("-------sma5----${shortSma.getValue(299).doubleValue()}")
//        println("-------sma51----${shortSma.getValue(270).doubleValue()}")
//        println("-------sma51----${shortSma.getValue(series.endIndex).doubleValue()}")
//
//        var volume = VolumeIndicator(series)
//        val volsma = SMAIndicator(volume, 60)
//        println("-------vol ----${volume.getValue(series.endIndex).doubleValue()}")
//        println("-------sma51----${volsma.getValue(series.endIndex).doubleValue()}")
//
//
//    }
//
//    @Autowired
//    lateinit var ma: MA
//    @Autowired
//    lateinit var ignite: Ignite
//
//    @Test
//    fun test2() {
//        println("--------RUN-----")
//        // ma.loadall()
//
//        // ma.load(50, "price")
//        //var cache = ignite.getOrCreateCache<String, Double>("MA50")
//
//        //println("-------vol---${cache.size()}---")
//
//        //   println("---------bhp---${cache.get("BHP.AX")} ---")
//        // println("---------bhp---${cache.get("RIO.AX")} ---")
//
//
//    }
//
//
//    @Test
//    fun test3() {
//        val content = JsonObject()
//        content.addProperty("id", "foo")
//        content.addProperty("no", 100)
//        println("--------RUN----${content.get("id")}-")
//        println("--------RUN----${content.get("no")}-")
//
//    }
//
//
//    @Test
//    fun test4() {
//
////        var corr :List<CoreData> = ignitecache.values( " where code=? ", arrayOf("BHP.AX"))
//        var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ? ", arrayOf("BHP.AX", "1"))
//
//        println("--------RUN----${querydata.size}-")
//
//        println("--------RUN----${querydata}-")
//    }
//
//    @Test
//    fun test5() {
//        var querydata = ignitecache.values(" where  date=? and changepercent < -0.04 ", arrayOf("2019-07-19"))
//        println("--------RUN----${querydata}-")
//
//    }
//
//
//    @Test
//    fun test6() {
//        var querydata = ignitecache.values(" where  date=? and volume <  ", arrayOf("2019-08-05"))
//        println("--------RUN----$querydata-")
//
//    }
//
//    @Test
//    fun test7() {
//        ma.loadall()
//        TimeUnit.SECONDS.sleep(5)
//        var cache3 = ignite.getOrCreateCache<String, Double>("MA60:vol")
//        println("--------RUN----${cache3.get("A2M.AX")}-")
//
//    }
//
//
//    @Test
//    fun test8() {
//
//        println("------------------------FIND ALL STOCKS ---------------------------")
//        //   var querydata = ignitecachestock.values(" where  top = ? ", arrayOf("100"))
////        var querydata = ignitecachestock.values(" where  top in( ? )", arrayOf("200,100"))
//        var querydata = ignitecachestock.values(" where  top in (?,?)", arrayOf("200","100"))
//
//        querydata.forEach { println("============stock==========${it.code}--------------${it.top}") }
//
//    }
//
//    @Test
//    fun test9() {
//
//        println("------------------------FIND ALL Lowest ---------------------------")
//        var sql = "SELECT  max(close)  from CoreData where code=? order by date desc  LIMIT 10 " +
//                "UNION " +
//                "SELECT close from CoreData where code=? order by date desc  LIMIT 1"
//
//
////        var querydata = ignitecache.fields("select  date ,max(close)  from CoreData  group by code order by date desc  LIMIT ?  ", arrayOf("5"))
//
//        var querydata = ignitecache.fields("select  date close from CoreData  group by code order by date desc  LIMIT ?  ", arrayOf("5"))
//
//        querydata.forEach { println("============stock==========$it--------------") }
//
//    }
//
//    @Test
//    fun test10() {
//
//        println("------------------------FIND ALL Lowest ---------------------------")
//
//        var querydata = ignitecache.values(" where code=?  order by date desc  LIMIT ?  ", arrayOf("RIO.AX", "14"))
//
//        var last = querydata.first().close
//        var max = querydata.maxBy { it.close }?.close
//
//        println("------------------MAX price ------------$max -----------------today-----------$last--------------------${(max!! - last) / max!!}")
//
////        querydata.forEach { println("============stock==========${it.code}--------------${it.close}") }
//
//
//    }
//
//}
