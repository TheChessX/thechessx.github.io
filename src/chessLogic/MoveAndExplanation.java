package chessLogic;

public class MoveAndExplanation {
    private String explanation;
    private Move move;

    MoveAndExplanation(Move move, String explanation) {
        this.move = move;
        this.explanation = explanation;
    }

    public Move getMove() {
        return move;
    }

    public String getExplanation() {
        return explanation;
    }
}
