package com.udacity.course3.reviews.repository;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    //required, mappedBy looks for the field in the child entity (Review) that carries the matching
    // @ManyToOne
    @OneToMany(mappedBy="product", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
