package com.udacity.course3.reviews;

import com.mongodb.MongoClient;
import com.udacity.course3.reviews.repository.entity.Comment;
import com.udacity.course3.reviews.repository.entity.Product;
import com.udacity.course3.reviews.repository.entity.Review;
import com.udacity.course3.reviews.util.TestUtils;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

/**
 * The test embeds H2 and Mongo.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=ReviewsApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  //drops and
// recreates H2 database upon each each execution
@TestPropertySource("classpath:applicationBkup.properties")
public class ReviewsApplicationTest {
	
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
	private MongodExecutable mongodExecutable;
	private MongoTemplate mongoTemplate;

	@Before
	public void setup() throws Exception {
		String ip = "localhost";
		int mongoPort = 27017;

		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(ip, mongoPort, Network.localhostIsIPv6()))
				.build();

		MongodStarter starter = MongodStarter.getDefaultInstance();
		mongodExecutable = starter.prepare(mongodConfig);
//		mongodExecutable.start();
		mongoTemplate = new MongoTemplate(new MongoClient(ip, mongoPort), "review");

		headers = new HttpHeaders();
		restTemplate = new TestRestTemplate();
		headers.add("content-type", "application/json");

		testProduct = new Product();
		testProduct.setProductName(TestUtils.getProducts().get(0).getProductName());
		productHttpEntity = new HttpEntity<>(testProduct, headers);
		sampleProductResponse = restTemplate.postForEntity(getProductURL(Optional.empty()), productHttpEntity, Product.class);
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

	@After
	public void clean() {
		mongodExecutable.stop();
		System.out.println("Execute clean()");
	}

	@Test
	public void createProductTest() {
		System.out.println("Execute test()");

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
		Assert.assertEquals(true, response.getBody().size() > 0);
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
		Assert.assertEquals(true, response.getBody().size() > 0);
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