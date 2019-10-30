package com.udacity.course3.reviews;

import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;
import com.udacity.course3.reviews.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ProductRdbmsRepositoryTest{

    @Autowired
    private ProductRdbmsRepository productRdbmsRepository;


    @Test
    public void saveAProductTest() {
        Assert.assertEquals(TestUtils.getProducts().get(0).getProductName(),
                productRdbmsRepository.save(TestUtils.getProducts().get(0)).getProductName());
    }

    @Test
    public void retrieveAllProductsTest() {
        productRdbmsRepository.save(TestUtils.getProducts().get(1));

        Assert.assertEquals(TestUtils.getProducts().get(1).getProductName(),
                productRdbmsRepository.findAll().get(0).getProductName());
    }
}
