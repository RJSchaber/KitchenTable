package com.project2.kitchentable.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.project2.kitchentable.beans.Requests;
import com.project2.kitchentable.services.RecipeService;
import com.project2.kitchentable.services.RequestService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

	private RequestService requestService;
	private RecipeService recipeService;

	@Autowired
	public void setRequestService(RequestService requestService) {
		this.requestService = requestService;
	}

	@Autowired
	public void setRecipeService(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@PostMapping
	public Mono<ResponseEntity<Requests>> addRequest(@RequestBody Requests q) {
		q.setRequestId(Uuids.timeBased());
		if (q.getRecipeId() == (null)) {
			q.setRecipeId(Uuids.timeBased());
		}
		return requestService.addRequest(q).map(request -> ResponseEntity.status(201).body(request))
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(q)));
	}

	@PutMapping
	public Mono<ResponseEntity<Requests>> approveRequest(@RequestParam(name = "requestId", required = true) UUID qID) {
		return requestService.getRequestById(qID).flatMap(q -> {
			return recipeService.getRecipeById(q.getRecipeId()).flatMap(r -> {
					r.setCuisine(q.getCuisine());
					r.setRecipeName(q.getName());
					r.setIngredients(q.getIngredients());
					recipeService.updateRecipe(r);
				return requestService.approveOrReject(q).map(request -> ResponseEntity.status(201).body(request))
						.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(q)));
			});

		});
		
	}

	@DeleteMapping
	public Mono<ResponseEntity<Requests>> rejectRequest(@RequestParam(name = "requestId", required = true) UUID qID) {
		return requestService.getRequestById(qID).flatMap(q -> requestService.approveOrReject(q).map(request -> ResponseEntity.status(201).body(request))
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(q))));
	}

}
