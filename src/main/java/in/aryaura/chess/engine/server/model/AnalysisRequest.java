package in.aryaura.chess.engine.server.model;

import in.aryaura.chess.engine.server.annotations.ValidFen;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisRequest {
    @ValidFen
    String fenBefore;
    @ValidFen
    String fenAfter;
}
