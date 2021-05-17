package com.project2.kitchentable.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.project2.kitchentable.beans.Kitchen;
import com.project2.kitchentable.services.KitchenService;
import com.project2.kitchentable.services.RecipeService;
import com.project2.kitchentable.services.ReviewService;
import com.project2.kitchentable.beans.Recipe;
import com.project2.kitchentable.beans.Reviews;
import com.project2.kitchentable.beans.User;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/kitchen")
public class KitchenController {
	
	@Autowired
	private AuthController authorize;
	private KitchenService kitchenService;
	private RecipeService recipeService;
	private ReviewService reviewService;

	@Autowired
	public void setKitchenService(KitchenService kitchenService) {
		this.kitchenService = kitchenService;
	}

	@Autowired
	public void setRecipeService(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@Autowired
	public void setReviewService(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping
	public Mono<ResponseEntity<Kitchen>> addKitchen(ServerWebExchange exchange, @RequestBody Kitchen k) {
		User user = authorize.userAuth(exchange);
		if(user == null) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return null;
		}		
		k.setId(user.getKitchenID());
		k.setHeadUser(user.getUserID());
		k.setFamilyID(user.getFamilyID());
		return kitchenService.addKitchen(k).map(kitchen -> ResponseEntity.status(201).body(kitchen))
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(k)));
	}

	@PutMapping
	public Mono<ResponseEntity<Kitchen>> addToList(ServerWebExchange exchange, @RequestParam(name = "list", required = false) String listname,
			@RequestParam(name = "ingredient", required = false) UUID iID,
			@RequestParam(name = "amount", required = false) Double amt) {
		User user = authorize.userAuth(exchange);
		if(user == null) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return null;
		}
		return kitchenService.getKitchenByID(user.getKitchenID()).flatMap(k -> kitchenService.addFood(listname, k, iID, amt).map(kitchen -> ResponseEntity.status(201).body(kitchen)).onErrorStop());

	}

	@DeleteMapping
	public Mono<ResponseEntity<Kitchen>> removeFood(ServerWebExchange exchange, @RequestParam(name = "list", required = false) String listname,
			@RequestParam(name = "ingredient", required = false) UUID iID,
			@RequestParam(name = "amount", required = false) Double amt) {
		User user = authorize.userAuth(exchange);
		if(user == null) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return null;
		}
		return kitchenService.getKitchenByID(user.getKitchenID()).flatMap(k -> kitchenService.removeFood(listname, k, iID, amt).map(kitchen -> ResponseEntity.status(201).body(kitchen))
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(k))));

	}

	@PostMapping(value = "/cook")
	public Mono<ResponseEntity<String>> cook(ServerWebExchange exchange, @RequestParam(name = "recipe", required = true) UUID recipe,
			@RequestParam(name = "review", required = false) String reviewBody,
			@RequestParam(name = "score", required = false) Double score) {
		
		User user = authorize.userAuth(exchange);
		if(user == null) {
			return Mono.just(ResponseEntity.badRequest().body("User is not logged in"));
		}
		
		return Mono.zip(kitchenService.getKitchenByID(user.getKitchenID()), recipeService.getRecipeById(recipe)).flatMap(data -> {
			Kitchen k = data.getT1();
			Recipe r = data.getT2();

			if((reviewBody != null) && (score != null)) {

				return kitchenService.cook(r, k).flatMap(kitchen -> { // Attempt to cook 
					Reviews rev = new Reviews(Uuids.timeBased(), user.getUserID(), recipe, score, reviewBody);
					return reviewService.addReview(rev).map(review -> ResponseEntity.status(201).body(kitchen.toString() + "\n" + review.toString())) // Proceed to try to add review
						.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.toString()))); // If error, assign error message to response
				}).onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.toString())));

			} else {
				return kitchenService.cook(r, k).map(kitchen -> ResponseEntity.status(201).body(kitchen.toString()))
						.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.toString())));
			}

		});

	}

	@PostMapping(value = "/buyFood")
	public Mono<ResponseEntity<Kitchen>> buyFood(ServerWebExchange exchange,
			@RequestParam(name = "ingredient", required = false) UUID iID,
			@RequestParam(name = "amount", required = false) Double amt) {		
		User user = authorize.userAuth(exchange);
		if(user == null) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return null;
		}

		return kitchenService.getKitchenByID(user.getKitchenID()).flatMap(k -> {
			if (k != null && k.getShoppingList() != null && k.getShoppingList().containsKey(iID)) {
				return kitchenService.removeFood("shopping", k, iID, amt).flatMap(tempK -> kitchenService.addFood("inventory", tempK, iID, amt)
							.map(kitchen -> ResponseEntity.status(201).body(kitchen))
							.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(k)))
				);
				
			}
			return Mono.just(ResponseEntity.badRequest().body(k));
		});
		

	}

}
