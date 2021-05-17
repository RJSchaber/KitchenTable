package com.project2.kitchentable.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import com.project2.kitchentable.beans.Reviews;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveReviewRepo extends ReactiveCassandraRepository<Reviews, String>{
	@AllowFiltering
	Flux<Reviews> findAllByRecipeId(UUID recipeId);
	
	@AllowFiltering
	Flux<Reviews> findAllByUserId(UUID userId);
	
	@AllowFiltering
	Mono<Reviews> findByReviewId(UUID reviewId);

}
