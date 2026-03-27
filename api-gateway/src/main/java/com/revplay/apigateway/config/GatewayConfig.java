package com.revplay.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // User Service Routes
            .route("user-service", r -> r.path("/api/users/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("user-service-cb"))
                    .retry(retry -> retry.setRetries(3))
                    .stripPrefix(1))
                .uri("lb://user-service"))
            
            // Catalog Service Routes
            .route("catalog-service", r -> r.path("/api/catalog/**", "/api/songs/**", "/api/search/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("catalog-service-cb"))
                    .retry(retry -> retry.setRetries(3))
                    .stripPrefix(1))
                .uri("lb://catalog-service"))
            
            // Playlist Service Routes
            .route("playlist-service", r -> r.path("/api/playlists/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("playlist-service-cb"))
                    .retry(retry -> retry.setRetries(3))
                    .stripPrefix(1))
                .uri("lb://playlist-service"))
            
            // Favourite Service Routes
            .route("favourite-service", r -> r.path("/api/favourites/**", "/api/likes/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("favourite-service-cb"))
                    .retry(retry -> retry.setRetries(3))
                    .stripPrefix(1))
                .uri("lb://favourite-service"))
            
            // Playback Service Routes
            .route("playback-service", r -> r.path("/api/playback/**", "/api/history/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("playback-service-cb"))
                    .retry(retry -> retry.setRetries(3))
                    .stripPrefix(1))
                .uri("lb://playback-service"))
            
            // Analytics Service Routes
            .route("analytics-service", r -> r.path("/api/analytics/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("analytics-service-cb"))
                    .retry(retry -> retry.setRetries(3))
                    .stripPrefix(1))
                .uri("lb://analytics-service"))
            
            .build();
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> exchange.getRequest().getHeaders().getFirst("X-User-ID");
    }

    @Bean
    public Resilience4JConfigBuilder<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfig> configBuilder() {
        return Resilience4JConfigBuilder.<Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfig>builder()
                .failureRateThreshold(50)
                .waitDuration(Duration.ofSeconds(5))
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5);
    }

    @Bean
    public Resilience4JConfigBuilder<Resilience4JConfigBuilder.Resilience4JTimeLimiterConfig> timeLimiterConfig() {
        return Resilience4JConfigBuilder.<Resilience4JConfigBuilder.Resilience4JTimeLimiterConfig>builder()
                .limitForPeriod(Duration.ofSeconds(60))
                .limitRefreshPeriod(Duration.ofSeconds(60))
                .timeoutDuration(Duration.ofSeconds(3))
                .limitSize(200);
    }
}
