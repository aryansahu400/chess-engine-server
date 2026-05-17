package in.aryaura.chess.engine.server.service;

import in.aryaura.chess.engine.server.exception.GeneralException;
import in.aryaura.chess.engine.server.model.SuggestMoveResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public Mono<SuggestMoveResponse> getMoveSuggestion(String fen) {
        System.out.println(Thread.currentThread().isVirtual());
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                            .queryParam("fen", URLEncoder.encode(fen, StandardCharsets.UTF_8))
                            .queryParam("depth", 15)
                        .build()
                ).retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    return Mono.error(new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR,"Couldn't connect to stock fish servers"));
                })
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    return Mono.error(new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR,"Stock fish servers couldn't process the request"));
                })
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {

                            return SuggestMoveResponse.builder()
                                    .move(
                                            Optional.ofNullable((String) response.get("continuation"))
                                                    .orElseThrow(()->new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR,"Couldn't connect to stock fish servers"))
                                                    .split(" ")[0]
                                        )
                                    .evaluation(
                                            Optional.ofNullable((Double)response.get("evaluation"))
                                                    .orElseThrow(()->new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR,"Couldn't connect to stock fish servers"))
                                    )
                                    .explanation(Optional.ofNullable((String) response.get("bestmove"))
                                            .orElseThrow(()->new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR,"Couldn't connect to stock fish servers"))
                                    )
                                    .build();


                        }
                );
    }
}
