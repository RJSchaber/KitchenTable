package com.project2.kitchentable.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.project2.kitchentable.beans.User;
import com.project2.kitchentable.utils.JWTParser;

@Component
public class AuthController {
	private static Logger log = LogManager.getLogger(AuthController.class);
	
	@Autowired
	private JWTParser tokenService;
	
	public User userAuth(ServerWebExchange exchange) {
		User u = null;
		
		try {
			if(exchange.getRequest().getCookies().get("token") != null) {
				String token = exchange.getRequest().getCookies().getFirst("token").getValue();
				log.trace(token);
				if(!token.equals("")) {
					u = tokenService.parser(token);
				}
			}
		}catch (Exception e) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return null;
		}
		
		return u;
	}
	
}
