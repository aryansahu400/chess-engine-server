package in.aryaura.chess.engine.server.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RateLimitConfiguration {
    @Value( "${rate-limit.enabled}")
    private boolean enabled;
    @Value( "${rate-limit.requests}")
    private int requests;
    @Value( "${rate-limit.duration-seconds}")
    private long durationSeconds;
    @Value( "${rate-limit.cookie-name}")
    private String cookieName;
}

