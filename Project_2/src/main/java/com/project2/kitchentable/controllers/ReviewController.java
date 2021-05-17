package com.project2.kitchentable.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project2.kitchentable.services.ReviewService;
import com.project2.kitchentable.beans.Reviews;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

	private ReviewService reviewService;
	
	@Autowired
	public void setReviewService(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	@GetMapping(value = "/{recipe}", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Mono<ResponseEntity<List<Reviews>>> viewReviewsByRecipe(@PathVariable("recipe") UUID recipe) {
		return reviewService.getReviewsByRecipeId(recipe).collectList().map(reviews -> ResponseEntity.status(200).body(reviews));
	}
	
	@GetMapping(value = "/user/{user}", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Mono<ResponseEntity<List<Reviews>>> viewReviewsByUser(@PathVariable("user") UUID user) {
		return reviewService.getReviewsByUserId(user).collectList().map(reviews -> ResponseEntity.status(200).body(reviews));
	}
	
	@GetMapping(value = "/review/{review}", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Mono<ResponseEntity<Reviews>> viewReview(@PathVariable("review") UUID review) {
		return reviewService.getReviewById(review).map(reviews -> ResponseEntity.status(200).body(reviews));
	}

}
