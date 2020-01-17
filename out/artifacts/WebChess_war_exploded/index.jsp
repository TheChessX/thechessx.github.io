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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/master.css?10">
    <script type="text/javascript" src="//code.jquery.com/jquery-latest.js"></script>
    <script src="master.js?14" type="text/javascript"></script>
  </head>
  <body>
    <h1 id="headingID">ChessX</h1>
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
  <button id = "RestartButton">Restart Game</button>
  </body>
</html>
