package in.aryaura.chess.engine.server.controller;

import in.aryaura.chess.engine.server.configuration.VirtualThreadSchedulerConfiguration;
import in.aryaura.chess.engine.server.model.ChatRequest;
import in.aryaura.chess.engine.server.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class Chat {

    private final ChatService chatService;

    public Chat(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/start")
    public ResponseEntity<java.lang.String> get() {
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }

    @PostMapping(produces = "text/event-stream")
    public Flux<String> stream(@RequestBody ChatRequest request) {
        return chatService
                .getResponse(request)
                .subscribeOn(VirtualThreadSchedulerConfiguration.vtScheduler);
    }

    @DeleteMapping(value = "/{chatId}")
    public Mono<Void> delete(@PathVariable("chatId") @org.hibernate.validator.constraints.UUID UUID chatId) {
        return chatService
                .clear(chatId)
                .subscribeOn(VirtualThreadSchedulerConfiguration.vtScheduler);
    }
}
