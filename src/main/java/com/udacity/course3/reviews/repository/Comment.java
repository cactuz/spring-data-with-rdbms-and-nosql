package com.udacity.course3.reviews.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name="reviewId", nullable=false)
    @JsonIgnore
    private Review review;

    private String comment;
}
