package com.udacity.course3.reviews;

import com.udacity.course3.reviews.controller.ReviewsController;
import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.Review;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewsController.class) //must match controller being tested
public class ReviewsControllerTest {

    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    MockMvc mockMvc;
    private static final String REVIEWS_PATH = "/reviews/products/1";
    private List<Product> products;
    private List<Review> reviews;

    @Before
    public void init() {
        products = TestUtils.getProducts();
        reviews = TestUtils.getReviews();
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(products.get(0)));
        Mockito.when(reviewRepository.save(Mockito.any())).thenReturn(reviews.get(0));
        Mockito.when(reviewRepository.getReviewsByProductProductId(Mockito.anyLong())).thenReturn(reviews);
    }

    @Test
    public void addAReviewTest() throws Exception{
        this.mockMvc.perform(post(REVIEWS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"review\" : " + "\"" + reviews.get(0).getReview() + "\"}")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.review").value(reviews.get(0).getReview()));
    }

    @Test
    public void retrieveReviewForAProductTest() throws Exception{
        this.mockMvc.perform(get(REVIEWS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].reviewId", is(1)))
                .andExpect(jsonPath("$.[1].review", is(reviews.get(1).getReview())))
                .andExpect(jsonPath("$.[1].comments", is(reviews.get(1).getComments())));
    }
}

