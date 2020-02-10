package chessLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import chessLogic.*;

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

public class TestEngine extends Engine{

    public TestEngine() {
        super();
    }


    @Override
    public Move play(Position pos) {
        System.out.println("TestEngine is playing");
        long startTime = System.currentTimeMillis();
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
            presetDepth = 2;
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
        System.out.println("Evaluation: " + bestMove.getScore());
        System.out.println("Move: " + bestMove);
        return bestMove;
    }

    public double treeEvalNX(Position pos, double alpha, double beta, int depth, long startTimeMillis) {
        if (System.currentTimeMillis() - startTimeMillis > MAX_TIME) {
            return pos.getScore();
        }
        if (map.containsKey(pos.toString())) {
            if (map.get(pos.toString()).getDepthSearched() >= depth) {
                if (duplicateCount % 1000 == 0) {
                    System.out.println("1000 Duplicate position found");
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
            return score;
            //return eval.evaluate(pos);
        }
        //long startTime = System.nanoTime();
        ArrayList<Position> posList1 = pos.getNextPositions();
        //long timetaken = System.nanoTime() - startTime;
        //System.out.println("MoveFinding: " + timetaken + "    "  + pos);

        if (posList1.size() == 0) {
            //System.out.println(pos.getScore() * (depth + 1));
            if (pos.getScore() == Double.MAX_VALUE) {
                return eval.evaluate(pos) * (depth + 1);
            }
            return pos.getScore() * (depth + 1);
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
                double criticality = Math.abs(pos.getScore() - pos1.getScore());
                double pos1Score;
                if (criticality > 2.5) {
                    pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
                } else {
                    pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis);
                }
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
                double criticality = Math.abs(pos.getScore() - pos1.getScore());
                double pos1Score;
                if (criticality > 2.5) {
                    pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
                } else {
                    pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis);
                }
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

        PosInfo info = new PosInfo();
        info.setPos(pos);
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
}
