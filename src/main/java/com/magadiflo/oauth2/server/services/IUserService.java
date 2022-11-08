package com.magadiflo.oauth2.server.services;

import com.magadiflo.commons.users.models.entity.User;

public interface IUserService {
	
	User findByUsername(String username);

}
