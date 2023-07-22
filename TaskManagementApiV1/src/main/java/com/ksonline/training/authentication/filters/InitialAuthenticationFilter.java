package com.ksonline.training.authentication.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ksonline.training.authentication.UsernamePasswordAuthentication;
import com.ksonline.training.log.Logger;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
//@CrossOrigin("http://localhost:3000")
public class InitialAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationManager manager;

	@Value("${jwt.signing.key}")
	private String signingKey;

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String username = request.getHeader("name");
		String password = request.getHeader("password");
		
		Logger.log("inside filter InitalAuthenication");
		Logger.log(username);
		Logger.log(password);

		Authentication principal = new UsernamePasswordAuthentication(username, password);
		manager.authenticate(principal);

		SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
		String jwt = Jwts.builder().setClaims(Map.of("name", username)).signWith(key).compact();
		response.setHeader("Authorization", jwt);
		//filterChain.doFilter(request, response);
	}

	// should apply this filter on /login (double negation)
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		// apply only on login,all other urls are excempted
		// dont apply filter on urls notequls to login
		// intercepts urls other than login
		return !request.getServletPath().equals("/api/v1/taskapp/login");
	}
}
