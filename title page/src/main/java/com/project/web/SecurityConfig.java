package com.project.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/api/**", "/style.css", "/script.js").permitAll()
                        .requestMatchers("/title.css", "/title.js", "/malus-law.css", "/malus-law.js", "/labs.json").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.defaultSuccessUrl("/", true))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/calculate", "/api/error/toggle", "/api/error/reset")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("\u0441\u0442\u0443\u0434\u0435\u043d\u0442").password("{noop}12345").roles("USER").build(),
                User.withUsername("\u043f\u0440\u0435\u043f\u043e\u0434").password("{noop}12345").roles("ADMIN").build()
        );
    }
}
