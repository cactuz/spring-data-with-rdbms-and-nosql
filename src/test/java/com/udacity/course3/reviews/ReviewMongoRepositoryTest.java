package com.udacity.course3.reviews;

import com.udacity.course3.reviews.repository.entity.Product;
import com.udacity.course3.reviews.repository.entity.Review;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
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
public class ReviewMongoRepositoryTest {

    @Autowired
    private ReviewMongoRepository reviewMongoRepository;
    @MockBean
    private CommentRdbmsRepository commentRdbmsRepository;
    @MockBean
    private ProductRdbmsRepository productRdbmsRepository;
    @MockBean
    private ReviewRdbmsRepository reviewRdbmsRepository;

    @Test
    public void saveAReviewTest() {
        Assert.assertEquals(TestUtils.getReviews().get(0).getReviewId(),
                reviewMongoRepository.save(TestUtils.getReviews().get(0)).getReviewId());
    }

    @Test
    public void retrieveAReviewTest() {
        reviewMongoRepository.save(TestUtils.getReviews().get(1));

        Review review = new Review();
        Product product = new Product();
        product.setProductId(1L);
        review.setProduct(product);
        Example<Review> example = Example.of(review);
        List<Review> reviews = reviewMongoRepository.findAll(example);

        Assert.assertEquals(reviews.get(0).getReviewId(),
                reviewMongoRepository.save(TestUtils.getReviews().get(0)).getReviewId());
    }
}
