package com.dharma.algo.Controller

import com.dhamma.pesistence.entity.data.Comment
import com.dhamma.pesistence.entity.data.QComment
import com.dhamma.pesistence.entity.repo.CommentRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@CrossOrigin
@RestController
class CommentController {
    @Autowired
    lateinit var commentRepo: CommentRepo

    @GetMapping("/comment/{id}")
    fun id(@PathVariable id: String): Comment {
        return commentRepo.findOne(QComment.comment.id.eq(id.toLong())).get()
    }

    @GetMapping("/comment/all/{userid}")
    fun getAll(@PathVariable userid: String): List<Comment> {
        return commentRepo.findAll(QComment.comment.userid.eq(userid)).toList()
    }

    @GetMapping("/comment/idea/{userid}")
    fun getAllIdeasbyUserid(@PathVariable userid: String): List<Comment> {
        return commentRepo.findAll(QComment.comment.userid.eq(userid).and(QComment.comment.type.eq("idea"))).toList()
    }

    @GetMapping("/comment/userid/{userid}")
    fun getAllCommentbyUserid(@PathVariable userid: String): List<Comment> {
        return commentRepo.findAll(QComment.comment.userid.eq(userid).and(QComment.comment.type.ne("ideas"))).toList()
    }

    @GetMapping("/comment/userid/{userid}/type/{type}")
    fun getAllCommentbyType(@PathVariable userid: String, type: String): List<Comment> {
        return commentRepo.findAll(QComment.comment.userid.eq(userid).and(QComment.comment.type.eq(type))).toList()
    }

    //    @GetMapping("/comment/userid/{userid}/code/{code}")
//    fun getAllCommentbyCode(@PathVariable userid: String, @PathVariable code: String): List<Comment> {
//        println("-------------$userid-----------------$code---------------")
//        return commentRepo.findAll(QComment.comment.userid.eq(userid).and(QComment.comment.code.eq(code))).toList()
//    }
    @GetMapping("/comment/code/{code}")
    fun getAllCommentbyCode(@PathVariable code: String): List<Comment> {
        println("------------------------------$code---------------")
        return commentRepo.findAll(QComment.comment.code.eq(code)).toList()
    }


    @PutMapping("/comment")
    fun insert(@RequestBody comment: Comment) {
        println("----insert-------------$comment")
        commentRepo.save(comment)

    }
}



