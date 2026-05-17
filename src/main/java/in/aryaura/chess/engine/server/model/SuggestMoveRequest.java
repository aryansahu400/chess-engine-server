package in.aryaura.chess.engine.server.model;

import in.aryaura.chess.engine.server.annotations.ValidFen;
import lombok.Data;

@Data
public class SuggestMoveRequest {
    @ValidFen
    private String fen;
}

