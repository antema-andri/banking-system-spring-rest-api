package com.backend.bankingsystem.filters;

import java.io.IOException;
import java.text.ParseException;

import com.backend.bankingsystem.service.TokenService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends OncePerRequestFilter{
	private TokenService tokenService;
	
	public JwtAuthorizationFilter(TokenService tokenService) {
		this.tokenService=tokenService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String bearer="Bearer ";
		String authorizationToken=request.getHeader("Authorization");
		String token="";
		if(authorizationToken!=null && authorizationToken.startsWith(bearer)) {
			token=authorizationToken.replace(bearer, "");
			try {
				if(tokenService.isValidToken(token)) {
					filterChain.doFilter(request, response);
					return;
				}else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Access denied. Invalid token.");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if(request.getRequestURI().equals("/api/auth/token") || request.getRequestURI().startsWith("/h2-console")){
			filterChain.doFilter(request, response);
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Access denied. Missing token.");
		}
	}

}
