import chessLogic.TestEvaluation;
import chessLogic.Position;
import org.junit.Assert;

import java.util.ArrayList;

public class TestsForPosition {
    private ArrayList<Position> positions = new ArrayList<>();
    private double EPSILON = 0.0001;

    TestsForPosition() {
        setUp();
    }

    protected void setUp(){
        positions.add(new Position(Position0));
        positions.add(new Position(Position1));
        positions.add(new Position(Position2));
        positions.add(new Position(Position3));
        positions.add(new Position(Position4));
        positions.add(new Position(Position5));
        positions.add(new Position(Position6));
        positions.add(new Position(Position7));
        positions.add(new Position(Position8));
        positions.add(new Position(Position9));
        positions.add(new Position(Position10));
        positions.add(new Position(Position11));
        positions.add(new Position(Position12));
    }


    @org.junit.jupiter.api.Test
    public void testCapturesPossible(){
        Assert.assertEquals(positions.get(0), positions.get(12));
    }


    private byte[][] Position0 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 4, 6, 0},
    };
    private byte[][] Position1 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 3, 0},
            {0, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 4, 6, 0},
    };
    private byte[][] Position2 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 9, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 3, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 4, 6, 0},
    };
    private byte[][] Position3 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 9, 9, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 3, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 4, 6, 0},
    };
    private byte[][] Position4 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 9, 9, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {0, 3, 0, 0, 0, 4, 6, 0},
    };
    private byte[][] Position5 = { // Initial position
            {10, 8, 9, 11, 12, 9, 8, 10},
            {7, 7, 7, 7, 7, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {4, 2, 3, 5, 6, 3, 2, 4}
    };
    private byte[][] Position6 = {
            {10, 8, 9, 11, 12, 9, 8, 10},
            {7, 7, 7, 7, 7, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 0, 1, 1},
            {4, 2, 3, 5, 6, 3, 2, 4}
    };
    private byte[][] Position7 = { // Ruy Lopez
            {10, 0, 9, 11, 12, 9, 8, 10},
            {7, 7, 7, 7, 0, 7, 7, 7},
            {0, 0, 8, 0, 0, 0, 0, 0},
            {0, 3, 0, 0, 7, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 0},
            {1, 1, 1, 1, 0, 1, 1, 1},
            {4, 2, 3, 5, 6, 0, 0, 4}
    };
    private byte[][] Position8 = {
            {10, 8, 9, 11, 12, 9, 8, 10},
            {7, 7, 7, 7, 7, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 1, 1, 1},
            {4, 2, 3, 5, 6, 3, 2, 4}
    };
    private byte[][] Position9 = { // French Defense
            {10, 8, 9, 11, 12, 0, 8, 10},
            {7, 7, 0, 9, 0, 7, 7, 7},
            {0, 0, 0, 0, 7, 0, 0, 0},
            {0, 0, 7, 7, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 2, 3, 0, 0, 0, 0},
            {1, 1, 1, 0, 0, 1, 1, 1},
            {4, 0, 3, 5, 6, 0, 2, 4}
    };
    private byte[][] Position10 = {
            {10, 8, 9, 11, 12, 9, 0, 10},
            {7, 7, 7, 8, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 7, 7, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 0},
            {4, 0, 3, 5, 6, 3, 2, 4}
    };
    private byte[][] Position11 = {
            {10, 8, 9, 11, 12, 9, 8, 10},
            {7, 7, 7, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 7, 7, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 0},
            {4, 0, 3, 5, 6, 3, 2, 4}
    };
    private byte[][] Position12 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 4, 6, 0},
    };
}