import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class ServletTryClass extends HttpServlet{

        private String message;

        public void init() throws ServletException {
            // Do required initialization
            message = "Hello World";
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            // Set response content type
            response.setContentType("text/html");

            // Actual logic goes here.
            PrintWriter out = response.getWriter();
            String title = "Using GET Method to Read Form Data";
            String docType =
                    "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title></head>\n" +
                    "<body>" +
                    request.getParameter("square1") + "\n" +
                    "</body>" +
                    "</html>"
            );
        }
        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            doGet(request, response);
        }


        public void destroy() {
            // do nothing.
        }
}

