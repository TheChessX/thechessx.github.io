package data;


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
    private byte[][] position; //indexed 0 to 7
    private boolean blackToMove;
    ArrayList moveList = new ArrayList<Move>();

    public Position() { // initializes starting position
        position = new byte[8][8];
        position = inputStartingPieces();
        setBlackToMove(false);
    }

    public Position(Position p) {
        position = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                position[i][j] = p.position[i][j];
            }
        }
        setBlackToMove(p.isBlackToMove());
    }

    public void setSquare(int xPos, int yPos, byte value) {
        position[xPos][yPos] = value;
    }

    public byte getSquare(int xPos, int yPos) {
        return position[xPos][yPos];
    }

    public ArrayList<Move> getAllLegalMoves() {
        moveList = new ArrayList<Move>();
    	if (moveList.size() == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (position[i][j] != 0) {
                        addMovesForPiece(i, j);
                    }
                }
            }
        }
        return moveList;
    }

    public boolean isLegalMove(Move move) {
        for (Move m : getAllLegalMoves()) {
            if (m.equals(move)) {
                return true;
            }
        }
        return false;
    }

    private void addMovesForPiece(int xPos, int yPos) {
        byte pieceNum = position[xPos][yPos];
        if (!blackToMove) {
        	if (pieceNum == 1) {
                addMovesWhitePawn(xPos, yPos);
            } else if (pieceNum == 2) {
                addMovesWhiteKnight(xPos, yPos);
            } else if (pieceNum == 3) {
                addMovesWhiteBishop(xPos, yPos);
            } else if (pieceNum == 4) {
                addMovesWhiteRook(xPos, yPos);
            } else if (pieceNum == 5) {
                addMovesWhiteQueen(xPos, yPos);
            } else if (pieceNum == 6) {
                addMovesWhiteKing(xPos, yPos);
            } 
        } else {
	        if (pieceNum == 7) {
	            addMovesBlackPawn(xPos, yPos);
	        } else if (pieceNum == 8) {
	            addMovesBlackKnight(xPos, yPos);
	        } else if (pieceNum == 9) {
	            addMovesBlackBishop(xPos, yPos);
	        } else if (pieceNum == 10) {
	            addMovesBlackRook(xPos, yPos);
	        } else if (pieceNum == 11) {
	            addMovesBlackQueen(xPos, yPos);
	        } else if (pieceNum == 12) {
	            addMovesBlackKing(xPos, yPos);
	        }
        }
    }

    private void addMovesWhitePawn(int xPos, int yPos) { // todo add em passant, promotion
    	if (xPos > 0 && position[xPos - 1][yPos] == 0) { // move forward 1
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos));
        }
        if (xPos == 6 && position[xPos - 1][yPos] == 0 && position[xPos - 2][yPos] == 0) { // moving forward by 2
            moveList.add(new Move(xPos, yPos, xPos - 2, yPos));
        }
        if (yPos != 7 && xPos > 0 && position[xPos - 1][yPos + 1] >= 7) { // one capture
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos + 1));
        }
        if (xPos != 0 && yPos != 0 && position[xPos - 1][yPos - 1] >= 7) { // other capture
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos - 1));
        }
    }

    private void addMovesWhiteKnight(int xPos, int yPos) { // adds eight possible knight moves, if legal
        if (xPos >= 2 && yPos != 0 && (position[xPos - 2][yPos - 1] == 0 || position[xPos - 2][yPos - 1] >= 7)) { // x - 2, y - 1
            moveList.add(new Move(xPos, yPos, xPos - 2, yPos - 1));
        }
        if (xPos >= 2 && yPos != 7 && (position[xPos - 2][yPos + 1] == 0 || position[xPos - 2][yPos + 1] >= 7)) { // x - 2, y + 1
            moveList.add(new Move(xPos, yPos, xPos - 2, yPos + 1));
        }
        if (xPos != 0 && yPos >= 2 && (position[xPos - 1][yPos - 2] == 0 || position[xPos - 1][yPos - 2] >= 7)) { // x - 1, y - 2
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos - 2));
        }
        if (xPos != 0 && yPos <= 5 && (position[xPos - 1][yPos + 2] == 0 || position[xPos - 1][yPos + 2] >= 7)) { // x - 1, y + 2
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos + 2));
        }
        if (xPos < 6 && yPos != 0 && (position[xPos + 2][yPos - 1] == 0 || position[xPos + 2][yPos - 1] >= 7)) { // x - 2, y - 1
            moveList.add(new Move(xPos, yPos, xPos + 2, yPos - 1));
        }
        if (xPos < 6 && yPos != 7 && (position[xPos + 2][yPos + 1] == 0 || position[xPos + 2][yPos + 1] >= 7)) { // x - 2, y + 1
            moveList.add(new Move(xPos, yPos, xPos + 2, yPos + 1));
        }
        if (xPos != 7 && yPos >= 2 && (position[xPos + 1][yPos - 2] == 0 || position[xPos + 1][yPos - 2] >= 7)) { // x - 1, y - 2
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 2));
        }
        if (xPos != 7 && yPos <= 5 && (position[xPos + 1][yPos + 2] == 0 || position[xPos + 1][yPos + 2] >= 7)) { // x - 1, y + 2
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 2));
        }
    }

    private void addMovesWhiteBishop(int xPos, int yPos) {
        int currentx = xPos;
        int currenty = yPos;
        boolean canMoveRightUp = true;
        while (canMoveRightUp) {
            canMoveRightUp = false;
            if (currentx < 7 && currenty < 7 && (position[currentx + 1][currenty + 1] == 0 || position[currentx + 1][currenty + 1] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx + 1, currenty + 1));
                canMoveRightUp = true;
                currentx++;
                currenty++;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveRightDown = true;
        while (canMoveRightDown) {
            canMoveRightDown = false;
            if (currentx < 7 && currenty > 0 && (position[currentx + 1][currenty - 1] == 0 || position[currentx + 1][currenty - 1] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx + 1, currenty - 1));
                canMoveRightDown = true;
                currentx++;
                currenty--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveLeftUp = true;
        while (canMoveLeftUp) {
            canMoveLeftUp = false;
            if (currentx > 0 && currenty < 7 && (position[currentx - 1][currenty + 1] == 0 || position[currentx - 1][currenty + 1] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx - 1, currenty + 1));
                canMoveLeftUp = true;
                currentx--;
                currenty++;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveLeftDown = true;
        while (canMoveLeftDown) {
            canMoveLeftDown = false;
            if (currentx > 0 && currenty > 0 && (position[currentx - 1][currenty - 1] == 0 || position[currentx - 1][currenty - 1] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx - 1, currenty - 1));
                canMoveLeftDown = true;
                currentx--;
                currenty--;
            }
        }
    }

    private void addMovesWhiteRook(int xPos, int yPos) {
        int currentx = xPos;
        int currenty = yPos;
        boolean canMoveRight = true;
        while (canMoveRight) {
            canMoveRight = false;
            if (currentx < 7 && (position[currentx + 1][currenty] == 0 || position[currentx + 1][currenty] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx + 1, currenty));
                canMoveRight = true;
                currentx++;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveLeft = true;
        while (canMoveLeft) {
            canMoveLeft = false;
            if (currentx > 0 && (position[currentx - 1][currenty] == 0 || position[currentx - 1][currenty] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx - 1, currenty));
                canMoveLeft = true;
                currentx--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveUp = true;
        while (canMoveUp) {
            canMoveUp = false;
            if (currenty > 0 && (position[currentx][currenty - 1] == 0 || position[currentx][currenty - 1] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx, currenty - 1));
                canMoveUp = true;
                currenty--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveDown = true;
        while (canMoveDown) {
            canMoveDown = false;
            if (currenty < 7 && (position[currentx][currenty + 1] == 0 || position[currentx][currenty + 1] >= 7)) {
                moveList.add(new Move(xPos, yPos, currentx, currenty + 1));
                canMoveDown = true;
                currenty++;
            }
        }
    }

    private void addMovesWhiteQueen(int xPos, int yPos) {
        addMovesWhiteBishop(xPos, yPos);
        addMovesWhiteRook(xPos, yPos);
    }

    private void addMovesWhiteKing(int xPos, int yPos) { // adds 8 king moves, if legal. TODO prevent king from entering check.
        // move up
        if (xPos != 0 && yPos != 0 && (position[xPos - 1][yPos - 1] == 0 || position[xPos - 1][yPos - 1] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos - 1));
        }
        if (xPos != 0 && (position[xPos - 1][yPos] == 0 || position[xPos - 1][yPos] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos));
        }
        if (xPos != 0 && yPos != 7 && (position[xPos - 1][yPos + 1] == 0 || position[xPos - 1][yPos + 1] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos + 1));
        }
        // move horizontally
        if (yPos != 0 && (position[xPos][yPos - 1] == 0 || position[xPos][yPos - 1] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos, yPos - 1));
        }
        if (yPos != 7 && (position[xPos][yPos + 1] == 0 || position[xPos][yPos + 1] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos, yPos + 1));
        }
        // move down
        if (xPos != 7 && yPos != 0 && (position[xPos + 1][yPos - 1] == 0 || position[xPos + 1][yPos - 1] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 1));
        }
        if (xPos != 7 && (position[xPos + 1][yPos] == 0 || position[xPos + 1][yPos] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos));
        }
        if (xPos != 7 && yPos != 7 && (position[xPos + 1][yPos + 1] == 0 || position[xPos + 1][yPos + 1] >= 7)) {
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 1));
        }
    }

    private void addMovesBlackPawn(int xPos, int yPos) { // todo add em passant, promotion
        if (xPos < 7 && position[xPos + 1][yPos] == 0) { // move forward 1
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos));
        }
        if (xPos == 1 && position[xPos + 1][yPos] == 0 && position[xPos + 2][yPos] == 0) { // moving forward by 2
            moveList.add(new Move(xPos, yPos, xPos + 2, yPos));
        }
        if (yPos != 7 && xPos != 7 && position[xPos + 1][yPos + 1] <= 6 && position[xPos + 1][yPos + 1] != 0) { // one capture
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 1));
        }
        if (yPos != 7 && yPos != 0 && xPos < 7 && position[xPos + 1][yPos - 1] <= 6 && position[xPos + 1][yPos - 1] != 0) { // other capture
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 1));
        }
    }

    private void addMovesBlackKnight(int xPos, int yPos) {
        if (xPos >= 2 && yPos != 0 && position[xPos - 2][yPos - 1] <= 6) { // x - 2, y - 1
            moveList.add(new Move(xPos, yPos, xPos - 2, yPos - 1));
        }
        if (xPos >= 2 && yPos != 7 && position[xPos - 2][yPos + 1] <= 6) { // x - 2, y + 1
            moveList.add(new Move(xPos, yPos, xPos - 2, yPos + 1));
        }
        if (xPos != 0 && yPos >= 2 && position[xPos - 1][yPos - 2] <= 6) { // x - 1, y - 2
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos - 2));
        }
        if (xPos != 0 && yPos <= 5 && position[xPos - 1][yPos + 2] <= 6) { // x - 1, y + 2
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos + 2));
        }
        if (xPos < 6 && yPos != 0 && position[xPos + 2][yPos - 1] <= 6) { // x - 2, y - 1
            moveList.add(new Move(xPos, yPos, xPos + 2, yPos - 1));
        }
        if (xPos < 6 && yPos != 7 && position[xPos + 2][yPos + 1] <= 6) { // x - 2, y + 1
            moveList.add(new Move(xPos, yPos, xPos + 2, yPos + 1));
        }
        if (xPos != 7 && yPos >= 2 && position[xPos + 1][yPos - 2] <= 6) { // x - 1, y - 2
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 2));
        }
        if (xPos != 7 && yPos <= 5 && position[xPos + 1][yPos + 2] <= 6) { // x - 1, y + 2
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 2));
        }
    }

    private void addMovesBlackBishop(int xPos, int yPos) {
        int currentx = xPos;
        int currenty = yPos;
        boolean canMoveRightUp = true;
        while (canMoveRightUp) {
            canMoveRightUp = false;
            if (currentx < 7 && currenty < 7 && (position[currentx + 1][currenty + 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx + 1, currenty + 1));
                canMoveRightUp = true;
                currentx++;
                currenty++;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveRightDown = true;
        while (canMoveRightDown) {
            canMoveRightDown = false;
            if (currentx < 7 && currenty > 0 && (position[currentx + 1][currenty - 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx + 1, currenty - 1));
                canMoveRightDown = true;
                currentx++;
                currenty--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveLeftUp = true;
        while (canMoveLeftUp) {
            canMoveLeftUp = false;
            if (currentx > 0 && currenty < 7 && (position[currentx - 1][currenty + 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx - 1, currenty + 1));
                canMoveLeftUp = true;
                currentx--;
                currenty++;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveLeftDown = true;
        while (canMoveLeftDown) {
            canMoveLeftDown = false;
            if (currentx > 0 && currenty > 0 && (position[currentx - 1][currenty - 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx - 1, currenty - 1));
                canMoveLeftDown = true;
                currentx--;
                currenty--;
            }
        }
    }

    private void addMovesBlackRook(int xPos, int yPos) {
        int currentx = xPos;
        int currenty = yPos;
        boolean canMoveRight = true;
        while (canMoveRight) {
            canMoveRight = false;
            if (currentx < 7 && (position[currentx + 1][currenty] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx + 1, currenty));
                canMoveRight = true;
                currentx++;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveLeft = true;
        while (canMoveLeft) {
            canMoveLeft = false;
            if (currentx > 0 && (position[currentx - 1][currenty] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx - 1, currenty));
                canMoveLeft = true;
                currentx--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveUp = true;
        while (canMoveUp) {
            canMoveUp = false;
            if (currenty > 0 && (position[currentx][currenty - 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx, currenty - 1));
                canMoveUp = true;
                currenty--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveDown = true;
        while (canMoveDown && currenty < 7) {
            canMoveDown = false;
            if (currentx < 7 && (position[currentx][currenty + 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx, currenty + 1));
                canMoveDown = true;
                currenty++;
            }
        }
    }

    private void addMovesBlackQueen(int xPos, int yPos) {
        addMovesBlackBishop(xPos, yPos);
        addMovesBlackRook(xPos, yPos);
    }

    private void addMovesBlackKing(int xPos, int yPos) {
        // move up
        if (xPos != 0 && yPos != 0 && position[xPos - 1][yPos - 1] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos - 1));
        }
        if (xPos != 0 && position[xPos - 1][yPos] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos));
        }
        if (xPos != 0 && yPos != 7 && position[xPos - 1][yPos + 1] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos - 1, yPos + 1));
        }
        // move horizontally
        if (yPos != 0 && position[xPos][yPos - 1] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos, yPos - 1));
        }
        if (yPos != 7 && position[xPos][yPos + 1] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos, yPos + 1));
        }
        // move down
        if (xPos != 7 && yPos != 0 && position[xPos + 1][yPos - 1] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 1));
        }
        if (xPos != 7 && position[xPos + 1][yPos] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos));
        }
        if (xPos != 7 && yPos != 7 && position[xPos + 1][yPos + 1] <= 6) {
            moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 1));
        }
    }


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
    }

	public boolean isBlackToMove() {
		return blackToMove;
	}

	public void setBlackToMove(boolean blackToMove) {
		this.blackToMove = blackToMove;
	}
}