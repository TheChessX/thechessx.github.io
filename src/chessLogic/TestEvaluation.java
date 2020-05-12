package chessLogic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/* Distance from center
 * Same color pawns around
 * Same color knights around
 * Opposing pieces around
 * Opposing pawns around
 */

public class TestEvaluation extends Evaluation {
	//Piece Values inherited
//	public final double pawnV = 1;
//	public final double rookV = 5;
//	public final double knightV = 3;
//	public final double bishopV = 3;
//	public final double queenV = 9;

	//Center Control - halved
//	public double PawnCOV = 0.1; //outside ring
//	public double PawnCIV = 0.3; //inside ring
//	public double KnightCOV = 0.1;
//	public double KnightCIV = 0.2;


	//King Safety - halved
//	public double pawnKS = 0.075;
//	public double knightKS = 0.05; // both knights and bishops
//	public double queenKS = 0.025;

	//Rooks - halved other than seventh rank bonus
//	public double rSeventhRank = 0.25;
//	public double rOpenFile = 0.175;
//	public double rConnected = 0.05;
//	public double rSemiOpenFile = 0.1;

	//Pawns
//	public double pawnConnected = 0.025;
//	public double doubledPawns = 0.1;
//	public double pawnGap = 0.05;
//	public double passedPawn = 0.1;

//	//Development
//	public double knightBackRankPenalty = 0.2;
//	public double bishopBackRankPenalty = 0.1;
//
//	//Bishops
//	public double bishopPair = 0.5;

	//Mobility - halved, not used
//	public double mobilityScore = 0.005;

//	public int count = 0;
//
//	private boolean endgame = false;


	public TestEvaluation() {
		PawnCOV = 0.1; //outside ring
		PawnCIV = 0.3; //inside ring
		KnightCOV = 0.1;
		KnightCIV = 0.2;
		pawnKS = 0.075;
		knightKS = 0.05; // both knights and bishops
		queenKS = 0.025;
		rSeventhRank = 0.25;
		rOpenFile = 0.175;
		rConnected = 0.05;
		rSemiOpenFile = 0.1;
		pawnConnected = 0.025;
		doubledPawns = 0.1;
		pawnGap = 0.05;
		passedPawn = 0.1;
	}

	public double evaluate(Position pos) {
		double score;
		if (super.endgame) {
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
					// + evaluateKingSafety(pos)
					// + evaluateMobility(pos, moves)
					+ evaluateDevelopment(pos)
					+ evaluateRooks(pos)
					+ evaluatePawns(pos)
					+ evaluateBishopPair(pos)
					+ evaluatePieceSquareTable(pos);
					//+ evaluateHangingPieces(pos);
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
				//+ evaluateHangingPieces(pos);
		score = round(score, 2);
		count++;
		if (count % 10000 == 0) {
			System.out.println(count/10000 + "0k positions evaluated");
		}
		return score;
	}

	public double evaluateHangingPieces(Position pos) {
		Position pos1 = pos.switchTurn();
		ArrayList<Move> reverseTurnMoves = pos1.getAllLegalMoves();
		int hangingToMove = 0;
		int hangingOpponent = 0;
		for (Move m1 : pos.getCaptureList()) {
			boolean isHangingPiece = true;
			for (Move m2 : reverseTurnMoves) {
				if (m1.getxFinal() == m2.getxFinal() && m1.getyFinal() == m2.getyFinal()) {
					isHangingPiece = false;
					break;
				}
			}
			if (isHangingPiece) {
				hangingToMove++;
			}
		}
		for (Move m1 : pos1.getCaptureList()) {
			boolean isHangingPiece = true;
			for (Move m2 : pos.getAllLegalMoves()) {
				if (m1.getxFinal() == m2.getxFinal() && m1.getyFinal() == m2.getyFinal()) {
					isHangingPiece = false;
				}
			}
			if (isHangingPiece) {
				hangingOpponent++;
			}
		}
		if (pos.isBlackToMove()) {
			return (hangingToMove - hangingOpponent);
		} else {
			return -(hangingToMove - hangingOpponent);
		}

	}

}
