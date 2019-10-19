package com.udacity.course3.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> getReviewsByProductProductId(Long productId);
}

