package com.udacity.course3.reviews.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    //required, mappedBy looks for the field in the child entity (ReviewMongo) that carries the matching
    @OneToMany(mappedBy="product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;
}
