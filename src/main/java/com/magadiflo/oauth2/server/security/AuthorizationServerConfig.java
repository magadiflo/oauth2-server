package com.magadiflo.oauth2.server.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer // Lo habilitamos como un servidor de autorización
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	// Viene de la clase SpringSecurityConfig del método anotado con @Bean passwordEncoder()
	private final PasswordEncoder passwordEncoder;
		
	// Viene de la clase SpringSecurityConfig del método anotado con @Bean authenticationManager()
	private final AuthenticationManager authenticationManager;
	
	// Viene de la clase InfoAdicionalToken
	private final InfoAdicionalToken infoAdicionalToken;
	
	public AuthorizationServerConfig(PasswordEncoder passwordEncoder, 
			AuthenticationManager authenticationManager, InfoAdicionalToken infoAdicionalToken) {
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.infoAdicionalToken = infoAdicionalToken;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Configuración de un único cliente: onlinestoreapp, 
		// Si queremos agregar más clientes, después del refreshTokenValiditySeconds(3600), concatenar
		// un .and() y agregar nuevamente un .withClient()... y establecerle nuevas configuraciones
		clients.inMemory()
			.withClient("onlinestoreapp")
			.secret(this.passwordEncoder.encode("12345"))
			.scopes("read", "write")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(3600)
			.refreshTokenValiditySeconds(3600);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//En el Arrays.asList, agregamos la información adicional para el Token + Información por defecto del Token (this.accessTokenConverter())
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(
				Arrays.asList(this.infoAdicionalToken, this.accessTokenConverter())); 
		
		endpoints.authenticationManager(this.authenticationManager)
			.tokenStore(this.tokenStore())
			.accessTokenConverter(this.accessTokenConverter())
			.tokenEnhancer(tokenEnhancerChain); // Agregamos a la configuración del endpoints la cadena (nueva información + información por defecto)
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(this.accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() { // Información, datos por defecto del JWT
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("clave12345");				
		return tokenConverter;
	}

}
