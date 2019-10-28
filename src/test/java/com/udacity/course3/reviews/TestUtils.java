package com.udacity.course3.reviews;

import com.udacity.course3.reviews.repository.Comment;
import com.udacity.course3.reviews.repository.Product;
import com.udacity.course3.reviews.repository.Review;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Super Awesome Shirt", new ArrayList<>()));
        products.add(new Product(2L, "Cool Sneakers", new ArrayList<>()));

        return products;
    }

    public static List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();

        reviews.add(new Review(1L, getProducts().get(0), "Great quality. Very stylish.",
                new ArrayList<>()));
        reviews.add(new Review(2L, getProducts().get(0), "Highly recommended.",
                new ArrayList<>()));

        return reviews;
    }

    public static List<Comment> getComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, getReviews().get(0), "I agree with you 100%."));
        comments.add(new Comment(1L, getReviews().get(0), "Thanks for the tip."));

        return comments;
    }
}
