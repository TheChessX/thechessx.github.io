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

    Evaluation eval;

    public TestEngine() {
        this.eval = new Evaluation();
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

    public Move play(Position pos) {
        System.out.println("Test Engine is playing");
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
        ArrayList<MoveAndResultingPosition> movesAndPos = new ArrayList<>();
        ArrayList<Position> positions = new ArrayList<Position>();
        for (Move m: moves) {
            positions.add(pos.positionAfterMove(m));
            movesAndPos.add(new MoveAndResultingPosition(m, pos.positionAfterMove(m), pos.isBlackToMove()));
        }
        Collections.sort(positions);
        Collections.sort(movesAndPos);
        int depth = 1;
        while (System.currentTimeMillis() - startTime < MAX_TIME) {
            for (int i = 0; i < movesAndPos.size(); i++) {
                movesAndPos.get(i).setScore(treeEvalNX(movesAndPos.get(i).getPos(), -1000000 * (presetDepth - 1) - 2, 1000000 * (presetDepth - 1) + 2, depth, startTime));
                if (movesAndPos.get(i).getScore() == 1000000 * presetDepth) {
                    break;
                }
                if (System.currentTimeMillis() - startTime > MAX_TIME) {
                    break;
                }
            }
            depth++;
            Collections.sort(movesAndPos);
        }
        System.out.println(depth);

        Move bestMove = movesAndPos.get(0).getMove();
        bestMove.setScore(movesAndPos.get(0).getScore());
//        if (pos.isBlackToMove()) {
//            for (int i = 0; i < movesAndPos.size(); i++) {
//                Move m = movesAndPos.get(i).getMove();
//                if (m.getScore() < bestMove.getScore()) {
//                    bestMove = m;
//                }
//            }
//        } else {
//            for (int i = 0; i < movesAndPos.size(); i++) {
//                Move m = movesAndPos.get(i).getMove();
//                if (m.getScore() > bestMove.getScore()) {
//                    bestMove = m;
//                }
//            }
//        }
        System.out.println("Evaluation: " + bestMove.getScore());
        System.out.println("Move: " + bestMove);
        return bestMove;
    }

    public MoveAndExplanation playAndExplain(Position pos) {
        System.out.println("Test Engine Playing");
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
        ArrayList<MoveAndResultingPosition> movesAndPos = new ArrayList<>();
        ArrayList<Position> positions = new ArrayList<Position>();
        for (Move m: moves) {
            positions.add(pos.positionAfterMove(m));
            movesAndPos.add(new MoveAndResultingPosition(m, pos.positionAfterMove(m), pos.isBlackToMove()));
        }
        Collections.sort(positions);
        Collections.sort(movesAndPos);
        int depth = 1;
        while (System.currentTimeMillis() - startTime < MAX_TIME && depth <=5) {
            for (int i = 0; i < movesAndPos.size(); i++) {
                movesAndPos.get(i).setScore(treeEvalNX(movesAndPos.get(i).getPos(), -1000000 * (depth - 1) - 2, 1000000 * (depth - 1) + 2, depth, startTime));
                if (movesAndPos.get(i).getScore() == 1000000 * presetDepth) {
                    break;
                }
                if (System.currentTimeMillis() - startTime > MAX_TIME) {
                    break;
                }
            }
            System.out.println(depth);
            depth++;
            Collections.sort(movesAndPos);
        }

        Move bestMove = movesAndPos.get(0).getMove();
        bestMove.setScore(movesAndPos.get(0).getScore());
//        if (pos.isBlackToMove()) {
//            for (int i = 0; i < movesAndPos.size(); i++) {
//                Move m = movesAndPos.get(i).getMove();
//                if (m.getScore() < bestMove.getScore()) {
//                    bestMove = m;
//                }
//            }
//        } else {
//            for (int i = 0; i < movesAndPos.size(); i++) {
//                Move m = movesAndPos.get(i).getMove();
//                if (m.getScore() > bestMove.getScore()) {
//                    bestMove = m;
//                }
//            }
//        }

        //System.out.println(pos.bestNextPosition);
        //printInformation(eval, pos, bestMove);


        ArrayList<Position> positionsInSequence = new ArrayList<>();
        ArrayList<Move> movesInSequence = new ArrayList<>();

        positionsInSequence.add(pos);
        Position nextPos = pos.positionAfterMove(bestMove);
        Move bestCurrentMove = bestMove;

        for (int i = 0; i < depth - 1; i++) {
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


//    public double treeEvalNX(Position pos, double alpha, double beta, int depth, long startTimeMillis) {
//        pos.setScore(eval.evaluate(pos));
//        if (System.currentTimeMillis() - startTimeMillis > MAX_TIME) {
//            return addToMap(pos, 0);
//        }
//        ArrayList<MoveAndResultingPosition> hashMoves = null;
//        if (map.containsKey(pos.toString())) {
//            hashMoves = map.get(pos.toString()).getOrderedMoves();
//            if (map.get(pos.toString()).getDepthSearched() >= depth) {
//                if (duplicateCount % 1000 == 0) {
//                    //System.out.println("1000 Duplicate position found");
//                }
//                duplicateCount++;
//                return map.get(pos.toString()).getScore();
//            }
//        }
//
//        if (depth == 0) {
//            return addToMap(pos, 0);
//        }
//
//
//        ArrayList<Move> legalMoves = pos.getAllLegalMoves();
//
//
//        if (legalMoves.size() == 0) {
//            return addEndToMap(pos, depth);
//        }
//        ArrayList<MoveAndResultingPosition> posList1;
//        if (hashMoves == null) {
//            posList1 = new ArrayList<>();
//            ArrayList<Move> captures = pos.getCaptureList();
//            ArrayList<Position> nextPos1 = pos.getNextPositions(pos.getCaptureList());
//            for (int i = 0; i < captures.size(); i++) {
//                posList1.add(new MoveAndResultingPosition(captures.get(i), nextPos1.get(i), pos.isBlackToMove()));
//            }
//
//            for (MoveAndResultingPosition p : posList1) {
//                p.getPos().setScore(eval.evaluate(p.getPos()));
//            }
//            Collections.sort(posList1);
//
//            ArrayList<MoveAndResultingPosition> posList2 = new ArrayList<MoveAndResultingPosition>();
//            ArrayList<Move> otherMoves = pos.getOtherList();
//            ArrayList<Position> nextPos2 = pos.getNextPositions(pos.getOtherList());
//            for (int i = 0; i < otherMoves.size(); i++) {
//                posList2.add(new MoveAndResultingPosition(otherMoves.get(i), nextPos2.get(i), pos.isBlackToMove()));
//            }
//
//            for (MoveAndResultingPosition p : posList2) {
//                p.getPos().setScore(eval.evaluate(p.getPos()));
//            }
//            Collections.sort(posList2);
//
//            posList1.addAll(posList2);
//        } else {
//            posList1 = hashMoves;
//            System.out.println("HashingUsed");
//        }
//
//
////		for (Position p: posList1) {
////			p.setScore(eval.evaluate(p));
////		}
////		Collections.sort(posList1);
//
//
////		if (depth > 2 && pos.isBlackToMove() && eval.evaluate(pos) < alpha - 1) {
////			depth = 2;
////		}
////		if (depth > 2 && !pos.isBlackToMove() && eval.evaluate(pos) > beta + 1) {
////			depth = 2;
////		}
//        ArrayList<MoveAndResultingPosition> hashList = new ArrayList<>();
//        double score;
//        if (pos.isBlackToMove()) {
//            score = 1000000 * depth + 1;
//            for (MoveAndResultingPosition pos1AndMove: posList1) {
//                Position pos1 = pos1AndMove.getPos();
//                pos1.setScore(pos1AndMove.getScore());
//                //double criticality = Math.abs(pos.getScore() - pos1.getScore());
//                double pos1Score;
////				if (criticality > 2.5) {
////					pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
////				} else {
//                pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis);
//                //}
//                if (pos1Score < score) {
//                    score = pos1Score;
//                }
//                if (score < beta) {
//                    beta = score;
//                }
//                if (alpha >= beta) {
//                    break;
//                }
//                MoveAndResultingPosition posForHash = pos1AndMove.clone();
//                posForHash.setScore(pos1Score);
//                hashList.add(posForHash);
//            }
//        } else {
//            score = -1000000 * depth - 1;
//            for (MoveAndResultingPosition pos1AndMove: posList1) {
//                Position pos1 = pos1AndMove.getPos();
//                pos1.setScore(pos1AndMove.getScore());
//                //double criticality = Math.abs(pos.getScore() - pos1.getScore());
//                double pos1Score;
////				if (criticality > 2.5) {
////					pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
////				} else {
//                pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis);
//                //}
//                if (pos1Score > score) {
//                    score = pos1Score;
//                }
//                if (score > alpha) {
//                    alpha = score;
//                }
//                if (alpha >= beta) {
//                    break;
//                }
//                MoveAndResultingPosition posForHash = pos1AndMove.clone();
//                posForHash.setScore(pos1Score);
//                hashList.add(posForHash);
//            }
//        }
//
//        Collections.sort(hashList);
//
//        PosInfo info = new PosInfo();
//        //info.setPos(pos);
//        info.setDepthSearched(depth);
//        info.setScore(score);
//        info.setOrderedMoves(hashList);
//        map.put(pos.toString(), info);
//        return score;
//    }


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
