package com.magadiflo.oauth2.server.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.magadiflo.commons.users.models.entity.User;
import com.magadiflo.oauth2.server.clients.IUserFeignClient;

@Service
public class UserService implements UserDetailsService {

	private final IUserFeignClient client;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public UserService(IUserFeignClient client) {
		this.client = client;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.client.findByUsername(username);

		if (user == null) {
			LOGGER.error("Login error, username [{}] not found!", username);
			throw new UsernameNotFoundException(String.format("Login error, username [%s] not found!", username));
		}

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> LOGGER.info("Role: {}", authority.getAuthority()))
				.collect(Collectors.toList());

		LOGGER.info("Authenticated user: {}", username);

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getEnabled(), true, true, true, authorities);
	}

}
