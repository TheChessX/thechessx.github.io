package chessLogic;

public class MoveAndExplanation {
    private String explanation;
    private Move move;

    public MoveAndExplanation(Move move, String explanation) {
        this.move = move;
        this.explanation = explanation;
    }
    public MoveAndExplanation(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public String getExplanation() {
        return explanation;
    }
}
