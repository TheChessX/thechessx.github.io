package chessLogic;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.swing.JOptionPane;

public class Position implements Comparable<Position> {


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
    ArrayList<Move> moveList = new ArrayList<Move>();
	ArrayList<Move> captureList = new ArrayList<Move>();
	ArrayList<Move> otherList = new ArrayList<Move>();
    
    private int enPassantColumn = -1;

    private boolean whiteCastleK = true;
    private boolean whiteCastleQ = true;
    private boolean blackCastleK = true;
    private boolean blackCastleQ = true;
    
    private boolean castleTest = false;
    
    private double score = Double.MAX_VALUE;

    public Position bestNextPosition = null;
    public Move bestNextMove = null;
    
    public Position() { // initializes starting position
        position = new byte[8][8];
        position = inputStartingPieces();
        //position = inputCustomPosition();
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
		captureList = new ArrayList<Move>();
		otherList = new ArrayList<Move>();
		if (moveList.size() == 0) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (position[i][j] != 0) {
						addMovesForPiece(i, j);
					}
				}
			}
		}
		//boolean legal = true;
		for (int i = 0; i < moveList.size(); i++) {
			//legal = true;
			Move potentialMov = (Move) moveList.get(i);
			Position potentialPos = new Position(this);
			boolean capture = false;
//			if (potentialPos.getSquare(potentialMov.getxFinal(), potentialMov.getyFinal()) % 6 >= potentialPos.getSquare(potentialMov.getxInitial(), potentialMov.getyInitial()) % 6 &&
//					!(potentialPos.getSquare(potentialMov.getxInitial(), potentialMov.getyInitial()) % 6 + potentialPos.getSquare(potentialMov.getxFinal(), potentialMov.getyFinal()) % 6 == 0)) {
//				capture = true;
//			}
			if (potentialPos.getSquare(potentialMov.getxFinal(), potentialMov.getyFinal()) != 0) {
				capture = true;
			}
			if (potentialMov.getPromotionID() != 0) {
				potentialPos.setSquare(potentialMov.getxFinal(), potentialMov.getyFinal(), potentialMov.getPromotionID());
			} else {
				potentialPos.setSquare(potentialMov.getxFinal(), potentialMov.getyFinal(), potentialPos.getSquare(potentialMov.getxInitial(), potentialMov.getyInitial()));
			}
			potentialPos.setSquare(potentialMov.getxInitial(), potentialMov.getyInitial(), (byte) 0);
			potentialPos.setBlackToMove(!this.blackToMove);
//    		int[] kingLocation = findKing(potentialPos);
//    		int kingR = kingLocation[0];
//    		int kingC = kingLocation[1];
//    		ArrayList<Move> potentialLegalMoves = potentialPos.getAllLegalMovesNoCheck();
//    		for (Move potentialNextMove: potentialLegalMoves) {
//    			if (potentialNextMove.getxFinal() == kingR && potentialNextMove.getyFinal() == kingC) {
//    				legal = false;
//    				break;
//    			}
//    		}
			if (inCheck(potentialPos)) {
				moveList.remove(i);
				i--;
			} else {
				if (capture) {
					captureList.add(potentialMov);
				} else {
					otherList.add(potentialMov);
				}
			}
		}
		return moveList;
	}


	public ArrayList<Move> getAllLegalMovesNoCheck() {
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

    public boolean isBlackToMove() {
		return blackToMove;
	}

	public void setBlackToMove(boolean blackToMove) {
		this.blackToMove = blackToMove;
	}
	
	public int[] findKing(Position pos) {
		byte targetKing;
		if (pos.isBlackToMove()) {
			targetKing = 6;
		} else {
			targetKing = 12;
		}
		int[] kingLocation = {-1, -1};
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (pos.getSquare(r, c) == targetKing) {
					kingLocation[0] = r;
					kingLocation[1] = c;
					return kingLocation;
				}
			}
		}
		return kingLocation;
	}
	
	public int[] findOwnKing() {
		byte targetKing;
		if (this.isBlackToMove()) {
			targetKing = 12;
		} else {
			targetKing = 6;
		}
		int[] kingLocation = {-1, -1};
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (this.getSquare(r, c) == targetKing) {
					kingLocation[0] = r;
					kingLocation[1] = c;
					return kingLocation;
				}
			}
		}
		return kingLocation;
	}
	
	public void setEnPassantColumn(int c) {
		this.enPassantColumn = c;
	}

	public boolean isWhiteCastleQ() {
		return whiteCastleQ;
	}

	public void setWhiteCastleQ(boolean whiteCastleQ) {
		this.whiteCastleQ = whiteCastleQ;
	}

	public boolean isWhiteCastleK() {
		return whiteCastleK;
	}

	public void setWhiteCastleK(boolean whiteCastleK) {
		this.whiteCastleK = whiteCastleK;
	}

	public boolean isBlackCastleQ() {
		return blackCastleQ;
	}

	public void setBlackCastleQ(boolean blackCastleQ) {
		this.blackCastleQ = blackCastleQ;
	}

	public boolean isBlackCastleK() {
		return blackCastleK;
	}

	public void setBlackCastleK(boolean blackCastleK) {
		this.blackCastleK = blackCastleK;
	}
	
	public boolean inCheck(Position pos) {
		int[] kingLocation = findKing(pos);
		int kingR = kingLocation[0];
		int kingC = kingLocation[1];
		ArrayList<Move> potentialLegalMoves = pos.getAllLegalMovesNoCheck();
		for (Move potentialNextMove: potentialLegalMoves) {
			if (potentialNextMove.getxFinal() == kingR && potentialNextMove.getyFinal() == kingC) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inCheck() {
		int[] kingLocation = findOwnKing();
		int kingR = kingLocation[0];
		int kingC = kingLocation[1];
		this.blackToMove = !this.blackToMove;
		ArrayList<Move> potentialLegalMoves = this.getAllLegalMovesNoCheck();
		this.blackToMove = !this.blackToMove;
		for (Move potentialNextMove: potentialLegalMoves) {
			if (potentialNextMove.getxFinal() == kingR && potentialNextMove.getyFinal() == kingC) {
				return true;
			}
		}
		return false;
	}
	
	public Position positionAfterMove(Move mov) {
		Position pos = new Position(this);
		pos.whiteCastleK = this.whiteCastleK;
		pos.whiteCastleQ = this.whiteCastleQ;
		pos.blackCastleK = this.blackCastleK;
		pos.blackCastleQ = this.blackCastleQ;
		
		int rI = mov.getxInitial();
		int cI = mov.getyInitial();
		int rF = mov.getxFinal();
		int cF = mov.getyFinal();
		
		if (rI == 7 && cI == 4) {
			pos.setWhiteCastleK(false);
			pos.setWhiteCastleQ(false);
		} else if (rI == 0 && cI == 4) {
			pos.setBlackCastleK(false);
			pos.setBlackCastleQ(false);
		} else if (rI == 7 && cI == 0) {
			pos.setWhiteCastleQ(false);
		} else if (rI == 7 && cI == 7) {
			pos.setWhiteCastleK(false);
		} else if (rI == 0 && cI == 0) {
			pos.setBlackCastleQ(false);
		} else if (rI == 0 && cI == 7) {
			pos.setBlackCastleK(false);
		}
		
		if ((pos.getSquare(rI, cI) == 1 && rI == 6 && rF == 4)||(pos.getSquare(rI, cI) == 7 && rI == 1 && rF == 3)) {
			pos.setEnPassantColumn(cF);
		} else {
			pos.setEnPassantColumn(-1);
		}
		
		if (mov.getPromotionID() != 0) {
			pos.setSquare(rF, cF, mov.getPromotionID());
		} else {
			if ((pos.getSquare(rI, cI) == 1 && rI == 3 && rF == 2 && cI != cF && pos.getSquare(rF, cF) == 0)||(pos.getSquare(rI, cI) == 7 && rI == 4 && rF == 5 && cI != cF && pos.getSquare(rF, cF) == 0)) {
				pos.setSquare(rI, cF, (byte) 0);
			}
			pos.setSquare(rF, cF, pos.getSquare(rI, cI));
		}
		pos.setSquare(rI, cI, (byte) 0);
		
		pos.setBlackToMove(!pos.isBlackToMove());
		
		if (cI == 4 && cF == 6 && (pos.getSquare(rF,  cF) == 6 || pos.getSquare(rF,  cF) == 12)) {
			pos.setSquare(rF, 5, pos.getSquare(rI, 7));
			pos.setSquare(rI, 7, (byte) 0);
		} else if (cI == 4 && cF == 2 && (pos.getSquare(rF,  cF) == 6 || pos.getSquare(rF,  cF) == 12)) {
			pos.setSquare(rF, 3, pos.getSquare(rI, 0));
			pos.setSquare(rI, 0, (byte) 0);
		}
		
		return pos;
	}
	
	public Position switchTurn() {
		Position newPos = new Position(this);
		newPos.blackToMove = !this.blackToMove;
		return newPos;
	}
	
	public ArrayList<Position> getNextPositions() {
		ArrayList<Position> posList = new ArrayList<Position>();

		ArrayList<Move> movList = this.getAllLegalMoves();
		for (Move m: movList) {
			posList.add(positionAfterMove(m));
		}
		
		return posList;
	}

	public ArrayList<Position> getNextPositions(ArrayList<Move> movList) {
		ArrayList<Position> posList = new ArrayList<Position>();
		for (Move m: movList) {
			posList.add(positionAfterMove(m));
		}
		return posList;
	}


	public void setScore(double score) {
		this.score = score;
	}
	
	public double getScore() {
		return score;
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
    	if (xPos == 1) { //Promotion
    		if (position[xPos - 1][yPos] == 0) { // move forward 1
                for (byte id = 2; id <= 5; id++) {
                	moveList.add(new Move(xPos, yPos, xPos - 1, yPos, id));
                }
            }
            if (yPos != 7 && position[xPos - 1][yPos + 1] >= 7) { // one capture
            	for (byte id = 2; id <= 5; id++) {
            		moveList.add(new Move(xPos, yPos, xPos - 1, yPos + 1, id));
            	}
            }
            if (yPos != 0 && position[xPos - 1][yPos - 1] >= 7) { // other capture
            	for (byte id = 2; id <= 5; id++) {
            		moveList.add(new Move(xPos, yPos, xPos - 1, yPos - 1, id));
            	}
            }
    	} else {
	    	if (enPassantColumn != -1 && Math.abs(yPos - enPassantColumn) == 1 && xPos == 3) {
	    		moveList.add(new Move(xPos, yPos, 2, enPassantColumn));
	    	}
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
                if (!(position[currentx + 1][currenty + 1] >= 7)) {
                	canMoveRightUp = true;
                }
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
                if (!(position[currentx + 1][currenty - 1] >= 7)) {
                	canMoveRightDown = true;
                }
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
                if (!(position[currentx - 1][currenty + 1] >= 7)) {
                	canMoveLeftUp = true;
                }
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
                if (!(position[currentx - 1][currenty - 1] >= 7)) {
                	canMoveLeftDown = true;
                }
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
                if (!(position[currentx + 1][currenty] >= 7)) {
                	canMoveRight = true;
                }
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
                if (!(position[currentx - 1][currenty] >= 7)) {
                	canMoveLeft = true;
                }
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
                if (!(position[currentx][currenty - 1] >= 7)) {
                	canMoveUp = true;
                }
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
                if (!(position[currentx][currenty + 1] >= 7)) {
                	canMoveDown = true;
                }
                currenty++;
            }
        }
    }

    private void addMovesWhiteQueen(int xPos, int yPos) {
        addMovesWhiteBishop(xPos, yPos);
        addMovesWhiteRook(xPos, yPos);
    }

    private void addMovesWhiteKing(int xPos, int yPos) { // adds 8 king moves, if legal. TODO prevent king from entering check.
        //Castling
    	if (!castleTest && whiteCastleK && position[7][5] == 0 && position[7][6] == 0) {
    		Position castleTestPos = new Position(this);
    		castleTestPos.castleTest = true;
    		castleTestPos.setBlackToMove(!this.blackToMove);
    		ArrayList<Move> oppMoves = castleTestPos.getAllLegalMovesNoCheck();
    		boolean castle = true;
    		for(Move mov: oppMoves) {
    			if (mov.getxFinal() == 7 && mov.getyFinal() >= 4 && mov.getyFinal() <= 6) {
    				castle = false;
    				break;
    			}
    		}
    		if (castle) {
    			moveList.add(new Move(7, 4, 7, 6));
    		}
    	}
    	
    	if (!castleTest && whiteCastleQ && position[7][3] == 0 && position[7][2] == 0 && position[7][1] == 0) {
    		Position castleTestPos = new Position(this);
    		castleTestPos.castleTest = true;
    		castleTestPos.setBlackToMove(!this.blackToMove);
    		ArrayList<Move> oppMoves = castleTestPos.getAllLegalMovesNoCheck();
    		boolean castle = true;
    		for(Move mov: oppMoves) {
    			if (mov.getxFinal() == 7 && mov.getyFinal() >= 1 && mov.getyFinal() <= 4) {
    				castle = false;
    				break;
    			}
    		}
    		if (castle) {
    			moveList.add(new Move(7, 4, 7, 2));
    		}
    	}
    	
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
        if (xPos == 6) { //Promotion
        	if (position[xPos + 1][yPos] == 0) { // move forward 1
	            for (byte id = 8; id <= 11; id++) {
	            	moveList.add(new Move(xPos, yPos, xPos + 1, yPos, id));
	            }
	        }
	        if (yPos != 7 && position[xPos + 1][yPos + 1] <= 6 && position[xPos + 1][yPos + 1] != 0) { // one capture
	        	for (byte id = 8; id <= 11; id++) {
	        		moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 1, id));
	        	}
	        }
	        if (yPos != 0 && yPos != 0 && xPos < 7 && position[xPos + 1][yPos - 1] <= 6 && position[xPos + 1][yPos - 1] != 0) { // other capture
	        	for (byte id = 8; id <= 11; id++) {
	        		moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 1, id));
	        	}
	        }
        } else {
        	if (enPassantColumn != -1 && Math.abs(yPos - enPassantColumn) == 1 && xPos == 4) {
	    		moveList.add(new Move(xPos, yPos, 5, enPassantColumn));
	    	}
        	if (xPos < 7 && position[xPos + 1][yPos] == 0) { // move forward 1
	            moveList.add(new Move(xPos, yPos, xPos + 1, yPos));
	        }
	        if (xPos == 1 && position[xPos + 1][yPos] == 0 && position[xPos + 2][yPos] == 0) { // moving forward by 2
	            moveList.add(new Move(xPos, yPos, xPos + 2, yPos));
	        }
	        if (yPos != 7 && xPos != 7 && position[xPos + 1][yPos + 1] <= 6 && position[xPos + 1][yPos + 1] != 0) { // one capture
	            moveList.add(new Move(xPos, yPos, xPos + 1, yPos + 1));
	        }
	        if (yPos != 0 && xPos != 7 && position[xPos + 1][yPos - 1] <= 6 && position[xPos + 1][yPos - 1] != 0) { // other capture
	            moveList.add(new Move(xPos, yPos, xPos + 1, yPos - 1));
	        }
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
                if (position[currentx + 1][currenty + 1] == 0) {
                	canMoveRightUp = true;
                }
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
                if (position[currentx + 1][currenty - 1] == 0) {
                	canMoveRightDown = true;
                }
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
                if (position[currentx - 1][currenty + 1] == 0) {
                	canMoveLeftUp = true;
                }
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
                if (position[currentx - 1][currenty - 1] == 0) {
                	canMoveLeftDown = true;
                }
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
                if (position[currentx + 1][currenty] == 0) {
                	canMoveRight = true;
                }
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
                if (position[currentx - 1][currenty] == 0) {
                	canMoveLeft = true;
                }
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
                if (position[currentx][currenty - 1] == 0) {
                	canMoveUp = true;
                }
                currenty--;
            }
        }
        currentx = xPos;
        currenty = yPos;
        boolean canMoveDown = true;
        while (canMoveDown && currenty < 7) {
            canMoveDown = false;
            if (currenty < 7 && (position[currentx][currenty + 1] <= 6)) {
                moveList.add(new Move(xPos, yPos, currentx, currenty + 1));
                if (position[currentx][currenty + 1] == 0) {
                	canMoveDown = true;
                }
                currenty++;
            }
        }
    }

    private void addMovesBlackQueen(int xPos, int yPos) {
        addMovesBlackBishop(xPos, yPos);
        addMovesBlackRook(xPos, yPos);
    }

    private void addMovesBlackKing(int xPos, int yPos) {
    	//Castling
    	if (!castleTest && blackCastleK && position[0][5] == 0 && position[0][6] == 0) {
    		Position castleTestPos = new Position(this);
    		castleTestPos.castleTest = true;
    		castleTestPos.setBlackToMove(!this.blackToMove);
    		ArrayList<Move> oppMoves = castleTestPos.getAllLegalMovesNoCheck();
    		boolean castle = true;
    		for(Move mov: oppMoves) {
    			if (mov.getxFinal() == 0 && mov.getyFinal() >= 4 && mov.getyFinal() <= 6) {
    				castle = false;
    				break;
    			}
    		}
    		if (castle) {
    			moveList.add(new Move(0, 4, 0, 6));
    		}
    	}
    	
    	if (!castleTest && blackCastleQ && position[0][3] == 0 && position[0][2] == 0 && position[0][1] == 0) {
    		Position castleTestPos = new Position(this);
    		castleTestPos.castleTest = true;
    		castleTestPos.setBlackToMove(!this.blackToMove);
    		ArrayList<Move> oppMoves = castleTestPos.getAllLegalMovesNoCheck();
    		boolean castle = true;
    		for(Move mov: oppMoves) {
    			if (mov.getxFinal() == 0 && mov.getyFinal() >= 1 && mov.getyFinal() <= 4) {
    				castle = false;
    				break;
    			}
    		}
    		if (castle) {
    			moveList.add(new Move(0, 4, 0, 2));
    		}
    	}
    	
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
    
    public int compareTo(Position pos) {
    	if (pos.getScore() - this.getScore() == 0) {
    		return 0;
    	} else {
	    	if (this.isBlackToMove()) {
	    		if (pos.getScore() > this.getScore()) {
	    			return 1;
	    		} else {
	    			return -1;
	    		}
	    	} else {
	    		if (this.getScore() > pos.getScore()) {
	    			return 1;
	    		} else {
	    			return -1;
	    		}
	    	}
    	}
    }

    public String toHumanNotation(Move mov) {
    	int rI = mov.getxInitial();
		int cI = mov.getyInitial();
		int rF = mov.getxFinal();
		int cF = mov.getyFinal();
    	String moveNotation = "";
    	
    	if (getSquare(rI, cI) % 6 == 2) {
			moveNotation += "N";
		} else if (getSquare(rI, cI) % 6 == 3) {
			moveNotation += "B";
		} else if (getSquare(rI, cI) % 6 == 4) {
			moveNotation += "R";
		} else if (getSquare(rI, cI) % 6 == 5) {
			moveNotation += "Q";
		} else if (getSquare(rI, cI) % 6 == 0) {
			moveNotation += "K";
		}
    	
    	ArrayList<Move> allMoves = getAllLegalMoves();
		for (Move m: allMoves) {
			if (m.getxFinal() == rF && m.getyFinal() == cF && getSquare(m.getxInitial(), m.getyInitial()) == getSquare(rI, cI) && getSquare(rI, cI) % 6 != 1 && m.getyInitial() != cI) {
				moveNotation += (char) (cI + 97);
				break;
			}
		}
		for (Move m: allMoves) {
			if (m.getxFinal() == rF && m.getyFinal() == cF && getSquare(m.getxInitial(), m.getyInitial()) == getSquare(rI, cI) && getSquare(rI, cI) % 6 != 1 && m.getyInitial() == cI && m.getxInitial() != rI) {
				moveNotation += 9 - (rI + 1);
				break;
			}
		}
		if (moveNotation.length() == 3) {
			boolean needLetter = false;
			for (Move m: allMoves) {
				if (m.getxFinal() == rF && m.getyFinal() == cF && getSquare(m.getxInitial(), m.getyInitial()) == getSquare(rI, cI) && getSquare(rI, cI) % 6 != 1 && m.getyInitial() != cI && m.getxInitial() == rI) {
					needLetter = true;
					break;
				}
			}
			if (!needLetter) {
				moveNotation = "" + moveNotation.charAt(0) + moveNotation.charAt(2);
			}
		}
		
		if (getSquare(rF, cF) != 0) {
			if (getSquare(rI, cI) % 6 == 1) {
				moveNotation += (char) (cI + 97);
			}
			moveNotation += "x";
		}
		
		if (mov.getPromotionID() != 0) {
		} else {
			if ((getSquare(rI, cI) == 1 && rI == 3 && rF == 2 && cI != cF && getSquare(rF, cF) == 0)||(getSquare(rI, cI) == 7 && rI == 4 && rF == 5 && cI != cF && getSquare(rF, cF) == 0)) {
				moveNotation += (char) (cI + 97);
				moveNotation += "x";
			}
		}
		
		moveNotation += mov.toString();
		
		if (cI == 4 && cF == 6 && (getSquare(rI,  cI) == 6 || getSquare(rI,  cI) == 12)) {
			moveNotation = "O-O";
		} else if (cI == 4 && cF == 2 && (getSquare(rI,  cI) == 6 || getSquare(rI,  cI) == 12)) {
			moveNotation = "O-O-O";
		}
		
		Position posAM = positionAfterMove(mov);
		if (posAM.getAllLegalMoves().size() == 0) {
			//boolean checkmate = false;
			posAM.setBlackToMove(!isBlackToMove());
//			int[] kingLocation = pos.findKing(pos);
//    		int kingR = kingLocation[0];
//    		int kingC = kingLocation[1];
//    		ArrayList<Move> potentialLegalMoves = pos.getAllLegalMovesNoCheck();
//    		for (Move potentialNextMove: potentialLegalMoves) {
//    			if (potentialNextMove.getxFinal() == kingR && potentialNextMove.getyFinal() == kingC) {
//    				checkmate = true;
//    				break;
//    			}
//    		}
			if (posAM.inCheck(posAM)) {
				moveNotation += "#";
			} else {
			}
		} else {
			posAM.setBlackToMove(!posAM.isBlackToMove());
			if (posAM.inCheck(posAM)) {
				moveNotation += "+";
			}
			posAM.setBlackToMove(!posAM.isBlackToMove());
		}
		
		return moveNotation;
    }
    
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
				sb.append(position[i][j] + " ");
			}
		}
    	sb.append(blackToMove);
    	sb.append(blackCastleK);
    	sb.append(blackCastleQ);
    	sb.append(whiteCastleK);
    	sb.append(blackCastleQ);
    	sb.append(enPassantColumn);
    	return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position1 = (Position) o;
		return blackToMove == position1.blackToMove &&
				whiteCastleK == position1.whiteCastleK &&
				whiteCastleQ == position1.whiteCastleQ &&
				blackCastleK == position1.blackCastleK &&
				blackCastleQ == position1.blackCastleQ &&
				Arrays.equals(position, position1.position);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(blackToMove, whiteCastleK, whiteCastleQ, blackCastleK, blackCastleQ);
		result = 31 * result + Arrays.hashCode(position);
		return result;
	}

	public String getPieceNotation(int byteValue) {
    	if (byteValue == 1 || byteValue == 7) {
    		return "";
		} else if (byteValue == 2 || byteValue == 8) {
    		return "N";
		} else if (byteValue == 3 || byteValue == 9) {
    		return "B";
		} else if (byteValue == 4 || byteValue == 10) {
    		return "R";
		} else if (byteValue == 5 || byteValue == 11) {
    		return "Q";
		} else if (byteValue == 6 || byteValue == 12) {
    		return "K";
		}
    	return "Not a piece.";
	}

	public ArrayList<Move> getMoveList() {
		return moveList;
	}

	public ArrayList<Move> getCaptureList() {
		return captureList;
	}

	public ArrayList<Move> getOtherList() {
		return otherList;
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
    
    private byte[][] inputCustomPosition() {
    	return new byte[][] {
				{0, 0, 0, 0, 0, 0, 12, 0},
				{0, 0, 0, 0, 0, 0, 7, 0},
				{0, 7, 0, 0, 0, 0, 0, 7},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 0, 0, 0, 0, 1, 0},
				{0, 0, 0, 0, 0, 1, 0, 0},
				{0, 0, 0, 0, 0, 0, 6, 0}
			};
    }
}