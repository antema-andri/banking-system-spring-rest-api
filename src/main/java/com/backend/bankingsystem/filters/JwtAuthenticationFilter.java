package com.backend.bankingsystem.filters;

import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.dto.AuthenticationDTO;
import com.backend.bankingsystem.service.AppUserService;
import com.backend.bankingsystem.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	private TokenService tokenService;
	private AppUserService appUserService;
	
	public JwtAuthenticationFilter(
			AuthenticationManager authenticationManager,
			TokenService tokenService,
			AppUserService appUserService
	) {
		setFilterProcessesUrl("/api/auth/token");
		this.authenticationManager=authenticationManager;
		this.tokenService=tokenService;
		this.appUserService=appUserService;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AuthenticationDTO authenticationDTO=new ObjectMapper().readValue(request.getInputStream(), AuthenticationDTO.class);
			String username= authenticationDTO.getUsername();
			String password= authenticationDTO.getPassword();
			UsernamePasswordAuthenticationToken u=new UsernamePasswordAuthenticationToken(username,password);
			return authenticationManager.authenticate(u);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String jwtAccessToken=tokenService.generateToken(authResult);
		String responseBody=buildResponseBody(authResult);

		response.setContentType("application/json");
		response.getWriter().write(responseBody);
		response.setHeader("Authorization", jwtAccessToken);
	}

	public String buildResponseBody(Authentication authResult) {
		UserDetails userDetails = (UserDetails) authResult.getPrincipal();
		AppUserDTO appUserDTO = appUserService.getCurrentUser(userDetails.getUsername());
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			String userJson = objectMapper.writeValueAsString(appUserDTO);
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("user", userJson);

			return objectMapper.writeValueAsString(responseMap);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
