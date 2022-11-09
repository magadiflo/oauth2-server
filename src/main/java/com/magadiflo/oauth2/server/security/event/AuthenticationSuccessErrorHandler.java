package com.magadiflo.oauth2.server.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.magadiflo.oauth2.server.services.IUserService;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	private final IUserService userService; // Se obtendrá de la clase service UserService
	
	public AuthenticationSuccessErrorHandler(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		// Tenemos dos alternativas para que no tome el username del cliente app ya que solamete nos interesa 
		// cuando el usuario registrado en la BD se ha autenticado exitosamente.
		// Para el ejemplo, se dejaron las dos formas, se puede optar solo por una.
		
		// 1° alternativa: verifica que es el cliente de la aplicación: onlinestoreapp
		if (authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}

		// 2° alternativa, esta forma queda muy acoplado. Por que que pasa si se cambia el cliente: onlinestoreapp, se tendría que venir aquí y actualizar
		if (authentication.getName().equalsIgnoreCase("onlinestoreapp")) {
			return;
		}

		UserDetails user = (UserDetails) authentication.getPrincipal();

		LOGGER.info("Success login: {}", user.getUsername());
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		LOGGER.error("Error en el login: {}", exception.getMessage());
	}

}
