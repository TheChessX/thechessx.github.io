public class Move {
    final int xInitial;
    final int xFinal;
    final int yInitial;
    final int yFinal;

    public Move(int yInitial, int xInitial, int yFinal, int xFinal) {
        this.xInitial = xInitial;
        this.xFinal = xFinal;
        this.yInitial = yInitial;
        this.yFinal = yFinal;
    }

    @Override
    public String toString() { // ASCII 'a' is 97
        StringBuilder standardNotation = new StringBuilder();
        standardNotation.append((char) (xInitial + 97));
        standardNotation.append(8 - yInitial);
        standardNotation.append(" to ");
        standardNotation.append((char) (xFinal + 97));
        standardNotation.append(8 - yFinal);
        return standardNotation.toString();
    }
}
