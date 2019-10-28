package com.udacity.course3.reviews.repository.mysql;

import com.udacity.course3.reviews.repository.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRdbmsRepository extends JpaRepository<Comment, Long>{
    List<Comment> getCommentByReviewReviewId(Long reviewId);
}
