package com.udacity.course3.reviews;

import com.udacity.course3.reviews.controller.CommentsController;
import com.udacity.course3.reviews.controller.ReviewsController;
import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.mysql.CommentRdbmsRepository;
import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;
import com.udacity.course3.reviews.repository.Review;
import com.udacity.course3.reviews.repository.mysql.ReviewRdbmsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewsController.class) //must match controller being tested
@Import(value = {FakeMongo.class})
public class ReviewsControllerUnitTest {

    @MockBean
    private ReviewRdbmsRepository reviewRdbmsRepository;
    @MockBean
    private CommentRdbmsRepository commentRdbmsRepository;
    @MockBean
    private ProductRdbmsRepository productRdbmsRepository;
    @MockBean
    private ReviewMongoRepository reviewMongoRepository;
    @MockBean
    private CommentsController commentsController;

    @Autowired
    MockMvc mockMvc;
    private static final String REVIEWS_PATH = "/reviews/products/1";
    private List<Product> products;
    private List<Review> reviews;

    @Before
    public void init() {
        products = TestUtils.getProducts();
        reviews = TestUtils.getReviews();
        Mockito.when(productRdbmsRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(reviewRdbmsRepository.save(Mockito.any())).thenReturn(reviews.get(0));
        Mockito.when(reviewMongoRepository.save(Mockito.any())).thenReturn(reviews.get(0));
        Mockito.when(reviewRdbmsRepository.getReviewsByProductProductId(Mockito.anyLong())).thenReturn(reviews);

        Mockito.when(reviewMongoRepository.findAll(Mockito.any(Example.class))).thenReturn(reviews);
        Mockito.when(commentsController.listCommentsForReview(Mockito.eq(1L))).thenReturn
                (new ResponseEntity<>(TestUtils.getComments(), HttpStatus.OK));
        Mockito.when(commentsController.listCommentsForReview(Mockito.eq(2L))).thenReturn
                (new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));
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
    public void retrieveReviewForAProductFromMongoTest() throws Exception{
        Mockito.when(productRdbmsRepository.existsById(Mockito.anyLong())).thenReturn(false);

        this.mockMvc.perform(get(REVIEWS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].reviewId", is(1)))
                .andExpect(jsonPath("$.[1].review", is(reviews.get(1).getReview())))
                .andExpect(jsonPath("$.[1].comments", is(reviews.get(1).getComments())));
    }

    @Test
    public void retrieveReviewForAProductFromRdbmsTest() throws Exception{
        Mockito.when(reviewMongoRepository.findAll(Mockito.any(Example.class))).thenThrow(new RuntimeException());

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

