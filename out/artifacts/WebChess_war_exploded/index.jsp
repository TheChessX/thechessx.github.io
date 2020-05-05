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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/master.css?28">
    <script type="text/javascript" src="//code.jquery.com/jquery-latest.js"></script>
    <script src="master.js?32" type="text/javascript"></script>
  </head>
  <body>
    <h1 id="headingID">ChessX</h1>
      <div id = "boardAndExplanation">
        <div id = "moveToggle_Board">
          <table>
            <tr>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/10.png" class="piece blackRook"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/8.png" class="piece blackKnight"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/9.png" class="piece blackBishop"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/11.png" class="piece blackQueen"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/12.png" class="piece blackKing"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/9.png" class="piece blackBishop"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/8.png" class="piece blackKnight"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/10.png" class="piece blackRook"></img>
              </td>
            </tr>
            <tr>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/7.png" class="piece blackPawn"></img>
              </td>
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
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/1.png" class="piece whitePawn"></img>
              </td>
            </tr>
            <tr>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/4.png" class="piece whiteRook"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/2.png" class="piece whiteKnight"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/3.png" class="piece whiteBishop"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/5.png" class="piece whiteQueen"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/6.png" class="piece whiteKing"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/3.png" class="piece whiteBishop"></img>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/2.png" class="piece whiteKnight"></img>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/4.png" class="piece whiteRook"></img>
              </td>
            </tr>
          </table>
          <button id = "BackToStart" ><img id = "DoubleLeftID" src="${pageContext.request.contextPath}/img/DoubleLeft.png"></img></button>
          <button id = "PreviousMove" ><img src="${pageContext.request.contextPath}/img/LeftArrow.png"></img></button>
          <button id = "NextMove" ><img src="${pageContext.request.contextPath}/img/RightArrow.png"></img></button>
          <button id = "toEnd" ><img id = "DoubleRightID" src="${pageContext.request.contextPath}/img/DoubleRight.png"></img></button>
        </div>
      <div id = "explanationDiv">
      </div>
    </div>
    <div id = "MoveList"></div>
    <button id = "RestartButton">Restart Game</button>
    <button id = "TestButton">Computer v. Computer</button>
    <p>Welcome to ChessX! Try to win with the White pieces. To move, click on the square of the piece that you want to move, then on the square you would like to move it to.
    The computer will play for the Black pieces; it takes approximately ten seconds to play each move.
    You can also have the computer play itself. Enjoy!</p>
  </body>
</html>
