import chessLogic.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.servlet.*;
import javax.servlet.http.*;

import chessLogic.*;


public class ChessServlet extends HttpServlet{

        private Position currentPosition;
        private Engine engine;
        private Evaluation evaluate;
        private boolean testingMode = false;
        private boolean firstEngineMove = true;
        private TestEngine testEngine;
        private int moveNumber = 0;
        private ArrayList<Position> positionList = new ArrayList<>();
        private int currentMoveIndex = 0;
        private JSONObject re;

        public void init() throws ServletException {
            // Do required initialization

            currentPosition = new Position();
            engine = new Engine();
            evaluate = new Evaluation();
            positionList.add(new Position());
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            long startTime = System.currentTimeMillis();

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            re = new JSONObject();

            if (request.getParameter("restart") != null && request.getParameter("restart").equals("true")) {
                handleRestart();
            } else if (request.getParameter("loadPage") != null && request.getParameter("loadPage").equals("true")) {
                re.put("loadpagecalled", "nothing");
            } else if (request.getParameter("test") != null && request.getParameter("test").equals("true")) {
                beginTesting();
            } else if (request.getParameter("TestingStill") != null && request.getParameter("TestingStill").equals("true")) {
                re.put("testing", "true");

                Move currentMove;
                MoveAndExplanation moveEx;

                if (!firstEngineMove) {
                    moveEx = engine.playAndExplain(currentPosition);
                    currentMove = moveEx.getMove();
                    theoryUpdate(testEngine, currentMove);
                } else {
                    moveEx = testEngine.playAndExplain(currentPosition);
                    currentMove = moveEx.getMove();
                    theoryUpdate(engine, currentMove);
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println(currentMove);
                System.out.println(moveEx.getExplanation());

                returnMoveInfo(currentMove);
                storeMove(currentMove);
                checkGameFinished();
                firstEngineMove = !firstEngineMove;

            } else if (request.getParameter("previousMove") != null && request.getParameter("previousMove").equals("true")) {
                if (currentMoveIndex > 0) {
                    currentMoveIndex--;
                }
            } else if (request.getParameter("nextMove") != null && request.getParameter("nextMove").equals("true")) {
                if (currentMoveIndex < moveNumber) {
                    currentMoveIndex++;
                    if (currentMoveIndex == moveNumber) {
                        re.put("isLastPosition", "true");
                    }
                }
            } else if (request.getParameter("toBeginning") != null && request.getParameter("toBeginning").equals("true")) {
                currentMoveIndex = 0;
            } else if (request.getParameter("toEnd") != null && request.getParameter("toEnd").equals("true")) {
                currentMoveIndex = moveNumber;
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
                        re.put("MoveNotation", currentPosition.toHumanNotation(currentMove));


                        theoryUpdate(engine, currentMove);

                        storeMove(currentMove);

                        checkGameFinished();

                    } else {
                        re.put("isLegal", "No");
                    }
                } else {
                    MoveAndExplanation moveAndEx = engine.playAndExplain(currentPosition);
                    Move currentMove = moveAndEx.getMove();
                    re.put("playedMove", "Computer");
                    returnMoveInfo(currentMove);


                    re.put("MoveExplanation", moveAndEx.getExplanation());

                    storeMove(currentMove);

                    checkGameFinished();
                }
            }
            re.put("position", addPosition(positionList.get(currentMoveIndex)));
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
        System.out.println("TheoryUpdate called");
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
                System.out.println("1 Theory");
            } else if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals("-")) {
                engine.setTheory(false);
                System.out.println("2 theory");
            }
        }
    }

    private void handleRestart() {
        currentPosition = new Position();
        engine = new Engine();
        evaluate = new Evaluation();
        positionList.clear();
        positionList.add(currentPosition);
        currentMoveIndex = 0;
        moveNumber = 0;
        re.put("restarting", "true");
        re.put("clearMoves", "true");
    }

    private void beginTesting() {
        currentPosition = new Position();
        engine = new Engine();
        evaluate = new Evaluation();
        testEngine = new TestEngine();
        re.put("testing", "true");
        re.put("clearMoves", "true");
        testingMode = true;
        currentMoveIndex = 0;
        moveNumber = 0;
        positionList.clear();
        positionList.add(currentPosition);
    }

    private void checkGameFinished() {
        if(currentPosition.getAllLegalMoves().size() == 0) {
            double score = currentPosition.getScore();
            if (score == 0) {
                re.put("gameEnd", "Draw");
            } else if (score > 0) {
                re.put("gameEnd", "WhiteWins");
            } else if (score < 0) {
                re.put("gameEnd", "BlackWins");
            }
        }
        if (isDraw()) {
            re.put("gameEnd", "Draw");
        }
    }

    private boolean gameConcluded() {
        if(currentPosition.getAllLegalMoves().size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void storeMove(Move currentMove) {
        currentPosition = currentPosition.positionAfterMove(currentMove);
        currentPosition.switchTurn();
        positionList.add(currentPosition);
        moveNumber++;
        currentMoveIndex++;
    }

    private void returnMoveInfo(Move currentMove) {
        re.put("InitialMoveSquare", currentMove.getxInitial() * 8 + currentMove.getyInitial());
        re.put("FinalMoveSquare", currentMove.getxFinal() * 8 + currentMove.getyFinal());
        re.put("PieceMoved", currentPosition.getPieceNotation(currentPosition.getSquare(currentMove.getxInitial(), currentMove.getyInitial())));
        re.put("MoveNumber", moveNumber);
        re.put("MoveNotation", currentPosition.toHumanNotation(currentMove));
    }

    private boolean isDraw() {
        int numRepeat = 1;
        for (int i = 0; i < positionList.size() - 1; i++) {
            if (positionList.get(positionList.size()-1).equals(positionList.get(i))) {
                numRepeat++;
            }
        }
        if (numRepeat >= 3) {
            return true;
        }
        return false;
    }
}
