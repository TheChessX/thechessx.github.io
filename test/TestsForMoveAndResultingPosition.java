import chessLogic.Evaluation;
import chessLogic.Move;
import chessLogic.MoveAndResultingPosition;
import chessLogic.Position;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;

public class TestsForMoveAndResultingPosition {
    private ArrayList<Position> positions = new ArrayList<>();
    private ArrayList<MoveAndResultingPosition> moveAndResultingPositions = new ArrayList<>();
    private Evaluation evaluation = new Evaluation();
    private double EPSILON = 0.0001;


    TestsForMoveAndResultingPosition() {
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
        for (Move m : positions.get(11).getAllLegalMoves()) {
            moveAndResultingPositions.add(new MoveAndResultingPosition(m, positions.get(11).positionAfterMove(m)));
        }
    }

    @org.junit.jupiter.api.Test
    public void testComparator(){
        Collections.sort(moveAndResultingPositions);
        Assert.assertEquals(new Move(7, 5, 7, 1), moveAndResultingPositions.get(0).getMove());

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
            {10, 8, 9, 11, 12, 0, 8, 10},
            {7, 7, 7, 9, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 7, 7, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 0},
            {4, 0, 3, 5, 6, 3, 2, 4}
    };
    private byte[][] Position11 = {
            {0, 0, 0, 0, 0, 10, 12, 0},
            {0, 0, 0, 0, 0, 7, 7, 7},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {0, 11, 0, 0, 0, 4, 6, 0},
    };
}
