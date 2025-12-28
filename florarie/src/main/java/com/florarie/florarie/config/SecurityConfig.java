package com.florarie.florarie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                // public
                .requestMatchers("/", "/flowers/**", "/bouquet/**", "/css/**", "/js/**").permitAll()
                // H2 console (doar dev)
                .requestMatchers("/h2-console/**").permitAll()
                // admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // restul necesita autentificare
                .anyRequest().authenticated()
        );

        http.formLogin(login -> login.permitAll());
        http.logout(logout -> logout.permitAll());

        // H2 console are nevoie de frame + fara CSRF pe acele rute
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
