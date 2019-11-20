package data;

public class Move {
    private final int xInitial;
    private final int xFinal;
    private final int yInitial;
    private final int yFinal;

    public Move(int xInitial, int yInitial, int xFinal, int yFinal) {
        this.xInitial = xInitial;
        this.xFinal = xFinal;
        this.yInitial = yInitial;
        this.yFinal = yFinal;
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
        standardNotation.append((char) (xInitial + 97));
        standardNotation.append(yInitial + 1);
        standardNotation.append(" to ");
        standardNotation.append((char) (xFinal + 97));
        standardNotation.append(yFinal + 1);
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
}