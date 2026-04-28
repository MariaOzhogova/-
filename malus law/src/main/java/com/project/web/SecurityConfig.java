package com.project.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем доступ абсолютно ко всем эндпоинтам без авторизации
                        .anyRequest().permitAll()
                )
                // Отключаем дефолтную HTML-форму логина
                .formLogin(AbstractHttpConfigurer::disable)
                // Отключаем базовую авторизацию (всплывающее окно)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Оставляем настройки отключения CSRF для API
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/calculate", "/api/error/toggle", "/api/error/reset")
                );

        return http.build();
    }
}
    // Метод userDetailsService удален, так как без формы входа он не нужен