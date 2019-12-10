import chessLogic.Engine;
import chessLogic.data.Position;
import chessLogic.data.Move;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class ServletTryClass extends HttpServlet{

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
            response.setContentType("text/html");

            // Actual logic goes here.
            PrintWriter out = response.getWriter();
            int xInitial = Integer.valueOf(request.getParameter("square1")) / 8;
            int yInitial = Integer.valueOf(request.getParameter("square1")) % 8;
            int xFinal = Integer.valueOf(request.getParameter("square2")) / 8;
            int yFinal = Integer.valueOf(request.getParameter("square2")) % 8;
            Move currentMove = new Move(xInitial, yInitial, xFinal, yFinal);

            if (currentPosition.isLegalMove(currentMove)) {
                out.println("The move is legal!!!");
            } else {
                out.println("The move is not legal");
            }



        }

        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            doGet(request, response);
        }


        public void destroy() {
            // do nothing.
        }
}

