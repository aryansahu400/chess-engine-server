package in.aryaura.chess.engine.server.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuggestMoveResponse {
    private String move;
    private double evaluation;
    private String explanation;
}
