package com.backend.bankingsystem.config;

import com.backend.bankingsystem.filters.JwtAuthenticationFilter;
import com.backend.bankingsystem.filters.JwtAuthorizationFilter;
import com.backend.bankingsystem.service.AppUserService;
import com.backend.bankingsystem.service.TokenService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Data
public class SecurCustomDsl extends AbstractHttpConfigurer<SecurCustomDsl, HttpSecurity> {
	private TokenService tokenService;
    private AppUserService appUserService;
	
	public SecurCustomDsl() {
	}
	
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new JwtAuthenticationFilter(
                authenticationManager,
                tokenService,
                appUserService
        ));
        http.addFilterBefore(new JwtAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }

}

