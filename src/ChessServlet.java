import chessLogic.Engine;
import chessLogic.data.Position;
import chessLogic.data.Move;

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
            response.setContentType("text/html");


            // Actual logic goes here.
            PrintWriter out = response.getWriter();

            if (request.getParameter("userMove").equals("true")) {
                int square1 = Integer.valueOf(request.getParameter("square1"));
                int square2 = Integer.valueOf(request.getParameter("square2"));
                int xInitial = square1 / 8;
                int yInitial = square1 % 8;
                int xFinal = square2 / 8;
                int yFinal = square2 % 8;
                Move currentMove = new Move(xInitial, yInitial, xFinal, yFinal);

                out.print("User");
                if (currentPosition.isLegalMove(currentMove)) {
                    out.print("Legal");
                    currentPosition = currentPosition.positionAfterMove(currentMove);
                    currentPosition.switchTurn();
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            out.print(currentPosition.getSquare(i, j) + " ");
                        }
                    }

                } else {
                    out.print("NotLe");
                }
            } else {
                Move currentMove = chessEngine.play(currentPosition);
                out.print("CompMoved");
                currentPosition = currentPosition.positionAfterMove(currentMove);
                currentPosition.switchTurn();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        out.print(currentPosition.getSquare(i, j) + " ");
                    }
                }
            }

            out.flush();
            out.close();
            // Returns a string with the following parameters:
            // First 4 characters: who's move (user or comp) is being processed by the request
            // if User, next 5 chars are legal or NotLe, if comp they are ignored
            // If the move is legal, the new position follows
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            doGet(request, response);
        }

        public void destroy() {
            // do nothing.
        }
}

