package com.magadiflo.oauth2.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableFeignClients // Habilitamos el uso del cliente feign. Para poder comunicar este microservicio con otro (similar al restTesmplate)
@EnableEurekaClient
@SpringBootApplication
public class Oauth2ServerApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String password = "12345";

		for (int i = 0; i < 4; i++) {
			System.out.println(this.passwordEncoder.encode(password));
		}

	}

}
