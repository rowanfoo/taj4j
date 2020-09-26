package com.dharma.algo.service

import com.dhamma.ignitedata.service.CoreDataIgniteService
import com.dhamma.ignitedata.service.NewsIgniteService
import com.dhamma.pesistence.entity.data.QUser
import com.dhamma.pesistence.entity.data.QWishlist
import com.dhamma.pesistence.entity.data.User
import com.dhamma.pesistence.entity.repo.UserRepo
import com.dhamma.pesistence.entity.repo.WishlistRepo
import com.dharma.algo.data.pojo.techstr
import com.dharma.algo.utility.Json
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import java.util.*


@Controller
class WishlistService {
    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var wishlistRepo: WishlistRepo


    @Autowired
    lateinit var algoService: AlgoService

    @Autowired
    lateinit var newsIgniteService: NewsIgniteService

    @Autowired
    lateinit var coreDataIgniteService: CoreDataIgniteService
    var mapper = ObjectMapper()

    private fun indicatorMap(username: String): Triple<Map<String, techstr>, Map<String, techstr>, Map<String, techstr>> {
        var userconfig = user(username).userConfig
        var rsistring = userconfig.get("rsi")?.get(0)?.asJsonObject?.get("value")?.asString
        var falldailystring = userconfig.get("falldaily")?.get(0)?.asJsonObject?.get("value")?.asString
        var volumexstring = userconfig.get("volumex")?.get(0)?.asJsonObject?.get("value")?.asString
        lateinit var rsilist: Map<String, techstr>
        lateinit var falldailylist: Map<String, techstr>
        lateinit var volumexlist: Map<String, techstr>

        runBlocking {

            launch {
                if (rsistring != null) rsilist = algoService.rsi(rsistring, Optional.empty()).map { it.code to it }.toMap()
            }

            launch {
                println("---------falldailystring---------------$falldailystring-----")
                if (falldailystring != null) falldailylist = algoService.price(falldailystring, Optional.empty()).map { it.code to it }.toMap()
                println("---------falldailystring------size---------${falldailylist.size}-----")
            }

            launch {
                println("---------Y1-----------------")
                if (volumexstring != null) volumexlist = algoService.vol(volumexstring, Optional.empty()).map { it.code to it }.toMap()
                println("---------Y2-----------------")

            }
        }
        return Triple(rsilist, falldailylist, volumexlist)
    }


    fun wishlistsummary(username: String): ArrayNode {

        println("---------wishlistsummary------------$username-----")
        var codes = allfavs(username)
        println("---------all codes-----------------$codes-----")
//        var userconfig = user(username).userConfig
//        var rsistring = userconfig.get("rsi")?.get(0)?.asJsonObject?.get("value").toString().replace("\"", "")
//        var falldailystring = userconfig.get("falldaily")?.get(0)?.asJsonObject?.get("value").toString().replace("\"", "")
//        var volumexstring = userconfig.get("volumex")?.get(0)?.asJsonObject?.get("value").toString().replace("\"", "")
//        var rsistring = userconfig.get("rsi")?.get(0)?.asJsonObject?.get("value")?.asString
//        var falldailystring = userconfig.get("falldaily")?.get(0)?.asJsonObject?.get("value")?.asString
//        var volumexstring = userconfig.get("volumex")?.get(0)?.asJsonObject?.get("value")?.asString

        var list = mutableListOf<JsonNode>()
        var map = mutableMapOf<String, String>()

//        lateinit var rsilist: Map<String, techstr>
//        lateinit var falldailylist: Map<String, techstr>
//        lateinit var volumexlist: Map<String, techstr>
        println("---------X111111111111-----------------")
        // load all of user config  , for ris , falldaily , voulume , then match to favs
        val (rsilist, falldailylist, volumexlist) = indicatorMap(username)

        println("---------X222222222222222-----------------")

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
            println("---------XXX---code-----------$it---")

            var rootNode = mapper.createObjectNode()
            (rootNode as ObjectNode).put("code", it)
//            (rootNode as ObjectNode).put("message", message)
            (rootNode as ObjectNode).put("message", message(rsilist, falldailylist, volumexlist, it))

            println("---------ZzzZZZee---1--------------")
            var data = coreDataIgniteService.today(it)
            println("---------ZzzZZZee---1--------$data------")
            (rootNode as ObjectNode).put("price", data.changepercent)
            (rootNode as ObjectNode).put("volume", data.volume)
            (rootNode as ObjectNode).put("date", data.date.toString())
            println("---------ZzzZZZee---2--------------")
//            var a = JsonObject()
//            a.addProperty("code", it)
//            a.addProperty("date", data.date.toString())
//            (rootNode as ObjectNode).put("news", mapper.convertValue(newsIgniteService.getCode(a), JsonNode::class.java))
            (rootNode as ObjectNode).put("news", Json.toJson(newsIgniteService.newsToday(it)))


            arrayNode.add(rootNode)
        }

        return arrayNode
    }


    private fun message(rsilist: Map<String, techstr>, falldailylist: Map<String, techstr>, volumexlist: Map<String, techstr>, code: String): String {
        var message = rsilist.get(code)?.message ?: ""
        message = message + " " + (falldailylist.get(code)?.message ?: "")
        message = message + " " + (volumexlist.get(code)?.message ?: "")
        return message
    }


//    private fun newsToday(code: String): JsonNode {
//        var mapper = ObjectMapper()
//        var data = coreDataIgniteService.today(code)
//
//        var a = JsonObject()
//        a.addProperty("code", code)
//        a.addProperty("date", data.date.toString())
//        return mapper.convertValue(newsIgniteService.getCode(a), JsonNode::class.java)
//    }


    private fun getbydate(@PathVariable algo: String, sectorparam: Optional<String>): List<techstr> {
        return algoService.rsi(algo, sectorparam)
    }


    private fun allfavs(username: String): List<String> {
        var mutableList = mutableListOf<String>()

        wishlistRepo.findAll(QWishlist.wishlist.userid.eq(username)).forEach {
            mutableList.addAll(it.code.split(","))
        }
        return mutableList;

    }

    private fun user(username: String): User = userRepo.findOne(QUser.user.username.eq(username)).get()

    private fun ris(algo: String) {
        algoService.rsi(algo, Optional.empty())
    }


}
