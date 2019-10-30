package com.udacity.course3.reviews.repository.mysql;

import com.udacity.course3.reviews.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRdbmsRepository extends JpaRepository<Product, Long> {
}
