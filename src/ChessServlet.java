import chessLogic.Engine;
import chessLogic.data.Position;
import chessLogic.data.Move;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class ChessServlet extends HttpServlet{

        private String message;
        private Position currentPosition;
        private Engine bigC;

        public void init() throws ServletException {
            // Do required initialization
            message = "Hello World";
            currentPosition = new Position();
            bigC = new Engine();
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            // Set response content type
            response.setContentType("application/json;charset=UTF-8");


            // Actual logic goes here.
            PrintWriter out = response.getWriter();
            int square1 = Integer.valueOf(request.getParameter("square1"));
            int square2 = Integer.valueOf(request.getParameter("square2"));
            int xInitial =  square1 / 8;
            int yInitial = square1 % 8;
            int xFinal = square2 / 8;
            int yFinal = square2 % 8;
            Move currentMove = new Move(xInitial, yInitial, xFinal, yFinal);


            if (currentPosition.isLegalMove(currentMove)) {
                currentPosition = currentPosition.positionAfterMove(currentMove);
                currentPosition.switchTurn();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        out.println(currentPosition.getSquare(i, j) + " ");
                    }
                }

            } else {
                out.println("NotLegal");
            }

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
}

