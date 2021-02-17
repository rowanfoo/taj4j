package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.Comment
import com.dhamma.pesistence.entity.data.QComment
import com.dhamma.pesistence.entity.data.type.DurationType
import com.dhamma.pesistence.entity.repo.CommentRepo
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

// tracker is really comment but it is type of duration = SHORT
@CrossOrigin
@RestController
class TrackerController {
    @Autowired
    lateinit var commentRepo: CommentRepo

    @PersistenceContext
    lateinit var em: EntityManager

    @GetMapping("/tracker/{userid}")
    fun id(@PathVariable userid: String): Iterable<Comment> {
        val query = JPAQueryFactory(em)
        return query.from(QComment.comment)
            .where(QComment.comment.period.eq(DurationType.SHORT).and(QComment.comment.userid.eq(userid)))
            .orderBy(QComment.comment.date.desc()).fetch() as List<Comment>
    }

    @GetMapping("/tracker/code/{code}/{userid}")
    fun code(@PathVariable userid: String, @PathVariable code: String): Iterable<Comment> {
        println("====TrackerController==code=========${userid}=================${code}===")

        var s = commentRepo.findAll(
            QComment.comment.userid.eq(userid).and(QComment.comment.code.eq(code)).and(
                QComment.comment.period.eq(DurationType.SHORT)
            )
        ).toList()
        println(s)
        return s
    }

    @DeleteMapping("/tracker/code/{code}/{userid}")
    fun removecode(@PathVariable userid: String, @PathVariable code: String) {

        println("---------------removecode---------${code}---------------")
        var s = commentRepo.findAll(
            QComment.comment.userid.eq(userid).and(QComment.comment.code.eq(code)).and(
                QComment.comment.period.eq(DurationType.SHORT)
            )
        ).toList()

        s.forEach {
            it.isReject = true
        }
        commentRepo.saveAll(s)
        commentRepo.flush()
    }
}



