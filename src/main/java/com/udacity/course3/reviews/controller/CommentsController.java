package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.repository.entity.Comment;
import com.udacity.course3.reviews.repository.entity.Review;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.mysql.ReviewRdbmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    com.udacity.course3.reviews.repository.mysql.CommentRdbmsRepository commentRdbmsRepository;
    @Autowired
    ReviewRdbmsRepository reviewRdbmsRepository;
    @Autowired
    ReviewMongoRepository reviewMongoRepository;
    @Autowired
    com.udacity.course3.reviews.repository.mongodb.CommentMongoRepository commentMongoRepository;

    /**
     * Creates a comment for a review.
     *
     * 1. Checks for existence of review.
     * 2. If review not found, returns NOT_FOUND.
     * 3. If found, saves comment.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.POST)
    public ResponseEntity<Comment> createCommentForReview(@PathVariable("id") Long reviewId,
                                                          @RequestBody Comment comment) {
        if (reviewRdbmsRepository.existsById(reviewId)) {
            Review review = new Review();
            review.setReviewId(reviewId);
            comment.setReview(review);
            Comment savedComment = commentRdbmsRepository.save(comment);
            commentMongoRepository.save(comment);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .buildAndExpand(reviewId)
                    .toUri();
            return ResponseEntity.created(location).body(savedComment);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * List comments for a review.
     *
     * 2. Checks for existence of review from the .
     * 3. If review not found, returns NOT_FOUND.
     * 4. If found, returns list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> listCommentsForReview(@PathVariable("id") Long reviewId) {
        try {
            List<Comment> comments;
            Comment comment = new Comment();
            Review review = new Review();
            review.setReviewId(reviewId);
            comment.setReview(review);
            Example<Comment> example = Example.of(comment);
            comments = commentMongoRepository.findAll(example);

            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(commentRdbmsRepository.getCommentByReviewReviewId(reviewId), HttpStatus.OK);
        }
    }
}