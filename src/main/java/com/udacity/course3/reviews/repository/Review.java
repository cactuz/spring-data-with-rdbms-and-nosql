package com.udacity.course3.reviews.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne //required, matches the @OneToMany in the Product entity
    @JoinColumn(name = "productId", nullable = false)
    @JsonIgnore
    private Product product;

    private String review;

    @OneToMany(mappedBy="review", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
