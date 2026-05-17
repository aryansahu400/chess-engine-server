package in.aryaura.chess.engine.server.model;

import in.aryaura.chess.engine.server.annotations.ValidFen;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatRequest {

    @org.hibernate.validator.constraints.UUID(message = "A valid chatId is required")
    UUID conversationId;
    @NotBlank(message = "prompt can not be blank")
    String prompt;
    @ValidFen
    String fen;
}

