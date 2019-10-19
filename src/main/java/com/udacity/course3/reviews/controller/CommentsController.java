package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReviewRepository reviewRepository;

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
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isPresent()) {
            comment.setReview(review.get());
            Comment savedComment = commentRepository.save(comment);

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
     * 2. Checks for existence of review.
     * 3. If review not found, returns NOT_FOUND.
     * 4. If found, returns list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> listCommentsForReview(@PathVariable("id") Long reviewId) {
        return new ResponseEntity<List<Comment>>(commentRepository.getCommentByReviewReviewId(reviewId), HttpStatus.OK);
    }
}