package in.aryaura.chess.engine.server.util;

import in.aryaura.chess.engine.server.configuration.GitDetails;
import in.aryaura.chess.engine.server.model.ErrorDetails;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class ErrorLoggingUtil {
    public static ErrorDetails getNormalizedObject(Throwable throwable, String transactionId){
        var errorDetails = ErrorDetails.builder()
                .branch(GitDetails.BRANCH)
                .commitHead(GitDetails.COMMIT_ID)
                .transactionId(transactionId)
                .serviceName("Chess Engine")
                .message(throwable.getMessage())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()).toString())
                .build();
        var stack = throwable.getStackTrace();
        var exceptionsList = new LinkedList<String>();
        List.of(stack).forEach(stackelement -> {
            if(errorDetails.getClassName() ==null && stackelement.toString().contains("in.aryaura.chess.engine.server")) {
                errorDetails.setClassName(stackelement.getClassName());
                errorDetails.setLineNumber(stackelement.getLineNumber());
                errorDetails.setMethodName(stackelement.getMethodName());
            }
            exceptionsList.add(stackelement.toString());
        });
        errorDetails.setStackTrace(exceptionsList.toString());

        return errorDetails;
    }
}
