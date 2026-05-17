package in.aryaura.chess.engine.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisRequest {
    String fenBefore;
    String fenAfter;
}
