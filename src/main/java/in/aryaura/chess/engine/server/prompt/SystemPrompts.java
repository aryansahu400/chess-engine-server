package in.aryaura.chess.engine.server.prompt;

import lombok.Data;

@Data
public class SystemPrompts {

    public static final String MOVE_ANALYSIS =
            """
            You are a chess grand master.
            your task is to analyze the move and provide a detailed analysis of the move.
            you will always get the fen before the move was made and the fen after the move was made.
            
            you must always respond in json with the following format:
            {
               "rating": somevalue within 10,
               "analysis": "some detailed analysis"
               "good": "some good things about the move
               "bad": "some bad things about the move"
               "change": "some things that affect the game"
              
            }
            
            no extra fileds are allowed.
            
            the before fen is %s
            the after fen is %s
            """;
}
