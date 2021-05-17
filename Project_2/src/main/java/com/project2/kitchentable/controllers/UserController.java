package com.project2.kitchentable.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.project2.kitchentable.beans.Recipe;
import com.project2.kitchentable.beans.User;
import com.project2.kitchentable.services.UserService;
import com.project2.kitchentable.utils.JWTParser;

import reactor.core.publisher.Mono;

@RestController
public class UserController {
	private UserService userService;
	private JWTParser tokenService;
	@Autowired
	private AuthController authorize;

	private static Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setTokenServicer(JWTParser parser) {
		this.tokenService = parser;
	}

	@GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
	public Publisher<User> getUsers(ServerWebExchange exchange) {
		User u = authorize.userAuth(exchange);
		if (u != null && u.getUserType() == 3) {
			return userService.getUsers();
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@PostMapping("users")
	public Mono<ResponseEntity<User>> registerUser(@RequestBody User u) {
		u.setUserID(Uuids.timeBased());
		if (u.getFamilyID() == null) {
			u.setFamilyID(Uuids.timeBased());
		}
		if (u.getKitchenID() == null) {
			u.setKitchenID(Uuids.timeBased());
		}
		if (u.getFavorites() == null) {
			List<UUID> favorites = new ArrayList<>();
			u.setFavorites(favorites);
		}
		if (u.getCookedRecipes() == null) {
			List<UUID> cooked = new ArrayList<>();
			u.setCookedRecipes(cooked);
		}
		return userService.addUser(u).map(user -> ResponseEntity.status(201).body(user))
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(u)));
	}

	@PostMapping(value = "login", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Publisher<User> login(ServerWebExchange exchange, @RequestParam(name = "userid") UUID userid) {
		return userService.getUserByID(userid).doOnNext(user -> {
			try {
				exchange.getResponse()
						.addCookie(ResponseCookie.from("token", tokenService.makeToken(user)).httpOnly(true).build());
			} catch (Exception e) {
				exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		});
	}

	@DeleteMapping("login")
	public ResponseEntity<Void> logout(ServerWebExchange exchange) {
		try {
			exchange.getResponse().addCookie(ResponseCookie.from("token", "").httpOnly(true).build());
		} catch (Exception e) {
			exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.noContent().build();
	}

	@PutMapping("users/{userID}")
	public Mono<User> updateUser(ServerWebExchange exchange, @PathVariable("userID") String userID,
			@RequestBody User u) {
		User user = authorize.userAuth(exchange);

		if (user != null && user.getUserType() == 3) {
			return userService.updateUser(u);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@PutMapping("users/{userID}/{kitchenID}")
	public Mono<User> updateUserKitchen(ServerWebExchange exchange, @PathVariable("userID") String userID,
			@PathVariable("kitchenID") String kitchenID, @RequestBody User u) {
		User user = authorize.userAuth(exchange);

		if (user != null && u.getUserType() == 2 && user.getKitchenID() == UUID.fromString(kitchenID)) {
			u.setKitchenID(UUID.fromString(kitchenID));
			return userService.updateUser(u);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@DeleteMapping("users/{userID}/{kitchenID}")
	public Mono<User> removeUserKitchen(ServerWebExchange exchange, @PathVariable("kitchenID") String kitchenID,
			@RequestBody User u) {
		User user = authorize.userAuth(exchange);

		if (user != null && user.getUserType() == 2 && user.getKitchenID() == UUID.fromString(kitchenID)) {
			return userService.setKitchenNull(u);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@DeleteMapping("users/{userID}")
	public Mono<Void> removeUser(ServerWebExchange exchange, @PathVariable("userID") String userID) {
		User user = authorize.userAuth(exchange);
		if (user != null && user.getUserType() == 3) {
			try {
				return userService.removeUser(UUID.fromString(userID));
			} catch (Exception e) {
				for (StackTraceElement st : e.getStackTrace())
					log.debug(st.toString());
			}
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}

	@GetMapping("users/favorites")
	public Mono<List<Recipe>> getFavorites(ServerWebExchange exchange, @RequestParam(name = "userid") UUID userId) {
		User user = authorize.userAuth(exchange);
		if ((user != null && user.getUserID().equals(userId)) || (user != null && user.getUserType() == 3))
			try {
				log.debug("Gathering list of favorites..");
				return userService.getFavorites(userId);
			} catch (Exception e) {
				for (StackTraceElement st : e.getStackTrace())
					log.debug(st.toString());
			}
		log.debug("Invalid user access, only same user and/or admin may retrieve this list.");
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return null;
	}

	@PutMapping("users/favorites/update")
	public Mono<User> addToFavorites(ServerWebExchange exchange, @RequestParam(name = "userId") UUID userId,
			@RequestParam(name = "recipeId") UUID recipeId) {
		User user = authorize.userAuth(exchange);
		if ((user != null && user.getUserID().equals(userId)) || (user != null && user.getUserType() == 3))
			try {
				log.debug("Updating list of favorites for user id: %s", userId);
				return userService.addToFavorites(userId, recipeId);
			} catch (Exception e) {
				for (StackTraceElement st : e.getStackTrace())
					log.debug(st.toString());
			}
		log.debug("Invalid user access, only same user and/or admin may update this list.");
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return null;
	}

	@DeleteMapping("users/favorites/update")
	public Mono<User> removeFromFavorites(ServerWebExchange exchange, @RequestParam(name = "userId") UUID userId,
			@RequestParam(name = "recipeId") UUID recipeId) {
		User user = authorize.userAuth(exchange);
		if ((user != null && user.getUserID().equals(userId)) || (user != null && user.getUserType() == 3))
			try {
				log.debug("Updating list of favorites for user id: %s", userId);
				return userService.removeFromFavorites(userId, recipeId);
			} catch (Exception e) {
				for (StackTraceElement st : e.getStackTrace())
					log.debug(st.toString());
			}
		log.debug("Invalid user access, only same user and/or admin may update this list.");
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return null;
	}

}
