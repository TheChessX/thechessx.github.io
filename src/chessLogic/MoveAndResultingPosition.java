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
        double pos1Score = pos.getScore();
        if (pos1Score == Double.MAX_VALUE) {
            pos1Score = eval.evaluate(pos);
        }
        if (score != null) {
            pos1Score = score;
        }
        double pos2Score = o.getPos().getScore();
        if (pos2Score == Double.MAX_VALUE) {
            pos2Score = eval.evaluate(o.getPos());
        }
        if (o.getScore() != null) {
            pos2Score = o.getScore();
        }

        if (blackToMove) {
            if (pos2Score > pos1Score) {
                return -1;
            } else if (pos2Score == pos1Score) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (pos2Score > pos1Score) {
                return 1;
            } else if (pos2Score == pos1Score) {
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
        return score;
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
}
