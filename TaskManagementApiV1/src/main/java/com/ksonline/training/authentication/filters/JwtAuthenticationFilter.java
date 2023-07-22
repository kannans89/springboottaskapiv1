package com.ksonline.training.authentication.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ksonline.training.authentication.UsernamePasswordAuthentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.signing.key}")
	private String signingKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.err.println("inside filter JwtAuthentication");
		String jwt = request.getHeader("Authorization");

		SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt)// json web token signed(JWT
				.getBody();

		String username = String.valueOf(claims.get("name"));
		GrantedAuthority a = new SimpleGrantedAuthority("user");
		var auth = new UsernamePasswordAuthentication(username, null, List.of(a));
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return 	(request.getServletPath().equals("/api/v1/taskapp/login")
				|| request.getServletPath().equals("/api/v1/taskapp/register"));
	}
}