package com.udacity.course3.reviews;

import com.udacity.course3.reviews.controller.ProductsController;
import com.udacity.course3.reviews.repository.Comment;
import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.mysql.CommentRdbmsRepository;
import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;

import com.udacity.course3.reviews.repository.mysql.ReviewRdbmsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductsController.class)  //must match controller being tested
@Import(value = {FakeMongo.class})
public class ProductsControllerUnitTest {

    @MockBean
    private ProductRdbmsRepository productRdbmsRepository;
    @MockBean
    private CommentRdbmsRepository commentRdbmsRepository;
    @MockBean
    private ReviewRdbmsRepository reviewRdbmsRepository;

    @Autowired
    MockMvc mockMvc;
    private static final String PRODUCTS_PATH = "/products/";
    private List<Product> products;

    @Before
    public void init() {
        products = TestUtils.getProducts();
        Mockito.when(productRdbmsRepository.save(Mockito.any())).thenReturn(products.get(0));
        Mockito.when(productRdbmsRepository.findById(Mockito.any())).thenReturn(Optional.of(products.get(0)));
        Mockito.when(productRdbmsRepository.findAll()).thenReturn(products);
    }

    @Test
    public void addAProductTest() throws Exception{
        this.mockMvc.perform(post(PRODUCTS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productName\" : " + "\"" + products.get(0).getProductName() + "\"}")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value(products.get(0).getProductName()));
    }

    @Test
    public void retrieveListOfProductsTest() throws Exception{
        this.mockMvc.perform(get(PRODUCTS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].productName", hasItems(products.get(0).getProductName(), products.get(1).getProductName())));
    }

    @Test
    public void retrieveAProductTest() throws Exception{
        this.mockMvc.perform(get(PRODUCTS_PATH + products.get(0).getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(products.get(0).getProductId()));
    }
}
