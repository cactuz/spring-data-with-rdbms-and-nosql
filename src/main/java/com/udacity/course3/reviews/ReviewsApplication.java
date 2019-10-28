package com.udacity.course3.reviews;

import com.mongodb.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories({"com.udacity.course3.reviews.repository.mongodb"})
@EnableJpaRepositories({"com.udacity.course3.reviews.repository.mysql"})
//@EnableJpaAuditing
public class ReviewsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReviewsApplication.class, args);
	}

    @Bean
    public MongoClient m() throws Exception {
        return new MongoClient("localhost");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(m(), "review");
    }

}