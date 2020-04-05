package chessLogic;

public class PosInfo {
    private Position pos;
    private double score;
    private int depthSearched;
    private Move bestMove;

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

    public Move getBestMove() {
        return bestMove;
    }

    public void setBestMove(Move bestMove) {
        this.bestMove = bestMove;
    }
}