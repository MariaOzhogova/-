package com.project.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/calculate",
                                "/api/error",
                                "/api/error/toggle",
                                "/api/error/reset",
                                "/api/save-lab",
                                "/style.css",
                                "/script.js",
                                "/favicon.ico",
                                "/csrf_test.html",
                                "/load_test.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/calculate", "/api/error/toggle", "/api/error/reset")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        log.info("Загрузка пользователей: студент (USER), препод (ADMIN)");
        log.warn("неудачная попытка");
        UserDetails student = User.builder()
                .username("студент")
                .password("{noop}12345")
                .roles("USER")
                .build();

        UserDetails teacher = User.builder()
                .username("препод")
                .password("{noop}54321")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(student, teacher);
    }
}