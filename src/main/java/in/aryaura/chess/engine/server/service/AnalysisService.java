package in.aryaura.chess.engine.server.service;

import in.aryaura.chess.engine.server.model.AnalysisRequest;
import in.aryaura.chess.engine.server.prompt.SystemPrompts;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

import java.time.Duration;


@Service
public class AnalysisService {


    private final ChatClient chatClient;

    @Autowired
    public AnalysisService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @SneakyThrows
    public Flux<String> analysis(AnalysisRequest request) {

        return chatClient
                .prompt(SystemPrompts.MOVE_ANALYSIS.formatted(
                        request.getFenBefore(),
                        request.getFenAfter()
                ))
                .stream()
                .content()
                .bufferTimeout(20, Duration.ofMillis(100))
                .map(tokens -> String.join("", tokens));
    }
}
