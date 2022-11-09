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

import com.magadiflo.commons.users.models.entity.User;
import com.magadiflo.oauth2.server.services.IUserService;

import feign.FeignException;

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
		
		
		// Como se ha autenticado correctamente, verificamos si tiene registrado algún intento fallido para poder actualizarlo y setearle a 0
		User authenticatedUser = this.userService.findByUsername(authentication.getName());
		if(authenticatedUser.getAttempts() != null && authenticatedUser.getAttempts() > 0) {
			authenticatedUser.setAttempts(0);
			this.userService.update(authenticatedUser, authenticatedUser.getId());
		}
	}

	// Este método saltará cuando la contraseña sea ingresada de manera incorrecta, es decir, la persona ingresa
	// el username correctamente, pero la contraseña no.
	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		LOGGER.error("Error en el login: {}", exception.getMessage());
		
		try {
			User user = this.userService.findByUsername(authentication.getName());
			if(user.getAttempts() == null) {
				user.setAttempts(0);				
			}
			
			user.setAttempts(user.getAttempts() + 1);
			LOGGER.info("Current attempts: {}", user.getAttempts());
			
			if(user.getAttempts() >= 3) {
				LOGGER.error("User has exceeded the maximum attempts", user.getUsername());
				user.setEnabled(false);
			}
			
			this.userService.update(user, user.getId());
		} catch(FeignException e) {
			LOGGER.error("El usuario {} no existe en el sistema", authentication.getName());			
		}
		
	}

}
