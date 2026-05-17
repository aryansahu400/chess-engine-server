package in.aryaura.chess.engine.server.validation;

import in.aryaura.chess.engine.server.annotations.ValidFen;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class FenValidator implements ConstraintValidator<ValidFen, String> {

    private boolean strict;

    @Override
    public void initialize(ValidFen annotation) {
        this.strict = annotation.strict();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String[] parts = value.trim().split("\\s+");
        if (parts.length != 6) {
            return false;
        }

        String board = parts[0];
        String activeColor = parts[1];
        String castling = parts[2];
        String enPassant = parts[3];
        String halfmoveClock = parts[4];
        String fullmoveNumber = parts[5];

        if (!isValidBoard(board)) return false;
        if (!isValidActiveColor(activeColor)) return false;
        if (!isValidCastlingRights(castling)) return false;
        if (!isValidEnPassant(enPassant)) return false;
        if (!isNonNegativeInteger(halfmoveClock)) return false;
        if (!isPositiveInteger(fullmoveNumber)) return false;

        if (strict) {
            if (!hasExactlyOneKingEach(board)) return false;
            if (!hasValidPawnPlacement(board)) return false;
            if (!hasReasonablePawnCount(board)) return false;
        }

        return true;
    }

    private boolean isValidBoard(String board) {
        String[] ranks = board.split("/");
        if (ranks.length != 8) {
            return false;
        }

        for (String rank : ranks) {
            int squares = 0;

            for (int i = 0; i < rank.length(); i++) {
                char c = rank.charAt(i);

                if (Character.isDigit(c)) {
                    int empty = c - '0';
                    if (empty < 1 || empty > 8) {
                        return false;
                    }
                    squares += empty;
                } else if (isPieceChar(c)) {
                    squares += 1;
                } else {
                    return false;
                }
            }

            if (squares != 8) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidActiveColor(String activeColor) {
        return "w".equals(activeColor) || "b".equals(activeColor);
    }

    private boolean isValidCastlingRights(String castling) {
        if ("-".equals(castling)) {
            return true;
        }

        Set<Character> seen = new HashSet<>();
        for (int i = 0; i < castling.length(); i++) {
            char c = castling.charAt(i);
            if ("KQkq".indexOf(c) == -1) {
                return false;
            }
            if (!seen.add(c)) {
                return false; // no duplicates
            }
        }
        return true;
    }

    private boolean isValidEnPassant(String enPassant) {
        if ("-".equals(enPassant)) {
            return true;
        }

        if (enPassant.length() != 2) {
            return false;
        }

        char file = enPassant.charAt(0);
        char rank = enPassant.charAt(1);

        return file >= 'a' && file <= 'h' && (rank == '3' || rank == '6');
    }

    private boolean isNonNegativeInteger(String value) {
        try {
            return Integer.parseInt(value) >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isPositiveInteger(String value) {
        try {
            return Integer.parseInt(value) >= 1;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean hasExactlyOneKingEach(String board) {
        int whiteKings = 0;
        int blackKings = 0;

        for (int i = 0; i < board.length(); i++) {
            char c = board.charAt(i);
            if (c == 'K') whiteKings++;
            if (c == 'k') blackKings++;
        }

        return whiteKings == 1 && blackKings == 1;
    }

    private boolean hasValidPawnPlacement(String board) {
        String[] ranks = board.split("/");

        for (int row = 0; row < ranks.length; row++) {
            String rank = ranks[row];
            int boardRank = 8 - row; // 8..1

            for (int i = 0; i < rank.length(); i++) {
                char c = rank.charAt(i);
                if (c == 'P' || c == 'p') {
                    if (boardRank == 1 || boardRank == 8) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean hasReasonablePawnCount(String board) {
        int whitePawns = 0;
        int blackPawns = 0;

        for (int i = 0; i < board.length(); i++) {
            char c = board.charAt(i);
            if (c == 'P') whitePawns++;
            if (c == 'p') blackPawns++;
        }

        return whitePawns <= 8 && blackPawns <= 8;
    }

    private boolean isPieceChar(char c) {
        return "prnbqkPRNBQK".indexOf(c) >= 0;
    }
}
