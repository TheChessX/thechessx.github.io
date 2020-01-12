import java.util.ArrayList;
import java.util.Collections;

import data.Move;
import data.Position;


public class Engine {
	private Evaluation eval;
	private int presetDepth;
	
	public Engine() {
		this.eval = new Evaluation();
		this.presetDepth = 4; //Looks n plies ahead
	}
	
	public Move play(Position pos) {
		eval.count = 0;
		ArrayList<Move> moves = pos.getAllLegalMoves();
		ArrayList<Position> positions = new ArrayList<Position>();
		for (Move m: moves) {
			positions.add(pos.positionAfterMove(m));
		}
		Collections.sort(positions);
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).setScore(treeEvalNX(positions.get(i), -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, presetDepth - 1));
			if (moves.get(i).getScore() == 1000000 * presetDepth) {
				break;
			}
		}
		
//		ArrayList<Move> moves = pos.getAllLegalMoves();
//		for (Move m: moves) {
//			Position potentialPos = pos.positionAfterMove(m);
//			m.setScore(treeEvalNX(potentialPos, -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, presetDepth - 1));
//			if (m.getScore() == 1000000 * presetDepth) {
//				break;
//			}
//			//System.out.println(m + ", " + m.getScore());
//		}
		
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
					//System.out.println("New best move, score: " + m.getScore());
				}
			}
		}
		return bestMove;
	}
	
//	public double treeEval(Position pos) {
//		ArrayList<Position> posList1 = pos.getNextPositions();
//		for (Position pos1: posList1) {
//			ArrayList<Position> posList2 = pos1.getNextPositions();
//			double score2;
//			if (posList2.size() > 0) {
//				score2 = eval.evaluate(posList2.get(0));
//			} else {
//				score2 = eval.evaluate(pos1);
//			}
//			if (pos1.isBlackToMove()) {
//				for (Position pos2: posList2) {
//					if (eval.evaluate(pos2) < score2) {
//						score2 = eval.evaluate(pos2);
//					}
//				}
//			} else {
//				for (Position pos2: posList2) {
//					if (eval.evaluate(pos2) > score2) {
//						score2 = eval.evaluate(pos2);
//					}
//				}
//			}
//			pos1.setScore(score2);
//		}
//		double score1;
//		if (posList1.size() > 0) {
//			score1 = posList1.get(0).getScore();
//		} else {
//			score1 = eval.evaluate(pos) * 10;
//		}
//		if (pos.isBlackToMove()) {
//			for (Position pos1: posList1) {
//				if (pos1.getScore() < score1) {
//					score1 = pos1.getScore();
//				}
//			}
//		} else {
//			for (Position pos1: posList1) {
//				if (pos1.getScore() > score1) {
//					score1 = pos1.getScore();
//				}
//			}
//		}
//		return score1;
//	}
//	
//	public double treeEvalN(Position pos, int depth) {
//		ArrayList<Position> posList1 = pos.getNextPositions();
//		
//		//System.out.println(posList1.size());
//		double score1;
//		if (posList1.size() > 0) {
//			if (depth == 0) {
//				score1 = eval.evaluate(posList1.get(0));
//			} else {
//				score1 = treeEvalN(posList1.get(0), depth - 1);
//			}
//		} else {
//			score1 = eval.evaluate(pos);
//			score1 *= (depth + 1);
//		}
//		if (pos.isBlackToMove()) {
//			for (Position pos1: posList1) {
//				double pos1Score;
//				if (depth == 0) {
//					pos1Score = eval.evaluate(pos1);
//					//System.out.println("finish evaluation");
//				} else {
//					if (eval.evaluate(pos1) > score1 + 0.5) {
//						if (depth > 1) {
//							//pos1Score = treeEvalN(pos1, 1);
//							pos1Score = score1;
//						} else {
//							pos1Score = score1;
//						}
//						//System.out.println("discard low evaluation move");
//					} else {
//						pos1Score = treeEvalN(pos1, depth - 1);
//					}
//				}
//				if (pos1Score < score1) {
//					score1 = pos1Score;
//				}
//			}
//		} else {
//			for (Position pos1: posList1) {
//				double pos1Score;
//				if (depth == 0) {
//					pos1Score = eval.evaluate(pos1);
//				} else {
//					if (eval.evaluate(pos1) < score1 - 0.5) {
//						if (depth > 1) {
//							//pos1Score = treeEvalN(pos1, 1);
//							pos1Score = score1;
//						} else {
//							pos1Score = score1;
//						}
//					} else {
//						pos1Score = treeEvalN(pos1, depth - 1);
//					}
//				}
//				if (pos1Score > score1) {
//					score1 = pos1Score;
//				}
//			}
//		}
//		return score1;
//	}
	
	public double treeEvalNX(Position pos, double alpha, double beta, int depth) {
		if (depth == 0) {
			return pos.getScore();
			//return eval.evaluate(pos);
		}
		
		ArrayList<Position> posList1 = pos.getNextPositions();
		
		if (posList1.size() == 0) {
			//System.out.println(pos.getScore() * (depth + 1));
			if (pos.getScore() == Double.MAX_VALUE) {
				return eval.evaluate(pos) * (depth + 1);
			}
			return pos.getScore() * (depth + 1);
			//return eval.evaluate(pos) * (depth + 1);
		}

		for (Position p: posList1) {
			p.setScore(eval.evaluate(p));
		}
		Collections.sort(posList1);
		
//		if (depth > 2 && pos.isBlackToMove() && eval.evaluate(pos) < alpha - 1) {
//			depth = 2;
//		}
//		if (depth > 2 && !pos.isBlackToMove() && eval.evaluate(pos) > beta + 1) {
//			depth = 2;
//		}

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
	
//	public ArrayList<Position> sort(ArrayList<Position> posList) {
//		if (posList.get(0).isBlackToMove()) {
//			for (int i = 0; i < posList.size(); i++) {
//				posList.get(i).setScore(eval.evaluate(posList.get(i)));
//				int j = i;
//				while (i > 0 && posList.get(i).getScore() < posList.get(i-1).getScore()) {
//					Collections.swap(posList, i, i-1);
//					i--;
//				}
//				i = j;
//			}
//		} else {
//			for (int i = 0; i < posList.size(); i++) {
//				posList.get(i).setScore(eval.evaluate(posList.get(i)));
//				int j = i;
//				while (i > 0 && posList.get(i).getScore() > posList.get(i-1).getScore()) {
//					Collections.swap(posList, i, i-1);
//					i--;
//				}
//				i = j;
//			}
//		}
//		return posList;
//	}
}
