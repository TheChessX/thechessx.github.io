package chessLogic;


public class Move {
    private final int xInitial;
    private final int xFinal;
    private final int yInitial;
    private final int yFinal;
    
    private final byte promotionID;
    
    private double score;

    public Move(int xInitial, int yInitial, int xFinal, int yFinal) {
        this.xInitial = xInitial;
        this.xFinal = xFinal;
        this.yInitial = yInitial;
        this.yFinal = yFinal;
        this.promotionID = 0;
    }
    
    public Move(int xInitial, int yInitial, int xFinal, int yFinal, byte promotionID) {
        this.xInitial = xInitial;
        this.xFinal = xFinal;
        this.yInitial = yInitial;
        this.yFinal = yFinal;
        this.promotionID = promotionID;
    }

    public int getxInitial() {
        return xInitial;
    }

    public int getxFinal() {
        return xFinal;
    }

    public int getyInitial() {
        return yInitial;
    }

    public int getyFinal() {
        return yFinal;
    }

    @Override
    public String toString() { // ASCII 'a' is 97
        StringBuilder standardNotation = new StringBuilder();
//        standardNotation.append((char) (yInitial + 97));
//        standardNotation.append(9 - (xInitial + 1));
//        standardNotation.append(" to ");
        standardNotation.append((char) (yFinal + 97));
        standardNotation.append(9 - (xFinal + 1));
        if (this.promotionID != 0) {
        	standardNotation.append("=");
        	if (promotionID == 2 || promotionID == 8) {
        		standardNotation.append("N");
        	} else if (promotionID == 3 || promotionID == 9) {
        		standardNotation.append("B");
        	} else if (promotionID == 4 || promotionID == 10) {
        		standardNotation.append("R");
        	} else {
        		standardNotation.append("Q");
        	}
        }
        return standardNotation.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        if (((Move)obj).xInitial == xInitial && ((Move)obj).yInitial == yInitial && ((Move)obj).xFinal == xFinal && ((Move)obj).yFinal == yFinal) {
            return true;
        } else {
            return false;
        }
    }
    
    public byte getPromotionID() {
    	return promotionID;
    }
    
    public double getScore() {
    	return score;
    }
    
    public void setScore(double score) {
    	this.score = score;
    }
}