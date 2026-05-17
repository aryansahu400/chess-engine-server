package in.aryaura.chess.engine.server.filter;

import in.aryaura.chess.engine.server.configuration.RateLimitConfiguration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Component
public class RateLimitingFilter implements WebFilter, Ordered {

    private final RateLimitConfiguration properties;
    private final ReactiveStringRedisTemplate redisTemplate;

    public RateLimitingFilter(
            RateLimitConfiguration properties,
            ReactiveStringRedisTemplate redisTemplate
    ) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        String cookieName = properties.getCookieName();

        String clientId = exchange.getRequest()
                .getCookies()
                .getFirst(cookieName) != null
                ? exchange.getRequest().getCookies().getFirst(cookieName).getValue()
                : null;

        ServerHttpResponse response = exchange.getResponse();

        if (clientId == null || clientId.isBlank()) {

            clientId = UUID.randomUUID().toString();

            ResponseCookie cookie = ResponseCookie.from(cookieName, clientId)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(365))
                    .build();

            response.addCookie(cookie);
        }

        String redisKey = "rate_limit:" + clientId;

        return redisTemplate.opsForValue()
                .increment(redisKey)
                .flatMap(count -> {

                    Mono<Boolean> expiryMono;

                    if (count == 1) {
                        expiryMono = redisTemplate.expire(
                                redisKey,
                                Duration.ofSeconds(properties.getDurationSeconds())
                        );
                    } else {
                        expiryMono = Mono.just(true);
                    }

                    return expiryMono.flatMap(ignore -> {

                        if (count > properties.getRequests()) {

                            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                            return response.writeWith(
                                    Mono.just(
                                            response.bufferFactory()
                                                    .wrap("Rate limit exceeded".getBytes())
                                    )
                            );
                        }

                        return chain.filter(exchange);
                    });
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}