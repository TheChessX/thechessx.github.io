package chessLogic;

import java.util.ArrayList;

public class PosInfo {
    private Position pos;
    private double score;
    private int depthSearched;
    private ArrayList<MoveAndResultingPosition> orderedMoves;

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getDepthSearched() {
        return depthSearched;
    }

    public void setDepthSearched(int depthSearched) {
        this.depthSearched = depthSearched;
    }

    public ArrayList<MoveAndResultingPosition> getOrderedMoves() {
        return orderedMoves;
    }

    public void setOrderedMoves(ArrayList<MoveAndResultingPosition> orderedMoves) {
        this.orderedMoves = (ArrayList<MoveAndResultingPosition>) orderedMoves.clone();
    }
}