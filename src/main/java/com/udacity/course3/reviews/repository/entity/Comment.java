package com.udacity.course3.reviews.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Document("comment")
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
