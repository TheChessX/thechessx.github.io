<%--
  Created by IntelliJ IDEA.
  User: jan
  Date: 11/27/19
  Time: 8:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>ChessX</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/master.css">
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
    <%-- <script src="${pageContext.request.contextPath}/master.js" type="text/javascript"></script> --%>
    <script>
        window.onload=function() {
            var squares = document.getElementsByClassName("square");
            for (var i = 0; i < squares.length; i++) {
                (function () {
                    squares[i].addEventListener("click", helpFunc(i));
                }());
            }

        }
        function myFunction(i) {
            document.getElementById("text").innerText = i;
        }
        function helpFunc(index) {
            return function() {
                myFunction(index);
            };
        }



    </script>
  </head>
  <body>
    <h1 id="headingID">ChessX</h1>
    <p id="text">This is text</p>
    <button id="ButtonID">Button</button>
    <table>
      <tr>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
      </tr>
      <tr>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
      </tr>
      <tr>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
      </tr>
      <tr>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
      </tr>
      <tr>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
      </tr>
      <tr>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
      </tr>
      <tr>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
      </tr>
      <tr>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square"></td>
        <td class="whiteSquare square"></td>
        <td class="blackSquare square">
            <img src="${pageContext.request.contextPath}/img/1.png" class="piece"></img>
        </td>
        <td class="whiteSquare square"></td>
      </tr>
    </table>
    <form action = "Hello" method = "POST">
        First Name: <input type = "text" name = "first_name">
        <br />
        Last Name: <input type = "text" name = "last_name" />
        <input type = "submit" value = "Submit" />
    </form>
  </body>
</html>
