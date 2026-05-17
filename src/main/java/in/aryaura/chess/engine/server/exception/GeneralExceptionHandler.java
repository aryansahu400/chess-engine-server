package in.aryaura.chess.engine.server.exception;

import in.aryaura.chess.engine.server.configuration.GitDetails;
import in.aryaura.chess.engine.server.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.ZonedDateTime;


@RestControllerAdvice
public class GeneralExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionHandler.class);
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(GeneralException ex, ServerWebExchange exchange) {

        LOGGER.error("General Exception: {}", ex.getMessage());

        var errorResponse = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now())
                .transactionId(exchange.getRequest().getId())
                .version(GitDetails.BRANCH+"-"+GitDetails.COMMIT_ID)
                .build();
        return ResponseEntity.status(ex.httpStatus).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, ServerWebExchange exchange){
        LOGGER.error("Exception: {}", ex.getMessage());
        var errorResponse = ErrorResponse
                .builder()
                .message("Internal Server Error")
                .timestamp(ZonedDateTime.now())
                .transactionId(exchange.getRequest().getId())
                .version(GitDetails.BRANCH+"-"+GitDetails.COMMIT_ID)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
