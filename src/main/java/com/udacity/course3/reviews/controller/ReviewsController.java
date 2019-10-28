package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;
import com.udacity.course3.reviews.repository.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    @Autowired
    com.udacity.course3.reviews.repository.mysql.ReviewRdbmsRepository reviewRdbmsRepository;
    @Autowired
    ProductRdbmsRepository productRdbmsRepository;
    @Autowired
    com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository reviewMongoRepository;
    @Autowired
    CommentsController commentsController;

    /**
     * Creates a review for a product.
     *
     * 1. Checks for existence of the product.
     * 2. If product not found, returns NOT_FOUND.
     * 3. If found, saves the review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/products/{id}", method = RequestMethod.POST)
    public ResponseEntity<Review> createReviewForProduct(@PathVariable("id") Long productId,
                                                    @RequestBody Review review) {

        if (productRdbmsRepository.existsById(productId)) {

            //This will provide for relationships to connect between ReviewMongo And Product
            //also need to create a new instance of product and just set the id. one cannot use the
            // product already stored from product repository productRdbmRepository because it
            // will create a circular reference resulting ins stackoverflow
            Product product = new Product();
            product.setProductId(productId);
            review.setProduct(product);

            //Save the review in two databases. Save the MySql first, which will generate then
            // auto-generate the Id and that same Id is used in the MongoDb insert
            Review mysqlReview = reviewRdbmsRepository.save(review);
            reviewMongoRepository.save(review);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .buildAndExpand(productId)
                    .toUri();
        return ResponseEntity.created(location).body(mysqlReview);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> listReviewsForProduct(@PathVariable("id") Long productId) {
        try {
            Review review = new Review();
            Product product = new Product();
            product.setProductId(productId);
            review.setProduct(product);

            Example<Review> example = Example.of(review);
            List<Review> reviews = reviewMongoRepository.findAll(example);

            reviews.forEach(r -> r.setComments(commentsController.listCommentsForReview(r.getReviewId()).getBody()));
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(reviewRdbmsRepository.getReviewsByProductProductId(productId), HttpStatus.OK);
        }
    }
}