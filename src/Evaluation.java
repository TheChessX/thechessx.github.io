import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import data.Position;

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
	public final double knightV = 3.05;
	public final double bishopV = 3.45;
	public final double queenV = 9;
	
	//Center Control
	public double PawnCOV = 0.2; //outside ring
	public double PawnCIV = 0.5; //inside ring
	public double KnightCOV = 0.4; 
	public double KnightCIV = 0.5;
	
	//King Safety
	public double pawnKS = 0.15;
	public double knightKS = 0.1;
	public double queenKS = 0.05;
	
	public Evaluation() {
		
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
	
//		DecimalFormat df = new DecimalFormat("#.##");
//		score = Double.valueOf(df.format(score));
//		if (score == 0.0) {
//			score = 0.0;
//		}
		score = round(score, 2);
		return score;
	}
	
	public double evaluateKingSafety(Position pos) {
		double score = 0.0;
		byte piece;
		
		//Finds King
		int[] kingLocation = findKing(pos);
		int kingR = kingLocation[0];
		int kingC = kingLocation[1];
		
		//Checks first circle around King
		for (int r = kingR - 1; r < kingR + 2; r++) {
			for (int c = kingC - 1; c < kingC + 2; c++) {
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
		
		//Checks second circle around King
		for (int r = kingR - 2; r < kingR + 3; r++) {
			for (int c = kingC - 2; c < kingC + 3; c++) {
				if (r > -1 && r < 8 && c > -1 && c < 8 && !(r > kingR -2 && r < kingR + 2 && c > kingC - 2 && c < kingC + 2)) {
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
		
		//Checks for distance from center
		double distanceScore = (Math.abs(kingR - 3.5) + Math.abs(kingC - 3.5))/5.0;
		if (pos.isBlackToMove()) {
			score -= distanceScore;
		} else {
			score += distanceScore;
		}
		
		//Finds King
		kingLocation = findOppKing(pos);
		kingR = kingLocation[0];
		kingC = kingLocation[1];
		
		//Checks first circle around King
		for (int r = kingR - 1; r < kingR + 2; r++) {
			for (int c = kingC - 1; c < kingC + 2; c++) {
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
		
		//Checks second circle around King
		for (int r = kingR - 2; r < kingR + 3; r++) {
			for (int c = kingC - 2; c < kingC + 3; c++) {
				if (r > -1 && r < 8 && c > -1 && c < 8 && !(r > kingR -2 && r < kingR + 2 && c > kingC - 2 && c < kingC + 2)) {
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
		
		//Checks for distance from center
		distanceScore = (Math.abs(kingR - 3.5) + Math.abs(kingC - 3.5))/5.0;
		if (pos.isBlackToMove()) {
			score += distanceScore;
		} else {
			score -= distanceScore;
		}
		
		score = round(score, 2);
		return score;
	}
	
	/*Finds location of the king corresponding to which color's move it is.
	 * Returns an array of two integers for the row and column.
	 * Returns {-1, -1} if king is not found (this should never happen).
	 */
	private int[] findKing(Position pos) {
		byte targetKing;
		if (pos.isBlackToMove()) {
			targetKing = 12;
		} else {
			targetKing = 6;
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
	
	private int[] findOppKing(Position pos) {
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
	
	private double round(double x, double n) {
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
	
	public double evaluateMobility(Position pos) {
		pos.setBlackToMove(!pos.isBlackToMove());
		double score = pos.getAllLegalMoves().size()/100.0;
		pos.setBlackToMove(!pos.isBlackToMove());
		//System.out.println(score);
		return score;
	}
	
	public double evaluate(Position pos) {
		if (pos.getAllLegalMoves().size() == 0) {
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
		
		double score = evaluatePieceValue(pos)
				+ evaluateCenterControl(pos) 
				+ evaluateKingSafety(pos);
				//+ evaluateMobility(pos);
		return score;
	}
}
