package chessLogic;

public class MoveAndResultingPosition implements Comparable<MoveAndResultingPosition> {
    private Move move;
    private Position pos;
    private Double score = null;
    private static TestEvaluation eval = new TestEvaluation();

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

    public Double getScore() {
        if (score != null) {
            return score;
        } else {
            return eval.evaluate(pos);
        }
    }

    public void setScore(double score) {
        this.score = score;
        pos.setScore(score);
    }


    public MoveAndResultingPosition clone() {
        MoveAndResultingPosition toReturn = new MoveAndResultingPosition(this.getMove(), this.getPos());
        toReturn.setScore(this.getScore());
        return toReturn;
    }
}
