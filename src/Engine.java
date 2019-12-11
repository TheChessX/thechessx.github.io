import java.util.ArrayList;

import data.Move;
import data.Position;


public class Engine {
	private Evaluation eval;
	private int presetDepth;
	
	public Engine() {
		this.eval = new Evaluation();
		this.presetDepth = 5; //Looks n plays ahead
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
		//System.out.println("checkpoint 1");
		for (Move m: moves) {
			Position potentialPos = pos.positionAfterMove(m);
			//System.out.println(m);
			m.setScore(treeEvalNX(potentialPos, -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, presetDepth - 1));
			//System.out.println(m);
		}
		//System.out.println("checkpoint last");
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
		//System.out.println(bestMove.getScore());
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
		
		//System.out.println(posList1.size());
		double score1;
		if (posList1.size() > 0) {
			if (depth == 0) {
				score1 = eval.evaluate(posList1.get(0));
			} else {
				score1 = treeEvalN(posList1.get(0), depth - 1);
			}
		} else {
			score1 = eval.evaluate(pos);
			score1 *= (depth + 1);
		}
		if (pos.isBlackToMove()) {
			for (Position pos1: posList1) {
				double pos1Score;
				if (depth == 0) {
					pos1Score = eval.evaluate(pos1);
					//System.out.println("finish evaluation");
				} else {
					if (eval.evaluate(pos1) > score1 + 0.5) {
						if (depth > 1) {
							//pos1Score = treeEvalN(pos1, 1);
							pos1Score = score1;
						} else {
							pos1Score = score1;
						}
						//System.out.println("discard low evaluation move");
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
					if (eval.evaluate(pos1) < score1 - 0.5) {
						if (depth > 1) {
							//pos1Score = treeEvalN(pos1, 1);
							pos1Score = score1;
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
	
	public double treeEvalNX(Position pos, double alpha, double beta, int depth) {
		if (depth == 0) {
			return eval.evaluate(pos);
		}
		
		ArrayList<Position> posList1 = pos.getNextPositions();
		
		if (posList1.size() == 0) {
			return eval.evaluate(pos) * (depth + 1);
		}

		double score;
		if (pos.isBlackToMove()) {
			score = 1000000 * depth + 1;
			for (Position pos1: posList1) {
				double pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1);
				if (pos1Score < score) {
					score = pos1Score;
				}
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					break;
				}
			}
		} else {
			score = -1000000 * depth - 1;
			for (Position pos1: posList1) {
				double pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1);
				if (pos1Score > score) {
					score = pos1Score;
				}
				if (score > alpha) {
					alpha = score;
				}
				if (alpha >= beta) {
					break;
				}
			}
		}
		return score;
	}
}
