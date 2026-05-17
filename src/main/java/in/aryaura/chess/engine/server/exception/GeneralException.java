package in.aryaura.chess.engine.server.exception;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException{
    HttpStatus httpStatus;

    public GeneralException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public GeneralException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
