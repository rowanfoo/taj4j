package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.QCoreStock
import com.dhamma.pesistence.entity.repo.StockRepo
import com.dharma.algo.service.CategoryService
import com.fasterxml.jackson.databind.JsonNode
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

    @Autowired
    lateinit var categoryService: CategoryService


    @GetMapping("/category")
    fun getStocks(): Set<String> {
        var s = stockrepo.category().filter { it != null && it.trim().isNotEmpty() }.map { it.trim() }.toSet()
        return s
    }

    @GetMapping("/category/subcategory")
    fun subcategory(): List<String> {
        return stockrepo.subcategory()
    }

    @GetMapping("/category/tags")
    fun tags(): List<String> {
        return stockrepo.tags()
    }

    @GetMapping("/category/tag/{name}")
    fun getCats(@PathVariable name: String): List<String> {
        var queryFactory = JPAQueryFactory(entityManager);
        return queryFactory.selectDistinct(QCoreStock.coreStock.tags).from(QCoreStock.coreStock)
                .where(QCoreStock.coreStock.category.like("%$name%"))
                .fetch().filterNotNull()
    }


    @GetMapping("/category/stock/category/{category}/tag/{tag}")
    fun getTags(@PathVariable category: String, @PathVariable tag: String): List<String> {
        return stockrepo.findAll(QCoreStock.coreStock.tags.like("%$tag%").and(QCoreStock.coreStock.category.like("%$category%"))).map { it.code }.toList();
    }

    @GetMapping("/category/stocks/{mode}/{category}")
    fun getMetaData(@PathVariable category: String, @PathVariable mode: String): List<JsonNode> {
        println("----------getMetaData-------$category---------------$mode---------")
        var z: List<JsonNode> = if (mode == "category") {
            categoryService.category(category)
        } else if (mode == "subcategory") {
            categoryService.subcategory(category)
        } else {
            categoryService.tag(category)
        }
        return z
    }
}

