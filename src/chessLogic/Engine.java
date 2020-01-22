package chessLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



public class Engine {
	private Evaluation eval;
	private int presetDepth;
	private HashMap<String, PosInfo> map = new HashMap();

	public Engine() {
		this.eval = new Evaluation();
		this.presetDepth = 2; //Looks n plies ahead
	}
	
	public Move play(Position pos) {
		System.out.println("call");
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
	
	public double treeEvalNX(Position pos, double alpha, double beta, int depth) {
//		if (map.containsKey(pos.toString())) {
//			if (map.get(pos.toString()).getDepthSearched() >= depth) {
//			//System.out.println("Duplicate position found");
//			return map.get(pos.toString()).getScore();
//
//			}
//		}
//
//		PosInfo info = new PosInfo();
//		info.setPos(pos);
//		info.setDepthSearched(0);
//		info.setScore(pos.getScore());
//		map.put(pos.toString(), info);

		if (depth == 0) {
			//long startTime = System.nanoTime();
			double score = pos.getScore();
			//long timetaken = System.nanoTime() - startTime;
			//System.out.println("Evaluation Time: " + timetaken);
			return score;
			//return eval.evaluate(pos);
		}
		long startTime = System.nanoTime();
		ArrayList<Position> posList1 = pos.getNextPositions();
		long timetaken = System.nanoTime() - startTime;
		System.out.println("MoveFinding: " + timetaken + "    "  + pos);
		
		if (posList1.size() == 0) {
			//System.out.println(pos.getScore() * (depth + 1));
			if (pos.getScore() == Double.MAX_VALUE) {
				return eval.evaluate(pos) * (depth + 1);
			}
			return pos.getScore() * (depth + 1);
		}

		for (Position p : posList1) {
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
}
