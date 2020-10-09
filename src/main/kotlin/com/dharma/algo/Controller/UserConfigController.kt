package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.QUserConfig
import com.dhamma.pesistence.entity.data.UserConfig
import com.dhamma.pesistence.entity.repo.UserConfigRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
class UserConfigController {

    @Autowired
    lateinit var userConfigRepo: UserConfigRepo


    @GetMapping("/userconfig/{id}")
    fun getMetaData(@PathVariable id: String): Iterable<UserConfig> {
        return userConfigRepo.findAll(QUserConfig.userConfig.userid.eq(id.toLong()))

    }


}
