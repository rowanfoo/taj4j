package com.dharma.algo.service

import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.HistoryIndicatorService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.*
import com.dhamma.pesistence.entity.repo.CommentRepo
import com.dhamma.pesistence.entity.repo.UserRepo
import com.dhamma.pesistence.entity.repo.WishlistRepo
import com.dharma.algo.utility.Json
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*


@Component
class WishlistService {
    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var wishlistRepo: WishlistRepo

    @Autowired
    lateinit var commentRepo: CommentRepo


//    @Autowired
//    lateinit var algoService: AlgoService

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService

    @Autowired
    lateinit var historyIndicatorService: HistoryIndicatorService

    fun wishlistsummary(username: String, date: Optional<String>): ArrayNode {

        println("---------wishlistsummary------------$username-----")
        var codes = allfavs(username)


        var mydate = if (date.isPresent) LocalDate.parse(date.get()) else historyIndicatorService.today()
        println("---------all codes-----------------$codes-----")
//        var userconfig = user(username).userConfig
//        var rsistring = userconfig.get("rsi")?.get(0)?.asJsonObject?.get("value").toString().replace("\"", "")
//        var falldailystring = userconfig.get("falldaily")?.get(0)?.asJsonObject?.get("value").toString().replace("\"", "")
//        var volumexstring = userconfig.get("volumex")?.get(0)?.asJsonObject?.get("value").toString().replace("\"", "")
//        var rsistring = userconfig.get("rsi")?.get(0)?.asJsonObject?.get("value")?.asString
//        var falldailystring = userconfig.get("falldaily")?.get(0)?.asJsonObject?.get("value")?.asString
//        var volumexstring = userconfig.get("volumex")?.get(0)?.asJsonObject?.get("value")?.asString

        // var list = mutableListOf<JsonNode>()
        //var map = mutableMapOf<String, String>()

//        lateinit var rsilist: Map<String, techstr>
//        lateinit var falldailylist: Map<String, techstr>
//        lateinit var volumexlist: Map<String, techstr>
        println("---------X111111111111-----------------")
        // load all of user config  , for ris , falldaily , voulume , then match to favs
        //  val (rsilist, falldailylist, volumexlist) = indicatorMap(username)

        println("---------X222222222222222-----------------")

        var map = historyIndicatorService.datecodes(codes, mydate)
        println("---------X3333333333333-----------------")
//        runBlocking {
//
//            launch {
//                if (rsistring != null) rsilist = algoService.rsi(rsistring, Optional.empty()).map { it.code to it }.toMap()
//            }
//
//            launch {
//                if (falldailystring != null) falldailylist = algoService.price(falldailystring, Optional.empty()).map { it.code to it }.toMap()
//            }
//
//            launch {
//                if (volumexstring != null) volumexlist = algoService.vol(volumexstring, Optional.empty()).map { it.code to it }.toMap()
//            }
//        }

        var mapper = ObjectMapper()
        val arrayNode = mapper.createArrayNode()
        codes.forEach {
            //            var message = rsilist.get(it)?.message ?: ""
//            message = message + " " + (falldailylist.get(it)?.message ?: "")
//            message = message + " " + (volumexlist.get(it)?.message ?: "")

            var rootNode = mapper.createObjectNode()
            (rootNode as ObjectNode).put("code", it)
//            (rootNode as ObjectNode).put("message", message)
            (rootNode as ObjectNode).put("message", message(map, it))
            println("--------dateeq-------$it--------------$mydate-")
            // can throw Exception here , if code ... has empty data for that day !! todo catch and warn
            var data = coreDataIgniteService.dateeq(it, mydate.toString())
            println("--------dateeq-------done----------")

            (rootNode as ObjectNode).put("price", data.close)

            (rootNode as ObjectNode).put("change", data.changepercent)

            (rootNode as ObjectNode).put("volume", data.volume)
            (rootNode as ObjectNode).put("date", data.date.toString())
            (rootNode as ObjectNode).put("news", Json.toJson(newsIgniteService.newsToday(it)))
            arrayNode.add(rootNode)
        }
        return arrayNode
    }


    private fun message(map: Map<String, List<HistoryIndicators>>, code: String): String {
//        var message = rsilist.get(code)?.message ?: ""
//        message = message + " " + (falldailylist.get(code)?.message ?: "")
//        message = message + " " + (volumexlist.get(code)?.message ?: "")
//        return message


        var message = ""
        var ls = map[code]
        ls?.forEach {

            if (it.type != IndicatorType.MA) {
                message = message + it.message + " , "
            }

        }
        return message

    }

    private fun allfavs(username: String): List<String> {
        var mutableList = mutableSetOf<String>()

        wishlistRepo.findAll(QWishlist.wishlist.userid.eq(username).and(QWishlist.wishlist.category.notLike("%INDEX%")))
            .forEach {
                mutableList.addAll(it.code.split(","))
            }


        commentRepo.findAll(QComment.comment.userid.eq(username).and(QComment.comment.isReject.eq(false))).forEach {
            mutableList.add(it.code)
        }
        return mutableList.toList();

    }

    private fun user(username: String): User = userRepo.findOne(QUser.user.username.eq(username)).get()
}
