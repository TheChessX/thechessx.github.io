package chessLogic;

import java.util.ArrayList;

import chessLogic.data.Move;
import chessLogic.data.Position;


public class Engine {
	private Evaluation eval;
	private int presetDepth;

	public Engine() {
		this.eval = new Evaluation();
		this.presetDepth = 1;
	}

	public Move play(Position pos) {
		ArrayList<Move> moves = pos.getAllLegalMoves();
//		for (Move m: moves) {
//			Position potentialPos = pos.positionAfterMove(m);
//			m.setScore(eval.evaluate(potentialPos.switchTurn()));
//		}
//		Move bestMove = moves.get(0);
//		if (pos.isBlackToMove()) {
//			for (Move m: moves) {
//				if (m.getScore() < bestMove.getScore()) {
//					bestMove = m;
//				}
//			}
//		} else {
//			for (Move m: moves) {
//				if (m.getScore() > bestMove.getScore()) {
//					bestMove = m;
//				}
//			}
//		}
		for (Move m: moves) {
			Position potentialPos = pos.positionAfterMove(m);
			//System.out.println(m);
			m.setScore(treeEvalN(potentialPos, presetDepth));
		}
		Move bestMove = moves.get(0);
		if (pos.isBlackToMove()) {
			for (Move m: moves) {
				if (m.getScore() < bestMove.getScore()) {
					bestMove = m;
				}
			}
		} else {
			for (Move m: moves) {
				if (m.getScore() > bestMove.getScore()) {
					bestMove = m;
				}
			}
		}
		return bestMove;
	}

	public double treeEval(Position pos) {
		ArrayList<Position> posList1 = pos.getNextPositions();
		for (Position pos1: posList1) {
			ArrayList<Position> posList2 = pos1.getNextPositions();
			double score2;
			if (posList2.size() > 0) {
				score2 = eval.evaluate(posList2.get(0));
			} else {
				score2 = eval.evaluate(pos1);
			}
			if (pos1.isBlackToMove()) {
				for (Position pos2: posList2) {
					if (eval.evaluate(pos2) < score2) {
						score2 = eval.evaluate(pos2);
					}
				}
			} else {
				for (Position pos2: posList2) {
					if (eval.evaluate(pos2) > score2) {
						score2 = eval.evaluate(pos2);
					}
				}
			}
			pos1.setScore(score2);
		}
		double score1;
		if (posList1.size() > 0) {
			score1 = posList1.get(0).getScore();
		} else {
			score1 = eval.evaluate(pos) * 10;
		}
		if (pos.isBlackToMove()) {
			for (Position pos1: posList1) {
				if (pos1.getScore() < score1) {
					score1 = pos1.getScore();
				}
			}
		} else {
			for (Position pos1: posList1) {
				if (pos1.getScore() > score1) {
					score1 = pos1.getScore();
				}
			}
		}
		return score1;
	}

	public double treeEvalN(Position pos, int depth) {
		ArrayList<Position> posList1 = pos.getNextPositions();
		double score1;
		if (posList1.size() > 0) {
			if (depth == 0) {
				score1 = eval.evaluate(posList1.get(0));
			} else {
				score1 = treeEvalN(posList1.get(0), depth - 1);
			}
		} else {
			score1 = eval.evaluate(pos);
			if (depth == presetDepth) {
				score1 *= 10;
			}
		}
		if (pos.isBlackToMove()) {
			for (Position pos1: posList1) {
				double pos1Score;
				if (depth == 0) {
					pos1Score = eval.evaluate(pos1);
				} else {
					if (eval.evaluate(pos1) > score1 + 1) {
						if (depth > 1) {
							pos1Score = treeEvalN(pos1, 1);
						} else {
							pos1Score = score1;
						}
					} else {
						pos1Score = treeEvalN(pos1, depth - 1);
					}
				}
				if (pos1Score < score1) {
					score1 = pos1Score;
				}
			}
		} else {
			for (Position pos1: posList1) {
				double pos1Score;
				if (depth == 0) {
					pos1Score = eval.evaluate(pos1);
				} else {
					if (eval.evaluate(pos1) < score1 - 1) {
						if (depth > 1) {
							pos1Score = treeEvalN(pos1, 1);
						} else {
							pos1Score = score1;
						}
					} else {
						pos1Score = treeEvalN(pos1, depth - 1);
					}
				}
				if (pos1Score > score1) {
					score1 = pos1Score;
				}
			}
		}
		return score1;
	}
}
