import chessLogic.Engine;
import chessLogic.data.Position;
import chessLogic.data.Move;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class ChessServlet extends HttpServlet{

        private Position currentPosition;
        private Engine chessEngine;

        public void init() throws ServletException {
            // Do required initialization

            currentPosition = new Position();
            chessEngine = new Engine();
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            // Set response content type
            //response.setContentType("text/html");

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            JSONObject re = new JSONObject();


            if (request.getParameter("restart") != null && request.getParameter("restart").equals("true")) {
                currentPosition = new Position();
            } else if (request.getParameter("loadPage") != null && request.getParameter("loadPage").equals("true")) {
                re.put("loadpagecalled", "nothing");
            } else {
                if (request.getParameter("userMove").equals("true")) {
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
                        //out.print("i: "+ (currentMove.getxInitial() + currentMove.getyInitial() * 8) + " f: " + (currentMove.getxFinal() + currentMove.getyFinal() * 8) + "Position: ");
                        currentPosition = currentPosition.positionAfterMove(currentMove);
                        currentPosition.switchTurn();

                    } else {
                        re.put("isLegal", "No");
                    }
                } else {
                    Move currentMove = chessEngine.play(currentPosition);
                    re.put("playedMove", "Computer");
                    //out.print("i: "+ (currentMove.getxInitial() + currentMove.getyInitial() * 8) + " f: " + (currentMove.getxFinal() + currentMove.getyFinal() * 8) + "Position: ");
                    currentPosition = currentPosition.positionAfterMove(currentMove);
                    currentPosition.switchTurn();
                }
            }
            re.put("position", addPosition(currentPosition));
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




}

