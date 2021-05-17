package com.project2.kitchentable.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project2.kitchentable.beans.Recipe;
import com.project2.kitchentable.beans.User;
import com.project2.kitchentable.data.ReactiveRecipeRepo;
import com.project2.kitchentable.data.ReactiveUserRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	private static Logger log = LogManager.getLogger(UserServiceImpl.class);
	@Autowired
	private ReactiveUserRepo userRepo;
	@Autowired
	private ReactiveRecipeRepo recipeRepo;

	@Override
	public Mono<User> getUsersByName(String lname, String fname) {
		return userRepo.findByLastnameAndFirstname(lname, fname);
	}

	@Override
	public Mono<User> addUser(User u) {
		log.trace("Attempting to add user: " + u.toString());
		return userRepo.insert(u);
	}

	@Override
	public Mono<User> updateUser(User u) {
		log.trace("Attempting to update user: " + u.toString());
		return userRepo.save(u);
	}

	@Override
	public Flux<User> getUsers() {
		log.trace("Attempting to find all users: ");
		return userRepo.findAll();
	}

	@Override
	public Mono<Void> removeUser(UUID id) {
		return userRepo.deleteByUserID(id);
	}

	@Override
	public Mono<User> getUserByID(UUID userID) {
		log.trace("Attempting to locate the user with uuid: " + userID);
		return userRepo.findByUserID(userID);
	}

	@Override
	public Mono<List<Recipe>> getFavorites(UUID userId) {
		log.debug("List of favorites incoming...");
		// Get user for the list owner
		return userRepo.findByUserID(userId).flatMap(user -> {
			// Return a flux of recipes as collected mono<list>
			if (user.getFavorites() == null) {
				log.debug("Favorites list was null, returning default list...");
				List<Recipe> defaultList = new ArrayList<>();
				return Mono.just(defaultList);
			} else {
				log.debug("Favorites list was valid, bringing it back...");
				return Flux.fromIterable(user.getFavorites()).flatMap(recipeId -> {
					// Transform list of favorite ids to the actual associated recipes
					return recipeRepo.findByRecipeId(recipeId);
				}).collectList();
			}
		});
	}

	@Override
	public Mono<User> addToFavorites(UUID userId, UUID recipeId) {
		// Retrieve user from database
		log.debug("Attempting to update user's favorites list...");
		return userRepo.findByUserID(userId).flatMap(user -> {
			// Retrieve user's list of favorites
			if (user.getFavorites() == null) {
				log.debug("List was null, creating new list for user...");
				user.setFavorites(new ArrayList<>());
			}
			log.debug("List is valid, adding recipe to user's favorite list...");
			user.getFavorites().add(recipeId);
			return userRepo.save(user);
		});
	}

	@Override
	public Mono<User> removeFromFavorites(UUID userId, UUID recipeId) {
		// Retrieve user from database
		log.debug("Attempting to update user's favorites list...");
		return userRepo.findByUserID(userId).flatMap(user -> {
			// Retrieve user's list of favorites
			if (user.getFavorites() == null) {
				log.debug("List was null, nothing to remove.");
				return userRepo.save(user);
			}
			log.debug("List is valid, removing recipe from user's favorite list...");
			user.getFavorites().remove(recipeId);
			return userRepo.save(user);
		});
	}

	public Mono<User> setKitchenNull(User u) {
		u.setKitchenID(null);
		return userRepo.save(u);
	}
}