package com.springboot.recipe_management_system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(c->c.disable())
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize->authorize
                        //Role
                        .requestMatchers(HttpMethod.GET,"/api/v1/roles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/roles/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/roles/*").hasRole("ADMIN")
                        //User
                        .requestMatchers(HttpMethod.GET,"/api/v1/auth").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/auth/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/auth/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/auth/*").hasRole("ADMIN")
                        //Recipe
                        .requestMatchers(HttpMethod.GET,"/api/v1/recipes").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/recipes/*").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/by-username-exact-match/*/recipes").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/by-username-partial-match/*/recipes").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/recipes/by-title-exact-match/*").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/recipes/by-title-partial-match/*").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/v1/users/*/recipes").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/recipes/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/recipes/*").authenticated()
                        //Ingredient
                        .requestMatchers(HttpMethod.GET,"/api/v1/ingredients").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/ingredients/*").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/v1/recipes/*/ingredients").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/ingredients/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/ingredients/*").authenticated()
                        .anyRequest().denyAll())
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
