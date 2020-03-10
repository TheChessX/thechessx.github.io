import java.util.ArrayList;
import java.util.Collections;

import data.Move;
import data.Position;

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
	private Evaluation eval;
	private int presetDepth;
	
	private boolean theory = true;
	private Workbook wb;
	private int wbRow = 0;
	private int wbCol = 0;
	
	//Opening Mode
	//-1 Engine does not use theory
	// 0 Engine plays top theory move only (most lines)
	// 1 Engine plays random theory move, weighted by depth of theory (RECOMMENDED)
	// 2 Engine plays random theory move
	private int openingMode = 1;
	
	public Engine() {
		this.eval = new Evaluation();
		this.presetDepth = 4; //Looks n plies ahead
		if (this.openingMode == -1) {
			theory = false;
		}
		try {
			InputStream inputStream = new FileInputStream(new File("./src/ChessXTheory.xlsx"));
			setWb(WorkbookFactory.create(inputStream));
			System.out.println("Theory loaded");
		} catch (Exception e) {
			System.out.println("Excel file error");
		}
	}
	
	public Move play(Position pos) {
		if (isTheory()) {
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
		System.out.println("Engine evaluation: " + bestMove.getScore());
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
		ArrayList<Move> legalMoves = pos.getAllLegalMoves();
		if (legalMoves.size() == 0) {
			//System.out.println(pos.getScore() * (depth + 1));
			if (pos.getScore() == Double.MAX_VALUE) {
				return eval.evaluate(pos) * (depth + 1);
			}
			return pos.getScore() * (depth + 1);
			//return eval.evaluate(pos) * (depth + 1);
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
		
		//System.out.println(posList1.size());
		
//		int size = posList1.size();
//		for (int i = 0; i < size; i++) {
//			posList1.remove(posList1.size() - 1);
//		}
		
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
	
	public ArrayList<Position> nullCut(ArrayList<Position> posList, double cutAmount) {
		for (Position pos: posList) {
			pos.switchTurn();
			pos.setScore(treeEvalNX(pos, -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, 1));
		}
		Collections.sort(posList);
		int size = posList.size();
		for (int i = 0; i < size * cutAmount; i++) {
			posList.remove(0);
		}
		for (Position pos: posList) {
			pos.setScore(Double.MAX_VALUE);
			pos.switchTurn();
		}
		return posList;
	}

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
}
