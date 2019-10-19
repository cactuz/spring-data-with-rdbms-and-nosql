package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.Review;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ProductRepository productRepository;

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
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            review.setProduct(product.get());  //this will provide for relationships to connect between Review And Product
            Review savedReview = reviewRepository.save(review);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .buildAndExpand(productId)
                    .toUri();

            return ResponseEntity.created(location).body(savedReview);
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
        return new ResponseEntity<>(reviewRepository.getReviewsByProductProductId(productId), HttpStatus.OK);
    }
}