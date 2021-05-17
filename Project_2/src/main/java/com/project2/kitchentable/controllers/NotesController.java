package com.project2.kitchentable.controllers;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.project2.kitchentable.beans.Notes;
import com.project2.kitchentable.beans.User;
import com.project2.kitchentable.services.NoteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/notes")
public class NotesController {

	@Autowired
	private NoteService noteService;
	@Autowired
	private AuthController authorize;
	private static Logger log = LogManager.getLogger(NotesController.class);
	
	@PostMapping
	public Mono<Notes> addNote(ServerWebExchange exchange, @RequestBody Notes n) {
		User user = authorize.userAuth(exchange);
		if(user != null) {
			log.trace("%s is trying to add a note", user.getFirstname());
			n.setUserId(user.getUserID());
			return noteService.addNotes(n);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}
	
	@GetMapping(value="/{recipeid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Notes> getNotesByRecipe(ServerWebExchange exchange, @PathVariable("recipeid") String recipeID) {
		User u = authorize.userAuth(exchange);
		if(u != null) {
			return noteService.getNotes(UUID.fromString(recipeID));
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}
	
	@PutMapping("/{noteid}")
	public Mono<Notes> updateNote(ServerWebExchange exchange, @PathVariable("noteid") String noteID, @RequestBody Notes n){
		User user = authorize.userAuth(exchange);
		if(user != null && n.getUserId() == user.getUserID()) {
			return noteService.updateNotes(n);
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}
	
	@DeleteMapping("/{recipeid}")
	public Mono<Void> removeNote(ServerWebExchange exchange, @PathVariable("recipeid") String recipeID, @PathVariable("userid") String userID){
		User user = authorize.userAuth(exchange);
		if(user != null && (user.getUserType() == 3 || UUID.fromString(userID) == user.getUserID())) {
			return noteService.removeNotes(UUID.fromString(recipeID), UUID.fromString(userID));
		}
		exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
		return null;
	}
}
