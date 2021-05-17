package com.project2.kitchentable.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project2.kitchentable.beans.User;
import com.project2.kitchentable.services.UserService;
import com.project2.kitchentable.utils.JWTParser;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
	@TestConfiguration
	static class Configuration {
		
		public UserController getPlayerService(UserService us) {
			UserController uc = new UserController();
			uc.setUserService(us);
			return uc;
		}

		public UserService getService() {
			return Mockito.mock(UserService.class);
		}

		public JWTParser getToken() {
			return Mockito.mock(JWTParser.class);
		}

		@Bean
		public ObjectMapper getMapper() {
			return Mockito.mock(ObjectMapper.class);
		}
	}

	@Autowired
	private UserController uc;
	@Autowired
	private UserService us;

	@Test
	public void testRegisterUserReturnsEntityWithStatus201() {
		User user = new User();

		Mockito.when(us.addUser(user)).thenReturn(Mono.just(user));

		Mono<ResponseEntity<User>> result = uc.registerUser(user);

		StepVerifier.create(result).expectNext(ResponseEntity.status(201).body(user)).expectComplete().verify();
	}

	@Test
	public void testRegisterUserReturns400() {
		User user = new User();

		Mockito.when(us.addUser(user)).thenReturn(Mono.error(new Exception()));

		Mono<ResponseEntity<User>> result = uc.registerUser(user);

		StepVerifier.create(result).expectNext(ResponseEntity.badRequest().body(user)).expectComplete().verify();
	}
}
