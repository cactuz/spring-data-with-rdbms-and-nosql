package com.udacity.course3.reviews.repository.mongodb;

import com.udacity.course3.reviews.repository.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is the mongodb repository for the entity Review.
 * Springboot can only create a single bean of the same name and so name of this class should be
 * different from the RDBMS repository or the @Autowired on the mongo instance will not detect
 * it.
 */
@Repository
public interface ReviewMongoRepository extends MongoRepository<Review, Long>{
}
