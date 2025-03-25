package com.springboot.recipe_management_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RecipeManagementSystemApplication implements CommandLineRunner {

	private static final Logger LOG= LoggerFactory.getLogger(RecipeManagementSystemApplication.class);

	private final PasswordEncoder passwordEncoder;

	@Autowired
    public RecipeManagementSystemApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
		SpringApplication.run(RecipeManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        LOG.info("Default user with ADMIN role: marcos, Default password: 123, Encoded password: {}", passwordEncoder.encode("123"));
		LOG.info("Default user with USER role: pedro, Default password: 123, Encoded password: {}", passwordEncoder.encode("123"));
	}

}
