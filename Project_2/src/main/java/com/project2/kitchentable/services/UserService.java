package com.project2.kitchentable.services;

import java.util.List;
import java.util.UUID;

import com.project2.kitchentable.beans.Recipe;
import com.project2.kitchentable.beans.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

	Mono<User> getUsersByName(String fname, String lname);

	Mono<User> addUser(User u);

	Flux<User> getUsers();

	Mono<User> updateUser(User user);

	Mono<User> getUserByID(UUID userID);

	Mono<List<Recipe>> getFavorites(UUID userid);

	Mono<User> addToFavorites(UUID userid, UUID recipeid);
	
	Mono<User> removeFromFavorites(UUID userId, UUID recipeId);

	Mono<Void> removeUser(UUID id);

	Mono<User> setKitchenNull(User u);
}