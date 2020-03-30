package chessLogic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;


import chessLogic.Move;
import chessLogic.Position;

/* Distance from center
 * Same color pawns around
 * Same color knights around
 * Opposing pieces around
 * Opposing pawns around
 */

public class Evaluation {
	//Piece Values
	public final double pawnV = 1;
	public final double rookV = 5;
	public final double knightV = 3;
	public final double bishopV = 3;
	public final double queenV = 9;
	
	//Center Control
	public double PawnCOV = 0.2; //outside ring
	public double PawnCIV = 0.6; //inside ring
	public double KnightCOV = 0.2; 
	public double KnightCIV = 0.4;
	
	//King Safety
	public double pawnKS = 0.15;
	public double knightKS = 0.1;
	public double queenKS = 0.05;
	
	//Rooks
	public double rSeventhRank = 0.25;
	public double rOpenFile = 0.35;
	public double rConnected = 0.1;
	public double rSemiOpenFile = 0.2;
	
	//Pawns
	public double pawnConnected = 0.05;
	public double doubledPawns = 0.2;
	public double pawnGap = 0.1;
	public double passedPawn = 0.1;
	
	//Development
	public double knightBackRankPenalty = 0.2;
	public double bishopBackRankPenalty = 0.1;

	//Bishops
	public double bishopPair = 0.5;

	//Mobility
	public double mobilityScore = 0.01;

	public int count = 0;
	
	private boolean endgame = false;


	public double[][] pawnSquareTable = {
			{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			{5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
			{1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
			{0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
			{0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
			{0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
			{0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
			{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
	};
	public double[][] knightSquareTable = {
			{-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
			{-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0},
			{-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
			{-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
			{-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
			{-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
			{-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0},
			{-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
	};
	public double[][] bishopSquareTable = {
			{-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
			{-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
			{-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
			{-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
			{-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
			{-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
			{-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
			{-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
	};
	public double[][] rookSquareTable = {
			{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			{0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
			{-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			{-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			{-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			{-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			{-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			{0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}
	};
	public double[][] queenSquareTable = {
			{-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
			{-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
			{-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
			{-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
			{0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
			{-1.0, 0.5, 0,5, 0,5, 0,5, 0,5, 0.0, -1.0},
			{-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0},
			{-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
	};
	public double[][] kingSquareTable = {
			{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
			{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
			{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
			{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
			{-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
			{-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
			{2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0},
			{2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0}
	};
	
	public Evaluation() {
		
	}

	public double evaluate(Position pos) {
		double score;
		if (endgame) {
			score = evaluateEndgame(pos);
		} else {
			ArrayList<Move> moves = pos.getAllLegalMoves();
			if (moves.size() == 0) {
				if (pos.inCheck()) {
					//System.out.println("Checkmate detected");
					if (pos.isBlackToMove()) {
						return 1000000;
					} else {
						return -1000000;
					}
				} else {
					return 0;
				}
			}

			score = evaluatePieceValue(pos)
					+ evaluateCenterControl(pos)
					+ evaluateKingSafety(pos)
					// + evaluateMobility(pos, moves)
					+ evaluateDevelopment(pos)
					+ evaluateRooks(pos)
					+ evaluatePawns(pos)
					+ evaluateBishopPair(pos)
					+ evaluatePieceSquareTable(pos);
			score = round(score, 2);
			count++;
			if (count % 100000 == 0) {
				System.out.println(count/100000 + "00k positions evaluated");
			}
		}
		return score;
	}

	public double evaluateEndgame(Position pos) {
		double score;
		ArrayList<Move> moves = pos.getAllLegalMoves();
		if (moves.size() == 0) {
			if (pos.inCheck()) {
				//System.out.println("Checkmate detected");
				if (pos.isBlackToMove()) {
					return 1000000;
				} else {
					return -1000000;
				}
			} else {
				return 0;
			}
		}

		score = evaluatePieceValue(pos)
				+ evaluateKingActivity(pos)
				+ evaluateMobility(pos, moves)
				+ evaluateRooks(pos)
				+ evaluatePawnsEndgame(pos)
				+ evaluateBishopPair(pos);
		score = round(score, 2);
		count++;
		if (count % 10000 == 0) {
			System.out.println(count/10000 + "0k positions evaluated");
		}
		return score;
	}



	public double evaluatePieceValue(Position pos) {
		double score = 0.0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pos.getSquare(i, j) == 7) {
					score -= pawnV;
				} else if (pos.getSquare(i, j) == 1) {
					score += pawnV;
				} else if (pos.getSquare(i, j) == 10) {
					score -= rookV;
				} else if (pos.getSquare(i, j) == 4) {
					score += rookV;
				} else if (pos.getSquare(i, j) == 8) {
					score -= knightV;
				} else if (pos.getSquare(i, j) == 2) {
					score += knightV;
				} else if (pos.getSquare(i, j) == 9) {
					score -= bishopV;
				} else if (pos.getSquare(i, j) == 3) {
					score += bishopV;
				} else if (pos.getSquare(i, j) == 11) {
					score -= queenV;
				} else if (pos.getSquare(i, j) == 5) {
					score += queenV;
				}
			}
		}
		
		score = round(score, 2);
		return score;
	}
	
	public double evaluatePieceValueNoPawns(Position pos) {
		double score = 0.0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pos.getSquare(i, j) == 10) {
					score += rookV;
				} else if (pos.getSquare(i, j) == 4) {
					score += rookV;
				} else if (pos.getSquare(i, j) == 8) {
					score += knightV;
				} else if (pos.getSquare(i, j) == 2) {
					score += knightV;
				} else if (pos.getSquare(i, j) == 9) {
					score += bishopV;
				} else if (pos.getSquare(i, j) == 3) {
					score += bishopV;
				} else if (pos.getSquare(i, j) == 11) {
					score += queenV;
				} else if (pos.getSquare(i, j) == 5) {
					score += queenV;
				}
			}
		}
		
		score = round(score, 2);
		return score;
	}

	public double evaluateKingSafety(Position pos) {
		double score = 0.0;
		byte piece;

		//Finds White King
		int whiteKingLocation = findPiece(pos, (byte) 6).get(0);
		int whiteKingR = whiteKingLocation/8;
		int whiteKingC = whiteKingLocation%8;

		//Checks first circle around King
		for (int r = whiteKingR - 1; r < whiteKingR + 2; r++) {
			for (int c = whiteKingC - 1; c < whiteKingC + 2; c++) {
				if (r > -1 && r < 8 && c > -1 && c < 8) {
					piece = pos.getSquare(r, c);
					if (piece == 1) {
						score += pawnKS;
					} if (piece == 7) {
						score -= pawnKS;
					} if (piece == 2 || piece == 3) {
						score += knightKS;
					} if (piece == 8 || piece == 9) {
						score -= knightKS;
					} if (piece == 5) {
						score += queenKS;
					} if (piece == 11) {
						score -= queenKS;
					}
				}
			}
		}

		//System.out.println(score);

		//Checks second circle around King
		for (int r = whiteKingR - 2; r < whiteKingR + 3; r++) {
			for (int c = whiteKingC - 2; c < whiteKingC + 3; c++) {
				if (r > -1 && r < 8 && c > -1 && c < 8 && !(r > whiteKingR -2 && r < whiteKingR + 2 && c > whiteKingC - 2 && c < whiteKingC + 2)) {
					piece = pos.getSquare(r, c);
					if (piece == 1) {
						score += pawnKS/2;
					} if (piece == 7) {
						score -= pawnKS/2;
					} if (piece == 2 || piece == 3) {
						score += knightKS/2;
					} if (piece == 8 || piece == 9) {
						score -= knightKS/2;
					} if (piece == 5) {
						score += queenKS/2;
					} if (piece == 11) {
						score -= queenKS/2;
					}
				}
			}
		}

		//System.out.println(score);

		//Checks for distance from center
		double distanceScore = (Math.abs(whiteKingR - 3.5) + Math.abs(whiteKingC - 3.5))/5.0;
		score += distanceScore;

		//System.out.println(score);

		//Finds Black King
		int blackKingLocation = findPiece(pos, (byte) 12).get(0);
		int blackKingR = whiteKingLocation/8;
		int blackKingC = whiteKingLocation%8;

		//Checks first circle around King
		for (int r = blackKingR - 1; r < blackKingR + 2; r++) {
			for (int c = blackKingC - 1; c < blackKingC + 2; c++) {
				if (r > -1 && r < 8 && c > -1 && c < 8) {
					piece = pos.getSquare(r, c);
					if (piece == 1) {
						score -= pawnKS;
					} if (piece == 7) {
						score += pawnKS;
					} if (piece == 2 || piece == 3) {
						score -= knightKS;
					} if (piece == 8 || piece == 9) {
						score += knightKS;
					} if (piece == 5) {
						score -= queenKS;
					} if (piece == 11) {
						score += queenKS;
					}
				}
			}
		}

		//System.out.println(score);

		//Checks second circle around King
		for (int r = blackKingR - 2; r < blackKingR + 3; r++) {
			for (int c = blackKingC - 2; c < blackKingC + 3; c++) {
				if (r > -1 && r < 8 && c > -1 && c < 8 && !(r > blackKingR -2 && r < blackKingR + 2 && c > blackKingC - 2 && c < blackKingC + 2)) {
					piece = pos.getSquare(r, c);
					if (piece == 1) {
						score -= pawnKS/2;
					} if (piece == 7) {
						score += pawnKS/2;
					} if (piece == 2 || piece == 3) {
						score -= knightKS/2;
					} if (piece == 8 || piece == 9) {
						score += knightKS/2;
					} if (piece == 5) {
						score -= queenKS/2;
					} if (piece == 11) {
						score += queenKS/2;
					}
				}
			}
		}

		//System.out.println(score);

		//Checks for distance from center
		distanceScore = (Math.abs(blackKingR - 3.5) + Math.abs(blackKingC - 3.5))/5.0;
		score -= distanceScore;

		//System.out.println(score);

		score = round(score, 2);
		return score;
	}

	/*Finds location of the king corresponding to which color's move it is.
	 * Returns an array of two integers for the row and column.
	 * Returns {-1, -1} if king is not found (this should never happen).
	 */
//	private int[] findKing(Position pos) {
//		byte targetKing;
//		if (pos.isBlackToMove()) {
//			targetKing = 12;
//		} else {
//			targetKing = 6;
//		}
//		int[] kingLocation = {-1, -1};
//		for (int r = 0; r < 8; r++) {
//			for (int c = 0; c < 8; c++) {
//				if (pos.getSquare(r, c) == targetKing) {
//					kingLocation[0] = r;
//					kingLocation[1] = c;
//					return kingLocation;
//				}
//			}
//		}
//		return kingLocation;
//	}
//
//	private int[] findOppKing(Position pos) {
//		byte targetKing;
//		if (pos.isBlackToMove()) {
//			targetKing = 6;
//		} else {
//			targetKing = 12;
//		}
//		int[] kingLocation = {-1, -1};
//		for (int r = 0; r < 8; r++) {
//			for (int c = 0; c < 8; c++) {
//				if (pos.getSquare(r, c) == targetKing) {
//					kingLocation[0] = r;
//					kingLocation[1] = c;
//					return kingLocation;
//				}
//			}
//		}
//		return kingLocation;
//	}
	
	public double evaluateCenterControl(Position pos) {
		double score = 0.0;
		for (int i = 2; i < 6; i++) {
			for (int j = 2; j < 6; j++) {
				if (i == 3 && j == 3 || i == 3 && j == 4 || i == 4 && j == 3 || i == 4 && j == 4) {
					 if (pos.getSquare(i, j) == 1) {
					 	 score = score + PawnCIV;

					 } else if (pos.getSquare(i, j) == 7) {
					 	 score = score - PawnCIV;

					 } else if (pos.getSquare(i, j) == 2) {
					 	 score = score + KnightCIV;

					 } else if (pos.getSquare(i, j) == 8) {
					 	 score = score - KnightCIV;
					 }

				} else {
					if (pos.getSquare(i, j) == 1) {
					 	 score = score + PawnCOV;

					} else if (pos.getSquare(i, j) == 7) {
					 	 score = score - PawnCOV;

					} else if (pos.getSquare(i, j) == 2) {
					 	 score = score + KnightCOV;

					} else if (pos.getSquare(i, j) == 8) {
					 	 score = score - KnightCOV;

					}

				}

			}

		}

		score = round(score, 2);
		return score;
	}
	
	public double round(double x, double n) {
		String format = "#.";
		for (int i = 0; i < n; i ++) {
			format += "#";
		}
		DecimalFormat df = new DecimalFormat(format);
		double rounded = Double.valueOf(df.format(x));
		if (rounded == 0.0) {
			rounded = 0.0;
		}
		return rounded;
	}
	
	public double evaluateMobility(Position pos, ArrayList<Move> moves) {
		double score = moves.size() * mobilityScore;
		if (pos.isBlackToMove()) {
			score *= -1;
		}
//		pos.setBlackToMove(!pos.isBlackToMove());
//		if (pos.isBlackToMove()) {
//			score -= pos.getAllLegalMoves().size()/100.0;
//		} else {
//			score += pos.getAllLegalMoves().size()/100.0;
//		}
//		pos.setBlackToMove(!pos.isBlackToMove());
		//System.out.println(score);
		score = round(score, 2);
		return score;
	}
	
	public double evaluateDevelopment(Position pos) {
		double score = 0.0;
		for (int c = 0; c < 8; c++) {
			if (pos.getSquare(0, c) == 8) {
				score += knightBackRankPenalty;
			}
			if (pos.getSquare(0, c) == 9) {
				score += bishopBackRankPenalty;
			}
			if (pos.getSquare(7, c) == 2) {
				score -= knightBackRankPenalty;
			}
			if (pos.getSquare(7, c) == 3) {
				score -= bishopBackRankPenalty;
			}
		}
		return score;
	}
	
	public double evaluateKingActivity(Position pos) {
		int whiteKing = findPiece(pos, (byte) 6).get(0);
		int blackKing = findPiece(pos, (byte) 12).get(0);
		return (7 - whiteKing/8 - blackKing/8)/5;
	}
	
	public double evaluatePawns(Position pos) {
		double score = 0.0;
		ArrayList<Integer> whitePawns = findPiece(pos, (byte) 1);
		ArrayList<Integer> whitePawnColumns = new ArrayList<Integer>();
		for (int location: whitePawns) {
			int r = location / 8;
			int c = location % 8;
			whitePawnColumns.add(c);
			if (c > 0 && pos.getSquare(r - 1, c - 1) == 1) {
				score += pawnConnected;
			}
			if (c < 7 && pos.getSquare(r - 1, c + 1) == 1) {
				score += pawnConnected;
			}
		}
		Collections.sort(whitePawnColumns);
		for (int i = 0; i < whitePawnColumns.size() - 1; i++) {
			if (whitePawnColumns.get(i) == whitePawnColumns.get(i + 1)) {
				score -= doubledPawns;
			} else if (whitePawnColumns.get(i) - whitePawnColumns.get(i + 1) < -1) {
				score -= pawnGap;
			}
		}
		
		ArrayList<Integer> blackPawns = findPiece(pos, (byte) 7);
		ArrayList<Integer> blackPawnColumns = new ArrayList<Integer>();
		for (int location: blackPawns) {
			int r = location / 8;
			int c = location % 8;
			blackPawnColumns.add(c);
			if (c > 0 && pos.getSquare(r + 1, c - 1) == 7) {
				score -= pawnConnected;
			}
			if (c < 7 && pos.getSquare(r + 1, c + 1) == 7) {
				score -= pawnConnected;
			}
		}
		Collections.sort(blackPawnColumns);
		for (int i = 0; i < blackPawnColumns.size() - 1; i++) {
			if (blackPawnColumns.get(i) == blackPawnColumns.get(i + 1)) {
				score += doubledPawns;
			} else if (blackPawnColumns.get(i) - blackPawnColumns.get(i + 1) < -1) {
				score += pawnGap;
			}
		}

		for (int i: whitePawns) {
			score += Math.pow(7 - i/8, 2)/33;
		}

		for (int i: blackPawns) {
			score -= Math.pow(i/8, 2)/33;
		}

		for (int i = 0; i < whitePawnColumns.size(); i++) {
			boolean isPassedPawn = true;
			for (int j = 0; j < blackPawnColumns.size(); j++) {
				if (Math.abs(whitePawnColumns.get(i) - blackPawnColumns.get(j)) <= 1) {
					isPassedPawn = false;
				}
			}
			if (isPassedPawn) {
				score += passedPawn;
			}
		}
		for (int i = 0; i < blackPawnColumns.size(); i++) {
			boolean isPassedPawn = true;
			for (int j = 0; j < whitePawnColumns.size(); j++) {
				if (Math.abs(blackPawnColumns.get(i) - whitePawnColumns.get(j)) <= 1) {
					isPassedPawn = false;
				}
			}
			if (isPassedPawn) {
				score -= passedPawn;
			}
		}


		return score;
	}
	
	public double evaluatePawnsEndgame(Position pos) {
		double score = 0.0;
		ArrayList<Integer> whitePawns = findPiece(pos, (byte) 1);
		ArrayList<Integer> whitePawnColumns = new ArrayList<Integer>();
		for (int location: whitePawns) {
			int r = location / 8;
			int c = location % 8;
			whitePawnColumns.add(c);
			if (c > 0 && pos.getSquare(r - 1, c - 1) == 1) {
				score += pawnConnected;
			}
			if (c < 7 && pos.getSquare(r - 1, c + 1) == 1) {
				score += pawnConnected;
			}
		}
		Collections.sort(whitePawnColumns);
		for (int i = 0; i < whitePawnColumns.size() - 1; i++) {
			if (whitePawnColumns.get(i) == whitePawnColumns.get(i + 1)) {
				score -= doubledPawns;
			} else if (whitePawnColumns.get(i) - whitePawnColumns.get(i + 1) < -1) {
				score -= pawnGap;
			}
		}
		
		ArrayList<Integer> blackPawns = findPiece(pos, (byte) 7);
		ArrayList<Integer> blackPawnColumns = new ArrayList<Integer>();
		for (int location: blackPawns) {
			int r = location / 8;
			int c = location % 8;
			blackPawnColumns.add(c);
			if (c > 0 && pos.getSquare(r + 1, c - 1) == 7) {
				score -= pawnConnected;
			}
			if (c < 7 && pos.getSquare(r + 1, c + 1) == 7) {
				score -= pawnConnected;
			}
		}
		Collections.sort(blackPawnColumns);
		for (int i = 0; i < blackPawnColumns.size() - 1; i++) {
			if (blackPawnColumns.get(i) == blackPawnColumns.get(i + 1)) {
				score += doubledPawns;
			} else if (blackPawnColumns.get(i) - blackPawnColumns.get(i + 1) < -1) {
				score += pawnGap;
			}
		}
		
		for (int i: whitePawns) {
			score += Math.pow(7 - i/8, 2)/11;
		}
		
		for (int i: blackPawns) {
			score -= Math.pow(i/8, 2)/11;
		}
		
		return score;
	}
	
	public double evaluateRooks(Position pos) {
		double score = 0.0;
		ArrayList<Integer> whiteRooks = findPiece(pos, (byte) 4);
		for (int location : whiteRooks) {
			if (location / 8 == 1) {
				score += rSeventhRank;
			}
			int file = location % 8;
			boolean open = true;
			boolean semiOpen = true;
			for (int i = 0; i < 8; i++) {
				if (pos.getSquare(i, file) == 1) {
					semiOpen = false;
					open = false;
				}
				if (pos.getSquare(i, file) == 7) {
					open = false;
				}
				if (pos.getSquare(i, file) == 4) {
					score += rConnected;
				}
			}
			if (open) {
				score += rOpenFile;
			}
			if (semiOpen) {
				score += rSemiOpenFile;
			}
		}
		ArrayList<Integer> blackRooks = findPiece(pos, (byte) 10);
		for (int location: blackRooks) {
			if (location / 8 == 6) {
				score -= rSeventhRank;
			}
			int file = location % 8;
			boolean open = true;
			boolean semiOpen = true;
			for (int i = 0; i < 8; i++) {
				if (pos.getSquare(i, file) == 7) {
					semiOpen = false;
					open = false;
				}
				if (pos.getSquare(i, file) == 1) {
					open = false;
				}
				if (pos.getSquare(i, file) == 10) {
					score -= rConnected;
				}
			}
			if (open) {
				score -= rOpenFile;
			}
			if (semiOpen) {
				score -= rSemiOpenFile;
			}
		}
		return score;
	}

	public double evaluateBishopPair(Position pos) {
		double score = 0;
		ArrayList<Integer> whiteBishops = findPiece(pos, (byte) 3);
		if (whiteBishops.size() == 2) {
			score += bishopPair;
		}
		ArrayList<Integer> blackBishops = findPiece(pos, (byte) 9);
		if (blackBishops.size() == 2) {
			score -= bishopPair;
		}
		return score;
	}

	public double evaluatePieceSquareTable(Position pos) {
		double score = 0.0;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (pos.getSquare(r, c) == 1) {
					score += pawnSquareTable[r][c];
				} else if (pos.getSquare(r, c) == 2) {
					score += knightSquareTable[r][c];
				} else if (pos.getSquare(r, c) == 3) {
					score += bishopSquareTable[r][c];
				} else if (pos.getSquare(r, c) == 4) {
					score += rookSquareTable[r][c];
				} else if (pos.getSquare(r, c) == 5) {
					score += queenSquareTable[r][c];
				} else if (pos.getSquare(r, c) == 6) {
					score += kingSquareTable[r][c];
				} else if (pos.getSquare(r, c) == 7) {
					score -= pawnSquareTable[7 - r][7 - c];
				} else if (pos.getSquare(r, c) == 8) {
					score -= knightSquareTable[7 - r][7 - c];
				} else if (pos.getSquare(r, c) == 9) {
					score -= bishopSquareTable[7 - r][7 - c];
				} else if (pos.getSquare(r, c) == 10) {
					score -= rookSquareTable[7 - r][7 - c];
				} else if (pos.getSquare(r, c) == 11) {
					score -= queenSquareTable[7 - r][7 - c];
				} else if (pos.getSquare(r, c) == 12) {
					score -= kingSquareTable[7 - r][7 - c];
				}
			}
		}
		return round(score/20, 2);
	}


	private ArrayList<Integer> findPiece(Position pos, byte id) {
		ArrayList<Integer> pieceLocations = new ArrayList<Integer>();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (pos.getSquare(r, c) == id) {
					pieceLocations.add(8 * r + c);
				}
			}
		}
		return pieceLocations;
	}

	public boolean isEndgame() {
		return endgame;
	}

	public void setEndgame(boolean endgame) {
		this.endgame = endgame;
	}
}
