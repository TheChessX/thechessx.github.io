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
    if (squareClickedOn != -1) {
        $.post("Hello",
            {
                square1:squareClickedOn,
                square2:i,
                userMove:true
            }).always(function(data, status) {
                //alert("Got here");
                //alert("Data: " + data + " Status: " + status);
                var dataString = data.toString();
                setUpPosition(dataString.substring(4, dataString.length));
                if (dataString.substring(0, 4) == "User") {
                    $.post("Hello",
                        {
                            square1:squareClickedOn,
                            square2:i,
                            userMove:false
                        }).always(function(data, status) {
                        var dataString = data.toString();
                        setUpPosition(dataString.substring(4, dataString.length));

                    });
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

function setUpPosition(data) {
    var positionString = data.toString();
    if (positionString.substring(0, 5) == "NotLe") {
        alert("The move that you made is illegal. Please try again.")
    } else {
        positionString = positionString.substring(5, positionString.length);
        for (var i = 0; i < 64; i++) {
            if (squares[i].children[0] != null) {
                squares[i].removeChild(squares[i].children[0]);
            }
            firstSpace = positionString.indexOf(" ");
            var pieceAtPosI = positionString.substring(0, firstSpace);
            if (pieceAtPosI != 0) {
                var image = document.createElement("img");
                image.setAttribute('src', "/img/" + pieceAtPosI + ".png");
                image.setAttribute('height', "50px");
                image.setAttribute('width', '50px');
                squares[i].appendChild(image);
            }
            positionString = positionString.replace(positionString.substring(0, firstSpace + 1), "");
        }
    }
}
