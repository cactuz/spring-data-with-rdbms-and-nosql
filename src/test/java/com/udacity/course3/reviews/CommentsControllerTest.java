package com.udacity.course3.reviews;

import com.udacity.course3.reviews.controller.CommentsController;
import com.udacity.course3.reviews.repository.*;
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
@WebMvcTest(CommentsController.class)  //must match controller being tested
public class CommentsControllerTest {

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    MockMvc mockMvc;
    private static final String COMMENTS_PATH = "/comments/reviews/1";
    private List<Comment> comments;
    private List<Review> reviews;

    @Before
    public void init() {
        comments = TestUtils.getComments();
        reviews = TestUtils.getReviews();
        Mockito.when(reviewRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(reviews.get(0)));
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(comments.get(0));
        Mockito.when(commentRepository.getCommentByReviewReviewId(Mockito.anyLong())).thenReturn(comments);
    }

    @Test
    public void addACommentTest() throws Exception{
        this.mockMvc.perform(post(COMMENTS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"comment\" : " + "\"" + comments.get(0).getComment() + "\"}")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value(comments.get(0).getComment()));
    }

    @Test
    public void retrieveCommentsForAReview() throws Exception{
        this.mockMvc.perform(get(COMMENTS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].commentId", is(1)))
                .andExpect(jsonPath("$.[1].comment", is(comments.get(1).getComment())));
    }
}

/*
[
  {
    "commentId": 1,
    "comment": "I agree with you 100%."
  },
  {
    "commentId": 2,
    "comment": "Thanks for the tip."
  }
]
 */
