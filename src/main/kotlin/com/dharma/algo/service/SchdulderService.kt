package com.dharma.algo.service

import com.dhamma.pesistence.entity.data.HistoryIndicators
import com.dhamma.pesistence.entity.data.IndicatorType
import com.dhamma.pesistence.entity.data.QUser
import com.dhamma.pesistence.entity.data.User
import com.dhamma.pesistence.entity.repo.HistoryIndicatorsRepo
import com.dhamma.pesistence.entity.repo.UserRepo
import com.dharma.algo.data.pojo.techstr
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


@Component
class SchdulderService {
    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var algoService: AlgoService


    @Autowired
    lateinit var historyIndicatorsRepo: HistoryIndicatorsRepo

    fun createHistoricIndicator(username: String) {
        println("------createHistoricIndicator---------DATA--------")
        var alldata = indicatorMap(username)

        println("------createHistoricIndicator------ WILL SAVE DATA-----------")
        var data = mutableListOf<HistoryIndicators>()
        alldata.forEach {

            it.values.forEach {
                data.add(historyIndicator(it, username))
            }


        }
        println("------createHistoricIndicator------ SAVE DATA-----------")
        historyIndicatorsRepo.saveAll(data)
        println("------createHistoricIndicator------ SAVE DATA  DONE-----------")

    }

    /**
     * !!!! In future , need to put , clear all IGNite just in i run the normal web , and it  run incomplete rsi list.
     */
    private fun indicatorMap(username: String): List<Map<String, techstr>> {
        var userconfig = user(username).userConfig
        var rsistring = userconfig.get("rsi")?.get(0)?.asJsonObject?.get("value")?.asString
        var falldailystring = userconfig.get("falldaily")?.get(0)?.asJsonObject?.get("value")?.asString
        var volumexstring = userconfig.get("volumex")?.get(0)?.asJsonObject?.get("value")?.asString
        lateinit var rsilist: Map<String, techstr>
        lateinit var falldailylist: Map<String, techstr>
        lateinit var volumexlist: Map<String, techstr>

        var list = mutableListOf<Map<String, techstr>>()


        runBlocking {

            launch {
                if (rsistring != null) {
                    //   rsilist = algoService.rsi(rsistring, Optional.empty()).map { it.code to it }.toMap()
                    list.add(algoService.rsi(rsistring, Optional.empty()).map { it.code to it }.toMap())
                }
            }

            launch {
                if (falldailystring != null) {
                    //                   falldailylist = algoService.price(falldailystring, Optional.empty()).map { it.code to it }.toMap()
                    list.add(algoService.price(falldailystring, Optional.empty()).map { it.code to it }.toMap())
                }
            }

            launch {
                if (volumexstring != null) {
//                    volumexlist = algoService.vol(volumexstring, Optional.empty()).map { it.code to it }.toMap()
                    list.add(algoService.vol(volumexstring, Optional.empty()).map { it.code to it }.toMap())
                }
            }
        }
        return list
    }


    private fun user(username: String): User = userRepo.findOne(QUser.user.username.eq(username)).get()


    private fun historyIndicator(tech: techstr, userid: String): HistoryIndicators {
        return com.dhamma.pesistence.entity.data.HistoryIndicators.builder()
                .code(tech.code)
                .date(tech.date)
                .message(tech.message)
                .userid(userid)
                .type(indicatorType(tech.type))
                .build()

    }

    private fun indicatorType(type: String): IndicatorType {
        return when {
            (type.equals("RSI")) -> IndicatorType.RSI
            (type.indexOf("fall") > 0) -> IndicatorType.PRICE_FALL
            (type.equals("VOl")) -> IndicatorType.VOLUME
            else -> IndicatorType.NONE
        }
    }
}
