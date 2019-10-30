package com.udacity.course3.reviews;

import com.udacity.course3.reviews.repository.entity.Comment;
import com.udacity.course3.reviews.repository.entity.Product;
import com.udacity.course3.reviews.repository.entity.Review;
import com.udacity.course3.reviews.repository.mongodb.CommentMongoRepository;
import com.udacity.course3.reviews.repository.mysql.CommentRdbmsRepository;
import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;
import com.udacity.course3.reviews.repository.mysql.ReviewRdbmsRepository;
import com.udacity.course3.reviews.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class CommentMongoRepositoryTest {

    @Autowired
    private CommentMongoRepository commentMongoRepository;
    @MockBean
    private ProductRdbmsRepository productRdbmsRepository;
    @MockBean
    private CommentRdbmsRepository commentRdbmsRepository;
    @MockBean
    private ReviewRdbmsRepository reviewRdbmsRepository;

    @org.junit.Test
    public void saveACommentTest() {
        Assert.assertEquals(TestUtils.getComments().get(0).getCommentId(),
                commentMongoRepository.save(TestUtils.getComments().get(0)).getCommentId());
    }

    @Test
    public void retrieveAReviewTest() {
        commentMongoRepository.save(TestUtils.getComments().get(0));

        List<Comment> comments;
        Comment comment = new Comment();
        Review review = new Review();
        review.setReviewId(1L);
        comment.setReview(review);
        Example<Comment> example = Example.of(comment);
        comments = commentMongoRepository.findAll(example);

        Assert.assertEquals(comments.get(0).getCommentId(),
                commentMongoRepository.save(TestUtils.getComments().get(0)).getCommentId());
    }
}
