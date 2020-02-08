import chessLogic.*;

public class TestPlay {
    Engine firstEngine = new Engine();
    Engine secondEngine = new Engine();

    public Move play(int engineNum, Position pos) {
        System.out.println("TestPlay called: " + engineNum);
        if (engineNum == 0) {
            return firstEngine.play(pos);
        }
        if (engineNum == 1) {
            return secondEngine.play(pos);
        }
        throw new IndexOutOfBoundsException("EngineNum is not 0 or 1");
    }
}
