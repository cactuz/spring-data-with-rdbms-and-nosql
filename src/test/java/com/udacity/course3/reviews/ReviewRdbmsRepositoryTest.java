package com.udacity.course3.reviews;

import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;
import com.udacity.course3.reviews.repository.mysql.ReviewRdbmsRepository;
import com.udacity.course3.reviews.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewRdbmsRepositoryTest {
    @Autowired
    private ReviewRdbmsRepository reviewRdbmsRepository;

    @Autowired
    private ProductRdbmsRepository productRdbmsRepository;

    @Test
    public void saveAReviewTest() {
        productRdbmsRepository.save(TestUtils.getProducts().get(0));

        Assert.assertEquals(TestUtils.getReviews().get(0).getReview(),
                reviewRdbmsRepository.save(TestUtils.getReviews().get(0)).getReview());
    }
}
