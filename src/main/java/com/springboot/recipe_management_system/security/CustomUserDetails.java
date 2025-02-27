package com.springboot.recipe_management_system.security;

import com.springboot.recipe_management_system.models.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<? extends GrantedAuthority> authorities= userEntity.getRoles().stream()
                .map(r->new SimpleGrantedAuthority("ROLE_"+r.getRole().toString()))
                .collect(Collectors.toList());
        return authorities;
    }
    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<? extends GrantedAuthority> authorities= userEntity.getRoles()
                .stream()
                .map(role-> new SimpleGrantedAuthority("ROLE_"+role))
                .toList();
        System.out.println("Granted authorities: "+authorities);
        return authorities;
    }*/

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
