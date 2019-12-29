package com.dharma.algo.utility

import com.dhamma.base.ignite.IgniteRepo
import com.dhamma.pesistence.entity.data.CoreData
import org.springframework.beans.factory.annotation.Autowired

object DataUtility {
    @Autowired
    lateinit var ignitecache: IgniteRepo<CoreData>

    fun todayData(code: String): CoreData {
        var today = ignitecache.values(" where code=?   order by date desc limit ?  ", arrayOf(code, "1"))
        return today[0]
    }
}