package com.amit.converse.chat.config;

import com.amit.converse.chat.config.filter.ChatContextFilter;
import com.amit.converse.chat.config.filter.JwtAuthenticationFilter;
import com.amit.converse.chat.config.filter.UserContextFilter;
import com.amit.converse.chat.model.Enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ChatContextFilter chatContextFilter;
    private final UserContextFilter userContextFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable()
                )
                .authorizeHttpRequests(authorization -> authorization
//                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/converse/users/newUser").permitAll()
                        .requestMatchers("/chatRoom/admin/**").hasRole(Role.ADMIN.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // First
                .addFilterAfter(userContextFilter, JwtAuthenticationFilter.class)           // Second
                .addFilterAfter(chatContextFilter, UserContextFilter.class);
        return http.build();
    }
}
