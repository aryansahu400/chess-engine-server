package in.aryaura.chess.engine.server.service;

import in.aryaura.chess.engine.server.model.ChatRequest;
import in.aryaura.chess.engine.server.prompt.SystemPrompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private ChatMemory chatMemory;

    @Autowired
    public ChatService(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    public Flux<String> getResponse(ChatRequest request) {
        return chatClient
                .prompt(SystemPrompts.CHAT_PROMPT.formatted(request.getFen()))
                .user(request.getPrompt())
                .advisors(
                        MessageChatMemoryAdvisor
                                .builder(chatMemory)
                                .conversationId(request.getConversationId().toString())
                                .build()
                )
                .stream()
                .content();
    }

    public Mono<Void> clear(UUID chatId) {
        chatMemory.clear(chatId.toString());
        return Mono.empty();
    }
}
