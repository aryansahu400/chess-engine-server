package in.aryaura.chess.engine.server.controller;

import in.aryaura.chess.engine.server.model.SuggestMoveRequest;
import in.aryaura.chess.engine.server.service.SuggestionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class SuggestionController {

    private final SuggestionService suggestionService;
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping("/suggest-move")
    public Mono<Map<String,Object>> get(@RequestBody SuggestMoveRequest request) {
        return suggestionService.getMoveSuggestion(request.getFen());
    }
}
