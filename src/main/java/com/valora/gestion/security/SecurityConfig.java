package com.valora.gestion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF por completo de forma explícita para TODAS las rutas
                .csrf(csrf -> csrf.disable())

                // Configuramos CORS antes que los requisitos de autenticación
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        // Permitimos explícitamente las opciones de CORS (Preflight)
                        .requestMatchers(org.springframework.web.bind.annotation.RequestMethod.OPTIONS.name())
                        .permitAll()
                        // Permitimos todo a la API
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll() // Cambiamos .authenticated() por .permitAll() temporalmente
                );

        return http.build();
    }

    // 5. Definir el Bean de CORS que usará el filtro de seguridad
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Poné la URL exacta de tu Angular
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://valora-peach.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}