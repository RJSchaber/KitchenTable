package com.project2.kitchentable.services;

import java.util.UUID;

import com.project2.kitchentable.beans.Notes;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteService {

	Mono<Notes> addNotes(Notes n);

	Mono<Notes> updateNotes(Notes n);

	Flux<Notes> getNotes(UUID recipeId);

	Mono<Notes> getNotesByRecipeAndUser(UUID recipeID, UUID userID);

	Mono<Void> removeNotes(UUID recipeID, UUID userID);

}