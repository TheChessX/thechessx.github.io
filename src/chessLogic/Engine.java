package chessLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Engine {
	protected TestEvaluation eval;
	protected int presetDepth;
	
	protected boolean theory = true;
	protected Workbook wb;
	protected int wbRow = 0;
	protected int wbCol = 0;
	protected HashMap<String, PosInfo> map = new HashMap();

	protected int MAX_TIME = 28000; // Maximum time in millis that the engine is allowed to take. Cuts off at this time and returns search result.

	protected int duplicateCount = 0;

	//Opening Mode
	//-1 Engine does not use theory
	// 0 Engine plays top theory move only (most lines)
	// 1 Engine plays random theory move, weighted by depth of theory (RECOMMENDED)
	// 2 Engine plays random theory move
	protected int openingMode = 1;
	
	public Engine() {
		this.eval = new TestEvaluation();
		this.presetDepth = 4; //Looks n plies ahead
		if (this.openingMode == -1) {
			theory = false;
		}
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("chessLogic/ChessXTheory.xlsx");
			setWb(WorkbookFactory.create(inputStream));
			System.out.println("Theory loaded");
		} catch (Exception e) {
			System.out.println("Excel file error");
		}
	}

	public MoveAndExplanation playAndExplain(Position pos) {
		System.out.println("Normal Engine Playing");
		long startTime = System.currentTimeMillis();
		if (isTheory()) {
			Move theoryMove = getTheoryMove(pos);
			return new MoveAndExplanation(theoryMove, "This move is theory.");
		}
		System.out.println("Not theory" + theory);

		eval.count = 0;
		if (eval.evaluatePieceValueNoPawns(pos) <= 18) {
			eval.setEndgame(true);
			presetDepth = 4;
		} else {
			eval.setEndgame(false);
			presetDepth = 4;
		}
		ArrayList<Move> moves = pos.getAllLegalMoves();
		ArrayList<Position> positions = new ArrayList<Position>();
		for (Move m: moves) {
			positions.add(pos.positionAfterMove(m));
		}
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).setScore(treeEvalNX(positions.get(i), -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, 1, startTime));
		}
		Collections.sort(positions);
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).setScore(treeEvalNX(positions.get(i), -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, presetDepth - 1, startTime));
			if (moves.get(i).getScore() == 1000000 * presetDepth) {
				break;
			}
			if (System.currentTimeMillis() - startTime > MAX_TIME) {
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

		//System.out.println(pos.bestNextPosition);
		//printInformation(eval, pos, bestMove);


		ArrayList<Position> positionsInSequence = new ArrayList<>();
		ArrayList<Move> movesInSequence = new ArrayList<>();

		positionsInSequence.add(pos);
		Position nextPos = pos.positionAfterMove(bestMove);
		Move bestCurrentMove = bestMove;

		for (int i = 0; i < presetDepth - 1; i++) {
			movesInSequence.add(bestCurrentMove);
			positionsInSequence.add(nextPos);
			//bestCurrentMove = new Move();
			ArrayList<Move> legalMoves = nextPos.getAllLegalMoves();
			if (nextPos.isBlackToMove()) {
				//System.out.println("Black to move");
				Double bestScore = Double.MAX_VALUE;
				System.out.println(legalMoves.size());
				for (Move m : legalMoves) {
					Double posScore = Double.MAX_VALUE;
					//if (map.containsKey(nextPos.positionAfterMove(m).toString())) {
					try {
						posScore = map.get(nextPos.positionAfterMove(m).toString()).getScore();
					} catch (NullPointerException n) {
						//System.out.println(m + " Is not in map (b)");
					}
					//}
					if (posScore < bestScore) {
						bestScore = posScore;
						bestCurrentMove = m;
						//System.out.println("New best move: " + bestCurrentMove + " for position " + i + "moves away.");
					}
				}
			} else {
				//System.out.println("White to move");
				Double bestScore = (-1) * Double.MAX_VALUE;
				System.out.println(legalMoves.size());
				for (Move m : legalMoves) {
					Double posScore = (-1) * Double.MAX_VALUE;
					//if (map.containsKey(nextPos.positionAfterMove(m).toString())) {
					try {
						posScore = map.get(nextPos.positionAfterMove(m).toString()).getScore();
					} catch (NullPointerException n) {
						//System.out.println(m + " Is not in map (w)");
					}
					//}
					if (posScore > bestScore) {
						bestScore = posScore;
						bestCurrentMove = m;
						//System.out.println("New best move: " + bestCurrentMove + " for position " + i + "moves away.");
					} else {
						//System.out.println("New not-best move: " + m + " for position " + i + "moves away. Evaluation: " + posScore);
					}
				}
				System.out.println(bestScore);
			}

			nextPos = nextPos.positionAfterMove(bestCurrentMove);
			System.out.println(bestCurrentMove.toRawString());
		}
		movesInSequence.add(bestCurrentMove);
		positionsInSequence.add(nextPos);
		//System.out.println("End of loop");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < movesInSequence.size(); i++) {
			sb.append(positionsInSequence.get(i).toHumanNotation(movesInSequence.get(i)));
			sb.append(", ");
			//System.out.println(positionsInSequence.get(i).toHumanNotation(movesInSequence.get(i)));
			//System.out.println(movesInSequence.get(i).toRawString() + "Evaluation: " + movesInSequence.get(i).getScore());
		}
		return new MoveAndExplanation(bestMove, "Computer's evaluation is " + bestMove.getScore() +
				". \n The sequence of moves that the engine thinks is best is: " + sb.toString() + "\n" +
				"The evaluation breakdown for the final position is: " + getInformation(eval, positionsInSequence.get(positionsInSequence.size()-1)));
	}


	public Move play(Position pos) {
		System.out.println("Normal Engine is playing");
		long startTime = System.currentTimeMillis();
		if (isTheory()) {
			Move theoryMove = getTheoryMove(pos);
			System.out.println("Playing Theory.");
			return theoryMove;
		}

		eval.count = 0;
		if (eval.evaluatePieceValueNoPawns(pos) <= 18) {
			eval.setEndgame(true);
			presetDepth = 4;
		} else {
			eval.setEndgame(false);
			presetDepth = 4;
		}
		ArrayList<Move> moves = pos.getAllLegalMoves();
		ArrayList<Position> positions = new ArrayList<Position>();
		for (Move m: moves) {
			positions.add(pos.positionAfterMove(m));
		}
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).setScore(treeEvalNX(positions.get(i), -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, 1, startTime));
		}
		System.out.println("Initial sort done");
		Collections.sort(positions);
		for (int i = 0; i < moves.size(); i++) {
			moves.get(i).setScore(treeEvalNX(positions.get(i), -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, presetDepth - 1, startTime));
			if (moves.get(i).getScore() == 1000000 * presetDepth) {
				break;
			}
			if (System.currentTimeMillis() - startTime > MAX_TIME) {
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
		System.out.println("Evaluation: " + bestMove.getScore());
		System.out.println("Move: " + bestMove);
		return bestMove;
	}
	
	public double treeEvalNX(Position pos, double alpha, double beta, int depth, long startTimeMillis) {
		if (System.currentTimeMillis() - startTimeMillis > MAX_TIME) {
			if (pos.getScore() != Double.MAX_VALUE) {
				Double score = pos.getScore();
				PosInfo info = new PosInfo();
				//info.setPos(pos);
				info.setDepthSearched(0);
				info.setScore(score);
				map.put(pos.toString(), info);
				return pos.getScore();
			} else {
				Double score = eval.evaluate(pos);
				PosInfo info = new PosInfo();
				//info.setPos(pos);
				info.setDepthSearched(0);
				info.setScore(score);
				map.put(pos.toString(), info);
				return eval.evaluate(pos);
			}
		}
		if (map.containsKey(pos.toString())) {
			if (map.get(pos.toString()).getDepthSearched() >= depth) {
				if (duplicateCount % 1000 == 0) {
					//System.out.println("1000 Duplicate position found");
				}
				duplicateCount++;
				return map.get(pos.toString()).getScore();
			}
		}

		if (depth == 0) {
			//long startTime = System.nanoTime();
			double score = pos.getScore();
			//long timetaken = System.nanoTime() - startTime;
			//System.out.println("Evaluation Time: " + timetaken);
			PosInfo info = new PosInfo();
			//info.setPos(pos);
			info.setDepthSearched(depth);
			info.setScore(score);
			map.put(pos.toString(), info);
			return score;
			//return eval.evaluate(pos);
		}
		//long startTime = System.nanoTime();
//		ArrayList<Position> posList1 = pos.getNextPositions();
		//long timetaken = System.nanoTime() - startTime;
		//System.out.println("MoveFinding: " + timetaken + "    "  + pos);

		ArrayList<Move> legalMoves = pos.getAllLegalMoves();


		if (legalMoves.size() == 0) {
			//System.out.println(pos.getScore() * (depth + 1));
			if (pos.getScore() == Double.MAX_VALUE) {
				Double score = eval.evaluate(pos) * (depth + 1);
				PosInfo info = new PosInfo();
				//info.setPos(pos);
				info.setDepthSearched(depth);
				info.setScore(score);
				map.put(pos.toString(), info);
				return score;
			}
			Double score = pos.getScore() * (depth + 1);
			PosInfo info = new PosInfo();
			//info.setPos(pos);
			info.setDepthSearched(depth);
			info.setScore(score);
			map.put(pos.toString(), info);
			return score;
		}

		ArrayList<Position> posList1 = pos.getNextPositions(pos.getCaptureList());

		for (Position p: posList1) {
			p.setScore(eval.evaluate(p));
		}
		Collections.sort(posList1);

		ArrayList<Position> posList2 = pos.getNextPositions(pos.getOtherList());

		for (Position p: posList2) {
			p.setScore(eval.evaluate(p));
		}
		Collections.sort(posList2);

		posList1.addAll(posList2);

		
//		for (Position p: posList1) {
//			p.setScore(eval.evaluate(p));
//		}
//		Collections.sort(posList1);
		
		
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
				//double criticality = Math.abs(pos.getScore() - pos1.getScore());
				double pos1Score;
//				if (criticality > 2.5) {
//					pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
//				} else {
					pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis);
				//}
				if (pos1Score < score) {
						score = pos1Score;
						pos.bestNextPosition = pos1;
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
				//double criticality = Math.abs(pos.getScore() - pos1.getScore());
				double pos1Score;
//				if (criticality > 2.5) {
//					pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
//				} else {
					pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis);
				//}
				if (pos1Score > score) {
					score = pos1Score;
					pos.bestNextPosition = pos1;
				}
				if (score > alpha) {
					alpha = score;
				}
				if (alpha >= beta) {
					break;
				}
			}
		}

		PosInfo info = new PosInfo();
		//info.setPos(pos);
		info.setDepthSearched(depth);
		info.setScore(score);
		map.put(pos.toString(), info);
		return score;
	}

	
//	public ArrayList<Position> nullCut(ArrayList<Position> posList, double cutAmount) {
//		for (Position pos: posList) {
//			pos.switchTurn();
//			pos.setScore(treeEvalNX(pos, -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, 1,));
//		}
//		Collections.sort(posList);
//		int size = posList.size();
//		for (int i = 0; i < size * cutAmount; i++) {
//			posList.remove(0);
//		}
//		for (Position pos: posList) {
//			pos.setScore(Double.MAX_VALUE);
//			pos.switchTurn();
//		}
//		return posList;
//	}

	public Workbook getWb() {
		return wb;
	}

	public void setWb(Workbook wb) {
		this.wb = wb;
	}

	public int getWbRow() {
		return wbRow;
	}

	public void setWbRow(int wbRow) {
		this.wbRow = wbRow;
	}

	public int getWbCol() {
		return wbCol;
	}

	public void setWbCol(int wbCol) {
		this.wbCol = wbCol;
	}

	public boolean isTheory() {
		return theory;
	}

	public void setTheory(boolean theory) {
		this.theory = theory;
	}

	public Move getTheoryMove(Position pos) {
		ArrayList<Integer> tRows = new ArrayList<Integer>();
		int totalRows = wb.getSheetAt(1).getPhysicalNumberOfRows();
		tRows.add(wbRow);
		int lastGoodRow = wbRow;
		wbRow++;
		while (wbRow < totalRows && !(wb.getSheetAt(1).getRow(wbRow).getCell(wbCol) == null) && (wbCol == 0 || wb.getSheetAt(1).getRow(wbRow).getCell(wbCol - 1).toString().equals("-"))) {
			//System.out.println(wbRow + " " + wbCol);
			if (!wb.getSheetAt(1).getRow(wbRow).getCell(wbCol).toString().equals("-")) {
				tRows.add(wbRow);
				lastGoodRow = wbRow;
			} else {
				if (openingMode == 0 || openingMode == 1) {
					tRows.add(lastGoodRow);
				}
			}
			wbRow++;
		}
		if (openingMode == 1 || openingMode == 2) {
			wbRow = tRows.get((int) (Math.random() * tRows.size()));
		} else if (openingMode == 0) {
			int currentRow = tRows.get(0);
			int bestRow = tRows.get(0);
			int max = 0;
			int count = 0;
			for (int r: tRows) {
				if (r == currentRow) {
					count++;
					if (count > max) {
						max = count;
						bestRow = r;
					}
				} else {
					currentRow = r;
					count = 0;
				}
			}
			wbRow = bestRow;
		}
		String tMove = wb.getSheetAt(1).getRow(wbRow).getCell(wbCol).toString();
		Move theoryMove = new Move(0, 0, 0, 0);
		ArrayList<Move> movesO = pos.getAllLegalMoves();
		for (Move m: movesO) {
			if (pos.toHumanNotation(m).equals(tMove)) {
				theoryMove = m;
				break;
			}
		}
//			int yInitial = (int) tMove.charAt(0) - 97;
//			int xInitial = 56 - tMove.charAt(1);
//			int yFinal = (int) tMove.charAt(2) - 97;
//			int xFinal = 56 - tMove.charAt(3);
//			int promotionID = 0;
//			if (tMove.length() > 4) {
//				promotionID = tMove.charAt(4);
//			}
//			Move theoryMove = new Move(xInitial, yInitial, xFinal, yFinal, (byte) promotionID);

		wbCol++;
		if (wb.getSheetAt(1).getRow(wbRow).getCell(wbCol).toString().equals("-")) {
			theory = false;
		}
		return theoryMove;
	}

	public void printInformation(Evaluation eval, Position pos, Move bestMove) {
		System.out.println("Evaluation: " + bestMove.getScore());
		System.out.println("Move: " + bestMove);
		if (eval.isEndgame()) {
			System.out.println("This is an endgame position.");
			System.out.println("Material: " + eval.evaluatePieceValue(pos));
			System.out.println("King Activity: " + eval.evaluateKingActivity(pos));
			System.out.println("Rooks: " + eval.evaluateRooks(pos));
			System.out.println("Pawns: " + eval.evaluatePawnsEndgame(pos));
		} else {
			System.out.println("This is a middlegame position.");
			System.out.println("Material: " + eval.evaluatePieceValue(pos));
			System.out.println("Center Control: " + eval.evaluateCenterControl(pos));
			System.out.println("King Safety: " + eval.evaluateKingSafety(pos));
			System.out.println("Development: " + eval.evaluateDevelopment(pos));
			System.out.println("Rooks: " + eval.evaluateRooks(pos));
			System.out.println("Pawns: " + eval.evaluatePawns(pos));
			System.out.println("Piece-Square Table: " + eval.evaluatePieceSquareTable(pos));

		}
	}
	public String getInformation(Evaluation eval, Position pos) {
		StringBuilder sb = new StringBuilder();
		//sb.append("Evaluation: " + pos.getScore() + "\n");
		if (eval.isEndgame()) {
			sb.append("This is an endgame position. \n");
			sb.append("Material: " + eval.evaluatePieceValue(pos) + "\n");
			sb.append("King Activity: " + eval.evaluateKingActivity(pos)+ "\n");
			sb.append("Rooks: " + eval.evaluateRooks(pos) + "\n");
			sb.append("Pawns: " + eval.evaluatePawnsEndgame(pos) + "\n");
			sb.append("Bishop Pair advantage: " + eval.evaluateBishopPair(pos) + "\n");
		} else {
			sb.append("This is a middlegame position. \n");
			sb.append("Material: " + eval.evaluatePieceValue(pos) + "\n");
			sb.append("Center Control: " + eval.evaluateCenterControl(pos) + "\n");
			sb.append("King Safety: " + eval.evaluateKingSafety(pos) + "\n");
			sb.append("Development: " + eval.round(eval.evaluateDevelopment(pos), 2) + "\n");
			sb.append("Rooks: " + eval.evaluateRooks(pos) + "\n");
			sb.append("Pawns: " + eval.round(eval.evaluatePawns(pos), 2)  + "\n");
			sb.append("Piece-Square Table: " + eval.evaluatePieceSquareTable(pos) + "\n");
			sb.append("Bishop Pair advantage: " + eval.evaluateBishopPair(pos) + "\n");
		}
		return sb.toString();
	}
	public String getInformation(TestEvaluation eval, Position pos) {
		StringBuilder sb = new StringBuilder();
		//sb.append("Evaluation: " + pos.getScore() + "\n");
		if (eval.isEndgame()) {
			sb.append("This is an endgame position. \n");
			sb.append("Material: " + eval.evaluatePieceValue(pos) + "\n");
			sb.append("King Activity: " + eval.evaluateKingActivity(pos)+ "\n");
			sb.append("Rooks: " + eval.evaluateRooks(pos) + "\n");
			sb.append("Pawns: " + eval.evaluatePawnsEndgame(pos) + "\n");
			sb.append("Bishop Pair advantage: " + eval.evaluateBishopPair(pos) + "\n");
			sb.append("Hanging Pieces: " + eval.evaluateCapturesPossible(pos) + "\n");

		} else {
			sb.append("This is a middlegame position. \n");
			sb.append("Material: " + eval.evaluatePieceValue(pos) + "\n");
			sb.append("Center Control: " + eval.evaluateCenterControl(pos) + "\n");
			sb.append("King Safety: " + eval.evaluateKingSafety(pos) + "\n");
			sb.append("Development: " + eval.round(eval.evaluateDevelopment(pos), 2) + "\n");
			sb.append("Rooks: " + eval.evaluateRooks(pos) + "\n");
			sb.append("Pawns: " + eval.round(eval.evaluatePawns(pos), 2)  + "\n");
			sb.append("Piece-Square Table: " + eval.evaluatePieceSquareTable(pos) + "\n");
			sb.append("Bishop Pair advantage: " + eval.evaluateBishopPair(pos) + "\n");
			sb.append("Hanging Pieces: " + eval.evaluateCapturesPossible(pos) + "\n");
		}
		return sb.toString();
	}
}
