package com.revplay.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeExchange(exchanges -> exchanges
                // Public endpoints
                .pathMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/songs/search").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/songs/most-played").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/songs/latest").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/albums/search").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/artists/search").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/search/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/browse/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/catalog/trending").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/playlists/public/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/api/auth/**").permitAll()
                // Protected endpoints
                .pathMatchers(HttpMethod.POST, "/api/catalog/songs").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/catalog/songs/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/catalog/songs/**").authenticated()
                .pathMatchers(HttpMethod.POST, "/api/catalog/albums").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/catalog/albums/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/catalog/albums/**").authenticated()
                .pathMatchers(HttpMethod.POST, "/api/catalog/artists").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/catalog/artists/**").authenticated()
                .pathMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()
                .pathMatchers(HttpMethod.POST, "/api/playlists/**").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/playlists/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/playlists/**").authenticated()
                .pathMatchers(HttpMethod.POST, "/api/favourites/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/favourites/**").authenticated()
                .pathMatchers(HttpMethod.POST, "/api/playback/**").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/playback/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/playback/**").authenticated()
                .anyExchange().authenticated()
            );
        
        return http.build();
    }
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowOriginPatterns(List.of("http://localhost:4200", "http://localhost:3000"));
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}
