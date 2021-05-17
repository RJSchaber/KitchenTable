package com.project2.kitchentable.services;

import java.util.UUID;

import com.project2.kitchentable.beans.Reviews;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

	Mono<Reviews> addReview(Reviews r);

	Flux<Reviews> getReviews();

	Mono<Reviews> updateReview(Reviews r);

	Mono<Reviews> getReviewById(UUID id);

	Mono<Void> removeReview(Reviews r);

	Flux<Reviews> getReviewsByRecipeId(UUID id);

	Flux<Reviews> getReviewsByUserId(UUID id);

}