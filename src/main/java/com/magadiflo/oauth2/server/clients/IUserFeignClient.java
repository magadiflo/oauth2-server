package com.magadiflo.oauth2.server.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.magadiflo.commons.users.models.entity.User;

//@FeignClient, para decirle que esta interfaz sea un cliente Feign
//name, nombre del microservicio con el que nos queremos comunicar

@FeignClient(name = "microservice-users")
public interface IUserFeignClient {

	@GetMapping(path = "/users/search/find-username")
	public User findByUsername(@RequestParam("user") String username);
	
	@PutMapping("/users/{id}")
	public User update(@RequestBody User user, @PathVariable Long id);

}
