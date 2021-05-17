package com.project2.kitchentable.controllers;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.project2.kitchentable.beans.Recipe;
import com.project2.kitchentable.beans.User;
import com.project2.kitchentable.services.RecipeService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/recipe")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;
	@Autowired
	private AuthController authorize;
	private static Logger log = LogManager.getLogger(RecipeController.class);

	@PostMapping
	public Mono<ResponseEntity<Recipe>> addRecipe(ServerWebExchange exchange, @RequestBody Recipe r) {
		User user = authorize.userAuth(exchange);

		if (user != null && user.getUserType() == 3) {
			try {
				log.trace("Adding recipe");
				r.setRecipeId(Uuids.timeBased());
				return recipeService.addRecipe(r).map(recipe -> ResponseEntity.status(201).body(recipe))
						.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(r)));
			}catch(Exception e) {
				for (StackTraceElement st : e.getStackTrace())
					log.debug(st.toString());
			}
			
		}

		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Publisher<Recipe> getRecipes(ServerWebExchange exchange) {
		User u = authorize.userAuth(exchange);
		if (u != null) {
			return recipeService.getRecipes();
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@PutMapping("/{recipeID}")
	public Mono<Recipe> updateRecipe(ServerWebExchange exchange, @PathVariable("recipeID") String recipeID,
			@RequestBody Recipe r) {
		User user = authorize.userAuth(exchange);

		if (user != null && user.getUserType() == 3) {
			return recipeService.updateRecipe(r);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@DeleteMapping("/{recipeID}")
	public Mono<Void> removeRecipe(ServerWebExchange exchange, @PathVariable("recipeID") String recipeId) {
		User user = authorize.userAuth(exchange);
		if (user != null && user.getUserType() == 3) {
			try {
				return recipeService.removeRecipeById(UUID.fromString(recipeId));
			}catch(Exception e) {
				for (StackTraceElement st : e.getStackTrace())
					log.debug(st.toString());
			}
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@GetMapping
	public Publisher<Recipe> getRecipeByName(ServerWebExchange exchange,
			@RequestParam("name") String recipeName) {
		User u = authorize.userAuth(exchange);
		if (u != null) {
			return recipeService.getRecipeByName(recipeName);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@GetMapping(value = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Publisher<Recipe> getRecipeById(ServerWebExchange exchange, @PathVariable("recipeId") UUID recipeId) {
		User u = authorize.userAuth(exchange);
		if (u != null) {
			return recipeService.getRecipeById(recipeId);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}
}
