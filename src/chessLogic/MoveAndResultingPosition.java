package chessLogic;

import org.junit.Test;

public class MoveAndResultingPosition implements Comparable<MoveAndResultingPosition> {
    private Move move;
    private Position pos;
    private Double score = null;
    private boolean blackToMove; // This variable stores who's move it was when this move was being considered, so it is the opposite of the side who's move it actually is in the stored position.
    private static TestEvaluation eval = new TestEvaluation();

    public MoveAndResultingPosition(Move m, Position p, boolean blackToMove) {
        move = m;
        pos = p;
        this.blackToMove = blackToMove;
    }

    @Override
    public int compareTo(MoveAndResultingPosition o) {
        int positionComparison = o.getPos().compareTo(pos);

        if (blackToMove) {
            if (positionComparison < 0) {
                return -1;
            } else if (positionComparison == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (positionComparison < 0) {
                return 1;
            } else if (positionComparison == 0) {
                return 0;
            } else {
                return -1;
            }
        }
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
    }

    public boolean isBlackToMove() {
        return blackToMove;
    }

    public void setBlackToMove(boolean blackToMove) {
        this.blackToMove = blackToMove;
    }

    public MoveAndResultingPosition clone() {
        MoveAndResultingPosition toReturn = new MoveAndResultingPosition(this.getMove(), this.getPos(), this.isBlackToMove());
        toReturn.setScore(this.getScore());
        return toReturn;
    }
}
