package com.udacity.course3.reviews.repository.mongodb;

import com.udacity.course3.reviews.repository.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMongoRepository extends MongoRepository<Comment, Long>{

}