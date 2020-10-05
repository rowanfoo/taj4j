package com.dharma.algo.service.algo

import com.dharma.algo.data.pojo.techstr
import com.google.gson.JsonObject

interface IProcess {
    fun process(data: JsonObject, addDate: Boolean, addNews: Boolean, addFund: Boolean, addStock: Boolean): List<techstr>
}
