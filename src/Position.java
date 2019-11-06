import java.util.ArrayList;

public class Position {
    /*
    Byte[][] stores the current chess position. Correspondence of number to piece:
    0 - empty
    1 - white pawn
    2 - white knight
    3 - white bishop
    4 - white rook
    5 - white queen
    6 - white king
    7 - black pawn
    8 - black knight
    9 - black bishop
    10 - black rook
    11 - black queen
    12 - black king
    True - Black
    False - White

    Board is stored as x,y with top left being 0,0
     */
    byte[][] position; //indexed 0 to 7
    boolean blackToMove;

    public Position() { // initializes starting position
        position = inputStartingPieces();
        blackToMove = false;
    }
    public Position(Position p) {
        position = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                position[i][j] = p.position[i][j];
            }
        }
        blackToMove = p.blackToMove;
    }
    /*public ArrayList getAllLegalMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] != 0) {
                    // TODO finish
                }
            }
        }
    }
    */




    private byte[][] inputStartingPieces() {
    	return new byte[][] {
				{10, 8, 9, 11, 12, 9, 8, 10},
				{7, 7, 7, 7, 7, 7, 7, 7},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{4, 2, 3, 5, 6, 3, 2, 4}
			};
    	
    	/*
    	position[0][0] = 10; // back row
        position[1][0] = 8;
        position[2][0] = 9;
        position[3][0] = 11;
        position[4][0] = 12;
        position[5][0] = 9;
        position[6][0] = 8;
        position[7][0] = 10;

        position[0][1] = 7; // black pawns
        position[1][1] = 7;
        position[2][1] = 7;
        position[3][1] = 7;
        position[4][1] = 7;
        position[5][1] = 7;
        position[6][1] = 7;
        position[7][1] = 7;

        position[0][6] = 1; // white pawns
        position[1][6] = 1;
        position[2][6] = 1;
        position[3][6] = 1;
        position[4][6] = 1;
        position[5][6] = 1;
        position[6][6] = 1;
        position[7][6] = 1;

        position[0][7] = 4; // front row
        position[1][7] = 2;
        position[2][7] = 3;
        position[3][7] = 6;
        position[4][7] = 5;
        position[5][7] = 3;
        position[6][7] = 2;
        position[7][7] = 4;
        */
    }
}
