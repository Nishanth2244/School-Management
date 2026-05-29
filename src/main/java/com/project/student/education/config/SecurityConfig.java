package com.project.student.education.config;

import com.project.student.education.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    // ---------------- PASSWORD ENCODER ---------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ---------------- AUTH PROVIDER ------------------------
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ---------------- AUTH MANAGER -------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ---------------- CORS CONFIG (ACCEPT ALL IN LAN) ------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow frontend from localhost, LAN IPs, mobile hotspot
        config.setAllowedOriginPatterns(List.of(
                "*"
        ));

        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // ---------------- SECURITY FILTER CHAIN ----------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(f -> f.deny()))
                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth

                        // CORS Preflight should always pass
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                        // PUBLIC ENDPOINTS (NO AUTH REQUIRED)
//                        .requestMatchers(
//                                "/api/student/auth/login",
//                                "/api/student/auth/signup",
//                                "/api/student/auth/refresh-token",
//                                "/api/student/auth/forgot-password",
//                                "/api/student/auth/reset-password",
//                                "/api/student/admission",
//                                "/api/student/admissions",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/actuator/**",
//                                "/api/student/notifications/**",
//                                "/images/**",
//                                "/ai/**",
//                                "/apisyniq/**",
//                                "/api-syniq/**",
//                                "/syniq/**",
//                                "/index.html",
//                                "/static/**",
//                                "/public/**",
//                                "/webjars/**",
//                                "/RepresentUI.html"
//                        ).permitAll()
//
//                        // Authenticated Endpoints
//                        .requestMatchers("/api/student/auth/change-password").authenticated()
//
//                        .anyRequest().authenticated()
                		
                		.anyRequest().permitAll()
                )

                // JWT filter placed before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
