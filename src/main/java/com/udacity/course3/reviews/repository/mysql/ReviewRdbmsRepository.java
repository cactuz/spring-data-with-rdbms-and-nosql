package com.udacity.course3.reviews.repository.mysql;

import com.udacity.course3.reviews.repository.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRdbmsRepository extends JpaRepository<Review, Long>{
    List<Review> getReviewsByProductProductId(Long productId);
}

