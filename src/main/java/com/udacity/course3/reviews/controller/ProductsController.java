package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.mysql.ProductRdbmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with product entity.
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    ProductRdbmsRepository productRdbmsRepository;

    /**
     * Creates a product.  Inspect header on successful response.
     *
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRdbmsRepository.save(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getProductId())
                .toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    /**
     * Finds a product by id.
     *
     * @param id The id of the product.
     * @return The product if found, or a 404 not found.
     */
    @RequestMapping(value = "/{id}")
    public ResponseEntity<Optional<Product>> findById(@PathVariable("id") Long id) {
        if (productRdbmsRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(productRdbmsRepository.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Lists all products.
     *
     * @return The list of products.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Product> listProducts() { return productRdbmsRepository.findAll();
    }
}