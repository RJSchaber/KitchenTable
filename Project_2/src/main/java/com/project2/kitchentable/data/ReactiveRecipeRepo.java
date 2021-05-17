package com.project2.kitchentable.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.project2.kitchentable.beans.Recipe;

import reactor.core.publisher.Mono;

@Repository
public interface ReactiveRecipeRepo extends ReactiveCassandraRepository<Recipe, String>{
	@AllowFiltering
	Mono<Recipe> findByRecipeId(UUID recipeId);
	@AllowFiltering
	Mono<Recipe> findByRecipeName(String recipeName);
	@AllowFiltering
	Mono<Void> deleteByRecipeId(UUID recipeId);
}
