package com.dharma.algo.data.pojo

import com.dhamma.pesistence.entity.data.Fundamental
import com.dhamma.pesistence.entity.data.News
import java.time.LocalDate

class techstr(var code: String, var date: LocalDate, var type: String, var message: String) {
    var stock: Stock? = null
    var news: List<News>? = null
    var fundamental: Fundamental? = null

}