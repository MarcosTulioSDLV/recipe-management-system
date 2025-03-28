package com.springboot.recipe_management_system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    //private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomAccessDeniedHandler customAccessDeniedHandler/*, CustomAuthenticationEntryPoint customAuthenticationEntryPoint*/) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        /*this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;*/
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(c->c.disable())
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize->authorize
                        //For Swagger and OpenAPI (Allow access to Swagger and OpenAPI without authentication)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        //Role
                        .requestMatchers(HttpMethod.GET,"/api/v1/roles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/roles/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/roles/*").hasRole("ADMIN")
                        //User
                        .requestMatchers(HttpMethod.GET,"/api/v1/auth").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/auth/self").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/auth/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/auth/self").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/auth/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/auth/*").hasRole("ADMIN")
                        //Recipe
                        .requestMatchers(HttpMethod.GET, "/api/v1/recipes/self/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/recipes/self").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/recipes/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/recipes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/users/self/recipes").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/v1/users/*/recipes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/recipes/self/*").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/recipes/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/recipes/self/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/recipes/*").hasRole("ADMIN")
                        //Ingredient
                        .requestMatchers(HttpMethod.GET,"/api/v1/ingredients/self/*").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/ingredients/self").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/ingredients/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/ingredients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/recipes/self/*/ingredients").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/v1/recipes/*/ingredients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/ingredients/self/*").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/ingredients/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/ingredients/self/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/ingredients/*").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .exceptionHandling(e-> e
                        .accessDeniedHandler(customAccessDeniedHandler) //Handles 403 Forbidden
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))//Handles 401 Unauthorized*/
                //.exceptionHandling(exception -> exception
                        //.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
