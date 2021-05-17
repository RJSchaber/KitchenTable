package com.project2.kitchentable.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.project2.kitchentable.beans.Ingredient;
import com.project2.kitchentable.services.IngredientService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/ingredient")
public class IngredientController {

	private IngredientService ingredientService;

	private static Logger log = LogManager.getLogger(IngredientController.class);
	
	@Autowired
	public void setIngredientService(IngredientService ingredientService) {
		this.ingredientService = ingredientService;
	}

	@PostMapping
	public Mono<ResponseEntity<Ingredient>> addIngredient(@RequestBody Ingredient i) {
		log.trace("Adding a new Ingredient");
		i.setIngredientId(Uuids.timeBased());

		return ingredientService.addIngredient(i).map(ingredient -> ResponseEntity.status(201).body(ingredient))
				.onErrorStop();
	}
	
	@GetMapping
	public Publisher<Ingredient> getIngredients(ServerWebExchange exchange) {
		try {
			return ingredientService.getIngredients();
		}catch(Exception e) {
			for (StackTraceElement st : e.getStackTrace())
				log.debug(st.toString());
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}
	
}
