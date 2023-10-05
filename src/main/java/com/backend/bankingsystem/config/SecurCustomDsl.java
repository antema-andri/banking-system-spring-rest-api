package com.backend.bankingsystem.config;

import com.backend.bankingsystem.filters.JwtAuthenticationFilter;
import com.backend.bankingsystem.filters.JwtAuthorizationFilter;
import com.backend.bankingsystem.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurCustomDsl extends AbstractHttpConfigurer<SecurCustomDsl, HttpSecurity> {
	private TokenService tokenService;
	
	public SecurCustomDsl(TokenService tokenService) {
		this.tokenService=tokenService;
	}
	
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new JwtAuthenticationFilter(authenticationManager, tokenService));
        http.addFilterBefore(new JwtAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }

}

