package chessLogic;

public class MoveAndResultingPosition implements Comparable<MoveAndResultingPosition> {
    private Move move;
    private Position pos;
    private int score;

    public MoveAndResultingPosition(Move m, Position p) {
        move = m;
        pos = p;
    }

    @Override
    public int compareTo(MoveAndResultingPosition o) {
        return pos.compareTo(o.getPos());
    }

    public Position getPos() {
        return pos;
    }

    public Move getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
