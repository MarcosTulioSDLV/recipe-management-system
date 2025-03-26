package com.springboot.recipe_management_system.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.springboot.recipe_management_system.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken= recoverToken(request);
        if(jwtToken!=null){
            DecodedJWT decodedJWT= jwtUtils.validateToken(jwtToken);
            String username= jwtUtils.extractUsername(decodedJWT);

            //Method 1 (recovering info from the db)
            UserDetails userDetails= userDetailsService.loadUserByUsername(username);
            List<? extends GrantedAuthority> authorities= new ArrayList<>(userDetails.getAuthorities());

            //Method 2 (recovering info from the token)
            /*String authorityString= jwtUtils.extractSpecificClaim(decodedJWT,"authorities").asString();
            List<GrantedAuthority> authorities= AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString);*/

            Authentication authentication= new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
            //Authentication authentication= new UsernamePasswordAuthenticationToken(username,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }

    public String recoverToken(HttpServletRequest request){
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwtToken==null || !jwtToken.startsWith("Bearer ")){
            return null;
        }
        return jwtToken.substring(7);
    }

}
