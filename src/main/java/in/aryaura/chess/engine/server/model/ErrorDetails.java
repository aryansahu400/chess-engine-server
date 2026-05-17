package in.aryaura.chess.engine.server.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDetails {
    private String branch;
    private String commitHead;
    private String transactionId;
    private String serviceName;
    private String timestamp;
    private String message;
    private String className;
    private String methodName;
    private Integer lineNumber;
    private String cause;
    private String stackTrace;

}
