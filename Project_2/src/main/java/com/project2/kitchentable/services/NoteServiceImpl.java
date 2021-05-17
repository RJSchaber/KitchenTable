package com.project2.kitchentable.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project2.kitchentable.beans.Notes;
import com.project2.kitchentable.data.ReactiveNoteRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private ReactiveNoteRepo noteRepo;

	@Override
	public Mono<Notes> addNotes(Notes n) {
		return noteRepo.insert(n);
	}

	@Override
	public Flux<Notes> getNotes(UUID recipeId) {
		return noteRepo.findByRecipeId(recipeId);
	}

	@Override
	public Mono<Notes> updateNotes(Notes n) {
		return noteRepo.save(n);
	}

	@Override
	public Mono<Notes> getNotesByRecipeAndUser(UUID recipeID, UUID userID) {

		return noteRepo.findByRecipeIdAndUserId(recipeID, userID);
	}

	@Override
	public Mono<Void> removeNotes(UUID recipeID, UUID userID) {
		return noteRepo.deleteByRecipeIdAndUserId(recipeID, userID);
	}

}
