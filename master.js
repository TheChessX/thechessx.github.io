var squareClickedOn = -1;
var squares = document.getElementsByClassName("square");
window.onload=function() {
    for (var i = 0; i < squares.length; i++) {
        (function () {
            squares[i].addEventListener("click", helpFunc(i));
        }());
    }

}
function myFunction(i) {
    document.getElementById("text").innerText = i;
    if (squareClickedOn != -1) {
        movePiece(squareClickedOn, i);
        $.post("/Hello",
            {
                square1:squareClickedOn,
                square2:i
            }, function(data, status) {
                alert("Data: " + data + "Status: " + status);
                if (data == "legal") {
                    squares[squareClickedOn].empty();
                    alert("asdfasdf");
                }
            });
        squareClickedOn = -1;
    } else {
        squareClickedOn = i;
    }

}
function helpFunc(index) {
    return function() {
        myFunction(index);
    };
}

function movePiece(square1, square2) {
    var piece = squares[square1].children[0];
    var name = piece.className;
    var x = document.createElement("IMG");
    x.setAttribute("width", "50");
    x.setAttribute("height", "50");
    $(piece).hide();

    if (name == "piece whitePawn") {
        x.setAttribute("src", "/img/1.png");
    }
    if (name == "piece whiteKnight") {
        x.setAttribute("src", "/img/2.png");
    }
    if (name == "piece whiteBishop") {
        x.setAttribute("src", "/img/3.png");
    }
    if (name == "piece whiteRook") {
        x.setAttribute("src", "/img/4.png");
    }
    if (name == "piece whiteQueen") {
        x.setAttribute("src", "/img/5.png");
    }
    if (name == "piece whiteKing") {
        x.setAttribute("src", "/img/6.png");
    }
    if (name == "piece blackPawn") {
        x.setAttribute("src", "/img/7.png");
    }
    if (name == "piece blackKnight") {
        x.setAttribute("src", "/img/8.png");
    }
    if (name == "piece blackBishop") {
        x.setAttribute("src", "/img/9.png");
    }
    if (name == "piece blackRook") {
        x.setAttribute("src", "/img/10.png");
    }
    if (name == "piece blackQueen") {
        x.setAttribute("src", "/img/11.png");
    }
    if (name == "piece blackKing") {
        x.setAttribute("src", "/img/12.png");
    }

    squares[square2].appendChild(x);
}
