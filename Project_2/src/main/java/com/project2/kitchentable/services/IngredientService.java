package com.project2.kitchentable.services;

import java.util.UUID;

import com.project2.kitchentable.beans.Ingredient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IngredientService {

	Mono<Ingredient> addIngredient(Ingredient i);

	Flux<Ingredient> getIngredients();

	Mono<Ingredient> updateIngredient(Ingredient i);

	Mono<Ingredient> getIngredientById(UUID id);

	Mono<Void> removeIngredient(Ingredient i);

}

