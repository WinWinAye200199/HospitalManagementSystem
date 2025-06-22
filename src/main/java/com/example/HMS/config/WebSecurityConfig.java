package com.example.HMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.HMS.security.JwtAuthenticationEntryPoint;
import com.example.HMS.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, 
														UserDetailsService userDetailsService, 
														PasswordEncoder passwordEncoder) throws Exception{
		//Get AuthManager Builder
		AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		//Set UserDetail Service and PasswordEncoder
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		//Build AuthManager
		AuthenticationManager authenticationManager = builder.build();
		
		return authenticationManager;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.cors();
	    http.csrf().disable();
	    http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	        .authorizeHttpRequests(authorize -> authorize
	            .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
	            .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll() // Permission for signup
	            .antMatchers(HttpMethod.PUT, "/api/auth/reset-password").permitAll()
	            .antMatchers(HttpMethod.POST, "/api/auth/request-reset-password").permitAll()
	            .antMatchers(HttpMethod.GET, "/api/users/getAllDoctorSchedules").permitAll()
	            .antMatchers(HttpMethod.GET, "/api/users/getDoctorWithSpecialization").permitAll()
	            .antMatchers(HttpMethod.GET, "/api/admin/doctorsWithDepartment").permitAll()
	            .antMatchers(HttpMethod.GET, "/api/admin/allDepartments").permitAll()
	            .antMatchers(HttpMethod.GET, "/api/admin/doctors").permitAll()
	            .antMatchers(HttpMethod.GET,"/api/staffs/fees").permitAll()
	            .antMatchers(HttpMethod.GET, "/api/admin/getMyProfile").permitAll()
	            .antMatchers("/api/users/**").hasAnyRole("USER","ADMIN","STAFF")
	            .antMatchers("/api/admin/**").hasRole("ADMIN")
	            .antMatchers("/api/doctors/**").hasRole("DOCTOR")
	            .antMatchers("/api/nurses/**").hasRole("NURSE")
	            .antMatchers("/api/staffs/**").hasRole("STAFF")
	            
	            .anyRequest().authenticated());
	    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

}
