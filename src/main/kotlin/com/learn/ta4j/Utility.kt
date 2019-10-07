package com.learn.ta4j

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import com.dhamma.pesistence.entity.data.CoreStock
import org.ta4j.core.BaseTimeSeries
import org.ta4j.core.TimeSeries
import java.time.ZoneId

class ConvertUtily {

    companion object Factory {
        fun utility(data: List<CoreData>): TimeSeries {
            var app = BaseTimeSeries.SeriesBuilder().withName("my_2017_series").build()

            data.forEach {
                app.addBar(it.date.atStartOfDay(ZoneId.of("Australia/Sydney")), it.open, it.high, it.low, it.close, it.volume)
            }

            return app
        }


        fun filterTop(ignitecachestock: IgniteRepo<CoreStock>, sector: String): Map<String, CoreStock> {
            println("---------------------TOPS---FIND-------$sector------------")
            lateinit var querydata: List<CoreStock>
            if (sector == "100")
                querydata = ignitecachestock.values(" where  top in( ?,? )", arrayOf("100", "50"))
            else if (sector == "200")
                querydata = ignitecachestock.values(" where  top in( ?,?,? )", arrayOf("200", "100", "50"))
            else if (sector == "300")
                querydata = ignitecachestock.values(" where  top in( ?,?,?,? )", arrayOf("300", "200", "100", "50"))



            return querydata.map { it.code to it }.toMap()
        }

        fun round(d: Double): Double {
            return String.format("%.1f", d).toDouble()
        }

    }


}