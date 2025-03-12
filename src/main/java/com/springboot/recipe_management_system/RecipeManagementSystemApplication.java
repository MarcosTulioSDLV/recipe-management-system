package com.springboot.recipe_management_system;

import com.springboot.recipe_management_system.enums.RoleEnum;
import com.springboot.recipe_management_system.models.Role;
import com.springboot.recipe_management_system.models.UserEntity;
import com.springboot.recipe_management_system.repositories.RoleRepository;
import com.springboot.recipe_management_system.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RecipeManagementSystemApplication implements CommandLineRunner {

	private static final Logger LOG= LoggerFactory.getLogger(RecipeManagementSystemApplication.class);

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final RoleRepository roleRepository;

	@Autowired
    public RecipeManagementSystemApplication(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(RecipeManagementSystemApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		LOG.info("Hello World!");

		/*Role role1= new Role(null,RoleEnum.ADMIN,new ArrayList<>());
		Role role2= new Role(null,RoleEnum.USER,new ArrayList<>());

		roleRepository.saveAll(List.of(role1,role2));

		UserEntity user1= new UserEntity();
		user1.setUsername("marcos");
		user1.setPassword(passwordEncoder.encode("123"));
		user1.setEmail("m@gmail.com");
		user1.setRoles(List.of(role1,role2));

		UserEntity user2= new UserEntity();
		user2.setUsername("pedro");
		user2.setPassword(passwordEncoder.encode("123"));
		user2.setEmail("p@gmail.com");
		user2.setRoles(List.of(role2));

		userRepository.saveAll(List.of(user1,user2));*/

        LOG.info("Default Admin user: marcos, Default password: 123, Encoded password: {}", passwordEncoder.encode("123"));
	}

}
