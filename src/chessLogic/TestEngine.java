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
    int hashCounter;
    int counterMoveCounter;
    Move[][] counterMoves = new Move[64][64];

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

    @Override
    public MoveAndExplanation playAndExplain(Position pos) {
        System.out.println("Test Engine Playing");
        counterMoves = new Move[64][64];
        hashCounter = 0;
        counterMoveCounter = 0;

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
        for (Move m: moves) {
            movesAndPos.add(new MoveAndResultingPosition(m, pos.positionAfterMove(m)));
        }
        Collections.sort(movesAndPos);

        int depth = 1;
        while (System.currentTimeMillis() - startTime < MAX_TIME && depth <=4) {
            for (int i = 0; i < movesAndPos.size(); i++) {
                movesAndPos.get(i).setScore(treeEvalNX(movesAndPos.get(i).getPos(), -1000000 * (depth - 1) - 2, 1000000 * (depth - 1) + 2, depth, startTime, null));
                if (!pos.isBlackToMove() && movesAndPos.get(i).getScore() == 1000000 * presetDepth || pos.isBlackToMove() && movesAndPos.get(i).getScore() == -1000000 * presetDepth) {
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
                    if (map.containsKey(nextPos.positionAfterMove(m).toString())) {
                        posScore = map.get(nextPos.positionAfterMove(m).toString()).getScore();
                    }
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
                    if (map.containsKey(nextPos.positionAfterMove(m).toString())) {
                        posScore = map.get(nextPos.positionAfterMove(m).toString()).getScore();
                    }
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


    public double treeEvalNX(Position pos, double alpha, double beta, int depth, long startTimeMillis, Move previousMove) {
        if (System.currentTimeMillis() - startTimeMillis > MAX_TIME) {
            return addToMap(pos, 0);
        }
        Move hashMove = null;
        if (map.containsKey(pos.toString())) {
            hashMove = map.get(pos.toString()).getBestMove();
            if (map.get(pos.toString()).getDepthSearched() >= depth) {
                return map.get(pos.toString()).getScore();
            }
        }

        if (depth == 0) {
            return addToMap(pos, 0);
        }

        ArrayList<Move> legalMoves = pos.getAllLegalMoves();

        if (legalMoves.size() == 0) {
            return addEndToMap(pos, depth);
        }
        ArrayList<MoveAndResultingPosition> posList1;

        posList1 = new ArrayList<>();
        ArrayList<Move> captures = pos.getCaptureList();
        ArrayList<Position> nextPos1 = pos.getNextPositions(pos.getCaptureList());
        for (int i = 0; i < captures.size(); i++) {
            posList1.add(new MoveAndResultingPosition(captures.get(i), nextPos1.get(i)));
        }
        for (MoveAndResultingPosition p : posList1) {
            p.getPos().setScore(eval.evaluate(p.getPos()));
        }
        Collections.sort(posList1);

        ArrayList<MoveAndResultingPosition> posList2 = new ArrayList<MoveAndResultingPosition>();
        ArrayList<Move> otherMoves = pos.getOtherList();
        ArrayList<Position> nextPos2 = pos.getNextPositions(pos.getOtherList());
        for (int i = 0; i < otherMoves.size(); i++) {
            posList2.add(new MoveAndResultingPosition(otherMoves.get(i), nextPos2.get(i)));
        }

        for (MoveAndResultingPosition p : posList2) {
            p.getPos().setScore(eval.evaluate(p.getPos()));
        }
        Collections.sort(posList2);

        if (previousMove != null) {
            if (counterMoves[previousMove.fromSquare()][previousMove.toSquare()] != null) {
                Move counterMove = null;
                for (int i = 0; i < posList1.size(); i++) {
                    if (posList1.get(i).getMove().equals(counterMoves[previousMove.fromSquare()][previousMove.toSquare()])) {
                        counterMove = posList1.get(i).getMove();
                        posList1.remove(i);
                        break;
                    }
                }
                if (counterMove != null) {
                    if (hashMove == null) {
                        posList1.add(0, new MoveAndResultingPosition(counterMove, pos.positionAfterMove(counterMove)));
                    } else if (!posList1.isEmpty()) {
                        posList1.add(1, new MoveAndResultingPosition(counterMove, pos.positionAfterMove(counterMove)));
                    }
                    counterMoveCounter++;
                    if (counterMoveCounter % 100 == 0) {
                        System.out.println("Countermove Used.");
                    }
                }
            }
        }


        posList1.addAll(posList2);

        if (hashMove != null) {
            hashCounter++;
            if (hashCounter % 1000 == 0) {
                System.out.println("Hashing");
            }
            for (int i = 0; i < posList1.size(); i++) {
                if (hashMove.equals(posList1.get(i).getMove())) {
                    posList1.remove(i);
                    break;
                }
            }
            posList1.add(0, new MoveAndResultingPosition(hashMove, pos.positionAfterMove(hashMove)));
        }


        double score;
        Move bestMove = null;
        if (pos.isBlackToMove()) {
            score = 1000000 * depth + 1;
            for (MoveAndResultingPosition pos1AndMove: posList1) {
                Position pos1 = pos1AndMove.getPos();
                pos1.setScore(pos1AndMove.getScore());
                //double criticality = Math.abs(pos.getScore() - pos1.getScore());
                double pos1Score;
//				if (criticality > 2.5) {
//					pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
//				} else {
                pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis, pos1AndMove.getMove());
                //}
                if (pos1Score < score) {
                    score = pos1Score;
                    bestMove = pos1AndMove.getMove();
                }
                if (score < beta) {
                    beta = score;
                }
                if (alpha >= beta) {
                    if (previousMove != null) {
                        counterMoves[previousMove.fromSquare()][previousMove.toSquare()] = pos1AndMove.getMove();
                    }
                    break;
                }
            }
        } else {
            score = -1000000 * depth - 1;
            for (MoveAndResultingPosition pos1AndMove: posList1) {
                Position pos1 = pos1AndMove.getPos();
                pos1.setScore(pos1AndMove.getScore());
                //double criticality = Math.abs(pos.getScore() - pos1.getScore());
                double pos1Score;
//				if (criticality > 2.5) {
//					pos1Score = treeEvalNX(pos1, alpha, beta, depth, startTimeMillis);
//				} else {
                pos1Score = treeEvalNX(pos1, alpha, beta, depth - 1, startTimeMillis, pos1AndMove.getMove());
                //}
                if (pos1Score > score) {
                    score = pos1Score;
                    bestMove = pos1AndMove.getMove();
                }
                if (score > alpha) {
                    alpha = score;
                }
                if (alpha >= beta) {
                    if (previousMove != null) {
                        counterMoves[previousMove.fromSquare()][previousMove.toSquare()] = pos1AndMove.getMove();
                    }
                    break;
                }
            }
        }


        PosInfo info = new PosInfo();
        //info.setPos(pos);
        info.setDepthSearched(depth);
        info.setScore(score);
        info.setBestMove(bestMove);
        map.put(pos.toString(), info);
        return score;
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
