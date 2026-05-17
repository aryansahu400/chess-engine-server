package in.aryaura.chess.engine.server.service;

import in.aryaura.chess.engine.server.model.SuggestMoveResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Service
public class SuggestionService {


    private final WebClient webClient;
    public SuggestionService(
            @Value( "${suggestion.url}")String baseUrl,
            WebClient.Builder builder
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }
    public Mono<Map<String,Object>> getMoveSuggestion(String fen) {
        System.out.println(Thread.currentThread().isVirtual());
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                            .queryParam("fen", URLEncoder.encode(fen, StandardCharsets.UTF_8))
                            .queryParam("depth", 15)
                        .build()
                ).retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {
                            String move = Optional.ofNullable((String) response.get("bestMove")).orElseThrow()
                            SuggestMoveResponse.builder()
                                    .move()
                                    .build()
                        }
                );
    }
}
