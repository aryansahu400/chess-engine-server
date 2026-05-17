package in.aryaura.chess.engine.server.model;

import lombok.Builder;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Data
public class ErrorResponse {
    String version;
    ZonedDateTime timestamp;
    String transactionId;
    String message;
}
