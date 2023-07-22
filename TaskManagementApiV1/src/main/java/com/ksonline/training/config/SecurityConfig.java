package com.ksonline.training.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ksonline.training.authentication.filters.InitialAuthenticationFilter;
import com.ksonline.training.authentication.filters.JwtAuthenticationFilter;
import com.ksonline.training.authentication.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private InitialAuthenticationFilter initialAuthenticationFilter;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 
    	 http.cors(c -> {                                              
    	      CorsConfigurationSource source = request -> {
    	        CorsConfiguration config = new CorsConfiguration();
    	        config.addAllowedOrigin("*");
    	        config.addAllowedHeader("*");
    	       // config.addExposedHeader("*");
    	        //config.setAllowedOrigins(
    	          //  List.of("http://localhost:3000"));
    	        //config.setAllowedMethods(
    	           // List.of("GET", "POST", "PUT", "DELETE"));
    	        config.addAllowedMethod("*");
    	        config.addExposedHeader("Authorization");
    	        return config;
    	      };
    	      c.configurationSource(source);
    	    });
        http.csrf().disable();

        http.addFilterAt(
                initialAuthenticationFilter,
                BasicAuthenticationFilter.class)
            .addFilterAfter(
                jwtAuthenticationFilter,
                BasicAuthenticationFilter.class
            );
        

        http.authorizeRequests()
              .mvcMatchers("/api/v1/taskapp/register").permitAll()
              .anyRequest().authenticated();
        
       
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
