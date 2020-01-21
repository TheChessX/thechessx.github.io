var firstSquareClickedOn = -1;
var squares = document.getElementsByClassName("square");
window.onload = function() {
    $.post("Hello",
        {
            loadPage:true
        }).always(function (data, status) {
        parseRequest(data);
    });
    for (var i = 0; i < squares.length; i++) {
        squares[i].addEventListener("click", extra(i));
    }

    document.getElementById("RestartButton").addEventListener("click", function () {
        $.post("Hello",
            {
                restart:true
            }).always(function (data, status) {
                parseRequest(data);
        });
    });
};

function extra(secondSquareClicked) {
    return function() {
        if (firstSquareClickedOn != -1) {
            $.post("Hello",
                {
                    square1:firstSquareClickedOn,
                    square2:secondSquareClicked,
                    userMove:true
                }).always(function(data, status) {
                parseRequest(data);

            });
            firstSquareClickedOn = -1;
        } else {
            firstSquareClickedOn = secondSquareClicked;
        }
    };
}



function parseRequest(data) {
    window._data = data;


    if (window._data.restarting != null && window._data.restarting == "true") {
        if (document.getElementById("message") != null) {
            document.getElementById("message").remove();
        }
        if (document.getElementById("playAgain") != null) {
            document.getElementById("playAgain").remove();
        }
    }

    for (var i = 0; i < 64; i++) {
        if (squares[i].className == "blackSquare square") {
            squares[i].style.backgroundColor = "Purple";
        }
        if (squares[i].className == "whiteSquare square") {
            squares[i].style.backgroundColor = "whitesmoke";
        }
        if (squares[i].children[0] != null) {
            squares[i].removeChild(squares[i].children[0]);
        }
        var pieceAtPosI = data.position[Math.floor(i / 8)][i % 8];
        //console.log(data.position[i/8]);
        if (pieceAtPosI != 0) {
            var image = document.createElement("img");
            image.setAttribute('src', "/img/" + pieceAtPosI + ".png");
            image.setAttribute('height', "50px");
            image.setAttribute('width', '50px');
            squares[i].appendChild(image);
        }
    }

    var gameEnd = window._data.gameEnd;
    if (gameEnd != null) {
        console.log("Game has ended");
        var message = document.createElement("h1");
        message.id = "message";
        if (gameEnd == "WhiteWins") {
            message.innerText = "You Won!"
        }
        if (gameEnd == "BlackWins") {
            message.innerText = "You Lost"
        }
        if (gameEnd == "Stalemate") {
            message.innerText = "Draw"
        }
        document.getElementById("headingID").appendChild(message);
        var playAgain = document.createElement("button");
        playAgain.id = "playAgain";
        playAgain.innerText = "Click here to play another game";
        playAgain.style.fontSize = '150%';
        playAgain.addEventListener("click", function () {
            $.post("Hello",
                {
                    restart:true
                }).always(function (data, status) {
                parseRequest(data);
            });
        });
        document.getElementById("headingID").appendChild(playAgain);
    } else {

        if (window._data.playedMove != null) {
            if (window._data.playedMove == "User") {
                if (window._data.isLegal == "No") {
                    console.log("The move that you made is illegal. Please try again.")
                } else {
                    squares[window._data.InitialMoveSquare].style.backgroundColor = "Yellow";
                    squares[window._data.FinalMoveSquare].style.backgroundColor = "Yellow";
                    $.post("Hello",
                        {
                            userMove: false
                        }).always(function (data, status) {
                        parseRequest(data);
                    });
                }
            }
            squares[window._data.InitialMoveSquare].style.backgroundColor = "Yellow";
            squares[window._data.FinalMoveSquare].style.backgroundColor = "Yellow";
        }
    }


}


