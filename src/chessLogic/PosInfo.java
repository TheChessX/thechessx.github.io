package chessLogic;

public class PosInfo {
    private Position pos;
    private double score;
    private int depthSearched;

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
}