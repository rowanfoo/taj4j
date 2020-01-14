package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.QCoreStock
import com.dhamma.pesistence.entity.repo.StockRepo
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@CrossOrigin
@RestController
class CategorysController {
    @Autowired
    lateinit var stockrepo: StockRepo


    @PersistenceContext
    lateinit var entityManager: EntityManager


    @GetMapping("/category")
    fun getStocks(): List<String> {
        var queryFactory = JPAQueryFactory(entityManager);
        return queryFactory.selectDistinct(QCoreStock.coreStock.category).from(QCoreStock.coreStock).fetch().filterNotNull()
    }

    @GetMapping("/tag/category/{name}")
    fun getCats(@PathVariable name: String): List<String> {
        var queryFactory = JPAQueryFactory(entityManager);
        return queryFactory.selectDistinct(QCoreStock.coreStock.tags).from(QCoreStock.coreStock)
                .where(QCoreStock.coreStock.category.like("%$name%"))
                .fetch().filterNotNull()
    }

}