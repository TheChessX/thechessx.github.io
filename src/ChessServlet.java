import chessLogic.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.servlet.*;
import javax.servlet.http.*;


public class ChessServlet extends HttpServlet{

        private Position currentPosition;
        private Engine engine;
        private Evaluation evaluate;
        private boolean testingMode = false;
        private boolean firstEngineMove = true;
        private TestEngine testEngine;
        private int moveNumber = 0;


        public void init() throws ServletException {
            // Do required initialization

            currentPosition = new Position();
            engine = new Engine();
            evaluate = new Evaluation();
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            long startTime = System.currentTimeMillis();

            // Set response content type
            //response.setContentType("text/html");

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            JSONObject re = new JSONObject();


            if (request.getParameter("restart") != null && request.getParameter("restart").equals("true")) {
                currentPosition = new Position();
                engine = new Engine();
                evaluate = new Evaluation();
                re.put("restarting", "true");
            } else if (request.getParameter("loadPage") != null && request.getParameter("loadPage").equals("true")) {
                re.put("loadpagecalled", "nothing");
            } else if (request.getParameter("test") != null && request.getParameter("test").equals("true")) {
                currentPosition = new Position();
                engine = new Engine();
                evaluate = new Evaluation();
                testEngine = new TestEngine();
                re.put("testing", "true");
                testingMode = true;
            } else if (request.getParameter("TestingStill") != null && request.getParameter("TestingStill").equals("true")) {
                re.put("testing", "true");

                Move currentMove;

                if (firstEngineMove) {
                    currentMove = engine.play(currentPosition);
                    theoryUpdate(testEngine, currentMove);
                } else {
                    currentMove = testEngine.play(currentPosition);
                    theoryUpdate(engine, currentMove);
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println(currentMove);
                if (currentPosition.getAllLegalMoves().size() == 0) {
                    double score = evaluate.evaluate(currentPosition);
                    if (score == 0) {
                        re.put("gameEnd", "Stalemate");
                    } else if (score < 0) {
                        re.put("gameEnd", "BlackWins");
                    } else if (score > 0) {
                        re.put("gameEnd", "WhiteWins");
                    }
                }
                re.put("InitialMoveSquare", currentMove.getxInitial() * 8 + currentMove.getyInitial());
                re.put("FinalMoveSquare", currentMove.getxFinal() * 8 + currentMove.getyFinal());
                re.put("PieceMoved", currentPosition.getPieceNotation(currentPosition.getSquare(currentMove.getxInitial(), currentMove.getyInitial())));
                re.put("MoveNumber", moveNumber);
                firstEngineMove = !firstEngineMove;
                currentPosition = currentPosition.positionAfterMove(currentMove);
                currentPosition.switchTurn();
                moveNumber++;
            } else {
                if (request.getParameter("userMove") != null && request.getParameter("userMove").equals("true")) {
                    int square1 = Integer.valueOf(request.getParameter("square1"));
                    int square2 = Integer.valueOf(request.getParameter("square2"));
                    int xInitial = square1 / 8;
                    int yInitial = square1 % 8;
                    int xFinal = square2 / 8;
                    int yFinal = square2 % 8;
                    Move currentMove = new Move(xInitial, yInitial, xFinal, yFinal);

                    re.put("playedMove", "User");

                    if (currentPosition.isLegalMove(currentMove)) {
                        re.put("isLegal", "Yes");
                        if (currentPosition.getSquare(currentMove.getxInitial(), currentMove.getyInitial()) == 1 && currentMove.getxFinal() == 0) {
                            currentMove.setPromotionID((byte) 5);
                        }

                        re.put("InitialMoveSquare", square1);
                        re.put("FinalMoveSquare", square2);
                        re.put("PieceMoved", currentPosition.getPieceNotation(currentPosition.getSquare(xInitial, yInitial)));
                        re.put("MoveNumber", moveNumber);

                        theoryUpdate(engine, currentMove);

                        currentPosition = currentPosition.positionAfterMove(currentMove);
                        currentPosition.switchTurn();
                        moveNumber++;

                        if(currentPosition.getAllLegalMoves().size() == 0) {
                            double score = evaluate.evaluate(currentPosition);
                            if (score == 0) {
                                re.put("gameEnd", "Stalemate");
                            } else if (score < 0) {
                                re.put("gameEnd", "BlackWins");
                            } else if (score > 0) {
                                re.put("gameEnd", "WhiteWins");
                            }
                        }



                    } else {
                        re.put("isLegal", "No");
                    }
                } else {
                    MoveAndExplanation moveAndEx = engine.playAndExplain(currentPosition);
                    Move currentMove = moveAndEx.getMove();
                    re.put("playedMove", "Computer");
                    re.put("InitialMoveSquare", currentMove.getxInitial() * 8 + currentMove.getyInitial());
                    re.put("FinalMoveSquare", currentMove.getxFinal() * 8 + currentMove.getyFinal());
                    re.put("PieceMoved", currentPosition.getPieceNotation(currentPosition.getSquare(currentMove.getxInitial(), currentMove.getyInitial())));
                    re.put("MoveNumber", moveNumber);

                    re.put("MoveExplanation", moveAndEx.getExplanation());

                    currentPosition = currentPosition.positionAfterMove(currentMove);
                    currentPosition.switchTurn();
                    moveNumber++;

                    if(currentPosition.getAllLegalMoves().size() == 0) {
                        double score = currentPosition.getScore();
                        if (score == 0) {
                            re.put("gameEnd", "Stalemate");
                        } else if (score < 0) {
                            re.put("gameEnd", "WhiteWins");
                        } else if (score > 0) {
                            re.put("gameEnd", "BlackWins");
                        }
                    }
                }
            }
            re.put("position", addPosition(currentPosition));
            log((System.currentTimeMillis() - startTime) + "");
            out.print(re.toString());
            out.flush();
            out.close();
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            doGet(request, response);
        }

        public void destroy() {
            // do nothing.
        }

        private JSONArray addPosition (Position pos) throws IOException{
            JSONArray position = new JSONArray();

            for (int i = 0; i < 8; i++) {
                JSONArray row = new JSONArray();
                for (int j = 0; j < 8; j++) {
                    row.put(pos.getSquare(i,j));
                }
                position.put(row);

            }
            return position;
        }

    private void theoryUpdate(Engine engine, Move currentMove) {
        if (engine.isTheory()) {

            String lastMove = currentPosition.toHumanNotation(currentMove);

            boolean moveFound = false;
            int totalRows = engine.getWb().getSheetAt(1).getPhysicalNumberOfRows();
            if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals(lastMove)) {
                engine.setWbCol(engine.getWbCol() + 1);
                moveFound = true;
            } else {
                engine.setWbRow(engine.getWbRow() + 1);
                while (engine.getWbRow() < totalRows && !(engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()) == null) && (engine.getWbCol() == 0 || engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol() - 1).toString().equals("-"))) {
                    if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals(lastMove)) {
                        engine.setWbCol(engine.getWbCol() + 1);
                        moveFound = true;
                        break;
                    }
                    engine.setWbRow(engine.getWbRow() + 1);
                }
            }

            //System.out.println(engine.getWbRow() + " " + engine.getWbCol());

            if (!moveFound) {
                engine.setTheory(false);
            } else if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals("-")) {
                engine.setTheory(false);
            }
        }
    }
//    private void theoryUpdate(TestEngine engine, Move currentMove) {
//        if (engine.isTheory()) {
//
//            String lastMove = currentPosition.toHumanNotation(currentMove);
//
//            boolean moveFound = false;
//            int totalRows = engine.getWb().getSheetAt(1).getPhysicalNumberOfRows();
//            if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals(lastMove)) {
//                engine.setWbCol(engine.getWbCol() + 1);
//                moveFound = true;
//            } else {
//                engine.setWbRow(engine.getWbRow() + 1);
//                while (engine.getWbRow() < totalRows && !(engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()) == null) && (engine.getWbCol() == 0 || engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol() - 1).toString().equals("-"))) {
//                    if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals(lastMove)) {
//                        engine.setWbCol(engine.getWbCol() + 1);
//                        moveFound = true;
//                        break;
//                    }
//                    engine.setWbRow(engine.getWbRow() + 1);
//                }
//            }
//
//            //System.out.println(engine.getWbRow() + " " + engine.getWbCol());
//
//            if (!moveFound) {
//                engine.setTheory(false);
//            } else if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals("-")) {
//                engine.setTheory(false);
//            }
//        }
//    }

}
