package in.aryaura.chess.engine.server.service;

import in.aryaura.chess.engine.server.model.AnalysisRequest;
import in.aryaura.chess.engine.server.prompt.SystemPrompts;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;


@Service
public class AnalysisService {


    private final ChatClient chatClient;

    @Autowired
    public AnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    @SneakyThrows
    public Flux<String> analysis(AnalysisRequest request) {

        return chatClient
                .prompt(SystemPrompts.MOVE_ANALYSIS.formatted(request.getFenBefore(), request.getFenAfter()))
                .stream()
                .content();
    }
}
