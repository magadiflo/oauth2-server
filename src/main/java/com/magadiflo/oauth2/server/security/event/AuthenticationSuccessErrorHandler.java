package com.magadiflo.oauth2.server.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();

		LOGGER.info("Success login: {}", user.getUsername());
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		LOGGER.error("Error en el login: {}", exception.getMessage());
	}

}
