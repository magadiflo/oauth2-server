package com.magadiflo.oauth2.server.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.magadiflo.commons.users.models.entity.User;
import com.magadiflo.oauth2.server.services.IUserService;

/***
 * Para agregar información al JWT debemos crear una clase que implemente la
 * interfaz TokenEnhancer (token potenciador) permite agregar nuevos clamis
 * 
 */

@Component
public class InfoAdicionalToken implements TokenEnhancer {

	private final IUserService userService;

	public InfoAdicionalToken(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User user = this.userService.findByUsername(authentication.getName());

		Map<String, Object> info = new HashMap<>();
		info.put("name", user.getName());
		info.put("lastName", user.getLastName());
		info.put("email", user.getEmail());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}
