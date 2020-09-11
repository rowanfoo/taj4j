package com.dharma.algo.service.algo

/*
@Component
class MARunning {


    @Autowired
    lateinit var maIgniteService: MaIgniteService

//    fun process(data: JsonObject): List<techstr> {
////        var percent = data.get("percent").asDouble
////        var code = data.get("code").asString
////       var ma = marunningService.twohundredma(code)
//
//        var array: IgniteCache<String, Double> = maIgniteService.getCache(data)
//        var list = mutableListOf<techstr>()
//
//        array.filter { it.value > 0 }.forEach {
//            var code = it.key
//            var ma = marunningService.twohundredma(code)
//            val end = ma.timeSeries.endIndex - 1
//            val start = ma.timeSeries.endIndex - 60
//            var counter = 0
//            for (i in start until end) {
//                if (ma.getValue(start).) {
//
//                }
//
//            }
//
//
//        }
//
//        return null
//    }


    fun process(data: JsonObject): List<techstr> {
        var time = data.get("time").asInt
        var number = data.get("number").asInt
        var alls: List<techstr> = mutableListOf<techstr>()

        println("--------MA RUNNING------process--$time--------$number")
        runBlocking {
            println("--------MA RUNNING------1")

            //            runData(time, number, all)
            runData(time, number, alls)
            println("--------MA RUNNING------2")

        }
        println("--------MA RUNNING------3")

        return alls

    }

    @Autowired
    lateinit var marunningService: MArunningService

    @Autowired
    lateinit var stocklist: List<String>


    suspend fun runData(time: Int, number: Int, list: List<techstr>) = withContext(Dispatchers.Default) {
        var alls: List<techstr> = mutableListOf<techstr>()
        stocklist.map {
            launch {
                var data = marunningService.codeOffset(it, time)
                var series = data.timeSeries

                var start = data.timeSeries.endIndex - number
                var end = data.timeSeries.endIndex - 1


                var pricet = series.getBar(data.timeSeries.endIndex).closePrice.doubleValue()
                var twohundredt = data.getValue(data.timeSeries.endIndex).doubleValue()

                if (pricet > twohundredt) {
                    var counter = 0
                    for (i in start..end) {

                        var price = series.getBar(i).closePrice.doubleValue()
                        var priced = series.getBar(i).dateName
                        var twohundred = data.getValue(i).doubleValue()

                        if (twohundred > price) {
                            counter++
                        }
                    }

                    if (counter > (number - 10)) {
                        println("-------BELOW 200-->>>>>>>>-FOUND--------$it")
                    }
                }
            }
        }
    }

}



*/
