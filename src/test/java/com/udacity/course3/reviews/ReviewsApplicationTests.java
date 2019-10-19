package com.udacity.course3.reviews;

import com.udacity.course3.reviews.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  //drops and
// recreates H2 database upon each each execution
public class ReviewsApplicationTests {
	
	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate;
	private HttpHeaders headers;
	private Product savedSampleProduct;
	private HttpEntity<Product> productHttpEntity;
	private ResponseEntity<Product> sampleProductResponse;
	private Product testProduct;
	private Review testReview;
	private Review savedSampleReview;
	private HttpEntity<Review> reviewHttpEntity;
	private ResponseEntity<Review> sampleReviewResponse;
	private Comment testComment;
	private Comment savedSampleComment;
	private HttpEntity<Comment> commentHttpEntity;
	private ResponseEntity<Comment> sampleCommentResponse;

	@Before
	public void init() {
		headers = new HttpHeaders();
		restTemplate = new TestRestTemplate();
		headers.add("content-type", "application/json");

		testProduct = new Product();
		testProduct.setProductName(TestUtils.getProducts().get(0).getProductName());
		productHttpEntity = new HttpEntity<>(testProduct, headers);
		sampleProductResponse = restTemplate.postForEntity(
				getProductURL(Optional.empty()), productHttpEntity, Product.class);
		savedSampleProduct = sampleProductResponse.getBody();

		testReview = new Review();
		testReview.setReview(TestUtils.getReviews().get(0).getReview());
		reviewHttpEntity = new HttpEntity<>(testReview, headers);
		sampleReviewResponse = restTemplate.postForEntity(getReviewURL(savedSampleProduct.getProductId()), reviewHttpEntity, Review.class);
		savedSampleReview = sampleReviewResponse.getBody();

		testComment = new Comment();
		testComment.setComment(TestUtils.getComments().get(0).getComment());
		commentHttpEntity = new HttpEntity<>(testComment, headers);
		sampleCommentResponse = restTemplate.postForEntity(getCommentURL(savedSampleReview.getReviewId()),
		commentHttpEntity, Comment.class);
		savedSampleComment = sampleCommentResponse.getBody();
	}

	@Test
	public void createProductTest() {
		Assert.assertEquals(HttpStatus.CREATED, sampleProductResponse.getStatusCode());
		Assert.assertEquals(getProductURL(Optional.of(savedSampleProduct.getProductId())), sampleProductResponse.getHeaders().getLocation().toString());
		Assert.assertEquals(testProduct.getProductName(), savedSampleProduct.getProductName());
	}

	@Test
	public void retrieveAProductByIdTest() {
		ResponseEntity<Product> response = restTemplate.getForEntity(getProductURL(Optional.of(savedSampleProduct.getProductId())), Product.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(testProduct.getProductName(), savedSampleProduct.getProductName());
	}

	@Test
	public void retrieveListofProductsTest() {
		restTemplate.postForEntity(getProductURL(Optional.empty()), productHttpEntity, Product.class);
		ResponseEntity<List<Product>> response = restTemplate.exchange(getProductURL(Optional
				.empty()), HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertThat(response.getBody().size(),  is(greaterThanOrEqualTo(2)));
	}

	@Test
	public void checkThatReviewAndCommentIsPopulatedOnRetrieveProductTest() {
		restTemplate.postForEntity(getProductURL(Optional.empty()), productHttpEntity, Product.class);
		ResponseEntity<Product> response = restTemplate.exchange(getProductURL(Optional.of
				(savedSampleProduct.getProductId())), HttpMethod.GET, null, Product.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertThat(response.getBody().getReviews().size(),  is(greaterThanOrEqualTo(1)));
		Assert.assertThat(response.getBody().getReviews().get(0).getComments().size(),  is(greaterThanOrEqualTo(1)));
	}
	
	@Test
	public void addAReviewTest() {
		Assert.assertEquals(HttpStatus.CREATED, sampleReviewResponse.getStatusCode());
		Assert.assertEquals(getReviewURL(savedSampleProduct.getProductId()), sampleReviewResponse.getHeaders().getLocation().toString());
		Assert.assertEquals(testReview.getReview(), savedSampleReview.getReview());
	}

	@Test
	public void retrieveListOfReviewsForAProductTest() {
		Review nextReview = new Review();
		nextReview.setReview(TestUtils.getReviews().get(1).getReview());
		HttpEntity newReviewHttpEntity = new HttpEntity<>(nextReview, headers);
		ResponseEntity<Review> newReviewResponse = restTemplate.postForEntity(getReviewURL(savedSampleProduct.getProductId()),
				newReviewHttpEntity, Review.class);

		ResponseEntity<List<Review>> response = restTemplate.exchange(getReviewURL(savedSampleProduct.getProductId()), HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {});
		System.out.println("Response : " + response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(2, response.getBody().size());
		Assert.assertEquals(savedSampleReview.getReview(), response.getBody().get(0).getReview());
		Assert.assertEquals(newReviewResponse.getBody().getReview(), response.getBody().get(1).getReview());
	}

	@Test
	public void addACommentTest() {
		Assert.assertEquals(HttpStatus.CREATED, sampleCommentResponse.getStatusCode());
		Assert.assertEquals(getCommentURL(savedSampleReview.getReviewId()), sampleCommentResponse.getHeaders().getLocation().toString());
		Assert.assertEquals(testComment.getComment(), savedSampleComment.getComment());
	}

	@Test
	public void retrieveListOfCommentsForAReviewTest() {
		Comment nextComment = new Comment();
		nextComment.setComment(TestUtils.getComments().get(1).getComment());
		HttpEntity newCommentHttpEntity = new HttpEntity<>(nextComment, headers);
		ResponseEntity<Comment> newCommentResponse = restTemplate.postForEntity(getCommentURL(savedSampleReview.getReviewId()), newCommentHttpEntity, Comment.class);

		ResponseEntity<List<Comment>> response = restTemplate.exchange(getCommentURL(savedSampleReview.getReviewId()), HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {});
		System.out.println("Response : " + response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(2, response.getBody().size());
		Assert.assertEquals(savedSampleComment.getComment(), response.getBody().get(0).getComment());
		Assert.assertEquals(newCommentResponse.getBody().getComment(), response.getBody().get(1).getComment());
	}

	private String getProductURL(Optional<Long> productId) {
		String url = "http://localhost:" + port + "/products/";
		if (productId.isPresent()) {
			url += productId.get();
		}
		return url;
	}

	private String getReviewURL(Long productId) {
		String url = "http://localhost:" + port + "/reviews/products/" + productId;
		return url;
	}

	private String getCommentURL(Long reviewId) {
		String url = "http://localhost:" + port + "/comments/reviews/" + reviewId;
		return url;
	}

}