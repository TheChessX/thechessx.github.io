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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/master.css?50">
    <script type="text/javascript" src="//code.jquery.com/jquery-latest.js"></script>
    <script src="master.js?42" type="text/javascript"></script>
  </head>
  <body>
    <h1 id="headingID">ChessX</h1>
      <div id = "boardAndExplanation">
        <div id = "moveToggle_Board">
          <table>
            <tr>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/10.png" class="piece blackRook"></img>
                <div class="rowName">8</div>
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
                <div class="rowName">7</div>
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
              <td class="whiteSquare square">
                <div class="rowName">6</div>
              </td>
              <td class="blackSquare square"></td>
              <td class="whiteSquare square"></td>
              <td class="blackSquare square"></td>
              <td class="whiteSquare square"></td>
              <td class="blackSquare square"></td>
              <td class="whiteSquare square"></td>
              <td class="blackSquare square"></td>
            </tr>
            <tr>
              <td class="blackSquare square">
                <div class="rowName">5</div>
              </td>
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
                <div class="rowName">4</div>
              </td>
              <td class="blackSquare square"></td>
              <td class="whiteSquare square"></td>
              <td class="blackSquare square"></td>
              <td class="whiteSquare square"></td>
              <td class="blackSquare square"></td>
              <td class="whiteSquare square"></td>
              <td class="blackSquare square"></td>
            </tr>
            <tr>
              <td class="blackSquare square">
                <div class="rowName">3</div>
              </td>
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
                <div class="rowName">2</div>
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
                <div class="rowName">1</div>
                <div class="colName">a</div>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/2.png" class="piece whiteKnight"></img>
                <div class="colName">b</div>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/3.png" class="piece whiteBishop"></img>
                <div class="colName">c</div>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/5.png" class="piece whiteQueen"></img>
                <div class="colName">d</div>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/6.png" class="piece whiteKing"></img>
                <div class="colName">e</div>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/3.png" class="piece whiteBishop"></img>
                  <div class="colName">f</div>
              </td>
              <td class="blackSquare square">
                <img src="${pageContext.request.contextPath}/img/2.png" class="piece whiteKnight"></img>
                <div class="colName">g</div>
              </td>
              <td class="whiteSquare square">
                <img src="${pageContext.request.contextPath}/img/4.png" class="piece whiteRook"></img>
                <div class="colName">h</div>
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
