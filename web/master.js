var firstSquareClickedOn = -1;
var squares = document.getElementsByClassName("square");
var isLatestPosition = true;
var stillTesting = true;
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
    document.getElementById("TestButton").addEventListener("click", function () {
        $.post("Hello",
            {
                test:true
            }).always(function (data, status) {
            parseRequest(data);
        });
        toggle();
    });

    function toggle() {
        document.getElementById("TestButton").innerText = "Stop Computer v. Computer";
        var old_element = document.getElementById("TestButton");
        var new_element = old_element.cloneNode(true);
        old_element.parentNode.replaceChild(new_element, old_element);
        document.getElementById("TestButton").addEventListener("click", function () {
            stillTesting = false;
            toggleBack();
        });
    }

    function toggleBack() {
        document.getElementById("TestButton").innerText = "Computer v. Computer";
        var old_element = document.getElementById("TestButton");
        var new_element = old_element.cloneNode(true);
        old_element.parentNode.replaceChild(new_element, old_element);
        document.getElementById("TestButton").addEventListener("click", function () {
            stillTesting = true;
            $.post("Hello",
                {
                    test:true
                }).always(function (data, status) {
                parseRequest(data);
            });
            toggle();
        });
    }

    document.getElementById("NextMove").addEventListener("click", function () {
        $.post("Hello",
            {
                nextMove:true
            }).always(function (data, status) {
            parseRequest(data);
        });
    });
    document.getElementById("PreviousMove").addEventListener("click", function () {
        $.post("Hello",
            {
                previousMove:true
            }).always(function (data, status) {
            parseRequest(data);
        });
        isLatestPosition = false;
    });
    document.getElementById("BackToStart").addEventListener("click", function () {
        $.post("Hello",
            {
                toBeginning :true
            }).always(function (data, status) {
            parseRequest(data);
        });
        isLatestPosition = false;
    });
    document.getElementById("toEnd").addEventListener("click", function () {
        $.post("Hello",
            {
                toEnd :true
            }).always(function (data, status) {
            parseRequest(data);
        });
        isLatestPosition = true;
    });
};

function extra(secondSquareClicked) {
    return function() {
        if (isLatestPosition) {
            if (firstSquareClickedOn != -1) {
                $.post("Hello",
                    {
                        square1: firstSquareClickedOn,
                        square2: secondSquareClicked,
                        userMove: true
                    }).always(function (data, status) {
                    parseRequest(data);

                });
                firstSquareClickedOn = -1;
            } else {
                firstSquareClickedOn = secondSquareClicked;
            }
        } else {
            alert("Please use the arrows below the board to get to the latest position.");
        }
    };
}



function parseRequest(data) {
    window._data = data;

    if (document.getElementById("explanation") != null) {
        document.getElementById("explanation").remove();
    }

    if (window._data.restarting != null && window._data.restarting == "true") {
        if (document.getElementById("message") != null) {
            document.getElementById("message").remove();
        }
        if (document.getElementById("playAgain") != null) {
            document.getElementById("playAgain").remove();
        }
        document.getElementById("MoveList").textContent = null;
    }

    if (window._data.isLastPosition != null && window._data.isLastPosition == "true") {
        isLatestPosition = true;
    }

    for (var i = 0; i < 64; i++) {
        if (squares[i].className == "blackSquare square") {
            squares[i].style.backgroundColor = "rgb(0, 100, 200)";
        }
        if (squares[i].className == "whiteSquare square") {
            squares[i].style.backgroundColor = "whitesmoke";
        }
        if (squares[i].children[0] != null) {
            squares[i].removeChild(squares[i].children[0]);
        }
        var pieceAtPosI = data.position[Math.floor(i / 8)][i % 8];
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
        var message = document.createElement("h3");
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
        //playAgain.style.fontSize = '150%';
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
        if (window._data.InitialMoveSquare != null && window._data.FinalMoveSquare != null) {
            squares[window._data.InitialMoveSquare].style.backgroundColor = "Yellow";
            squares[window._data.FinalMoveSquare].style.backgroundColor = "Yellow";

            if (window._data.MoveNotation != null) {
                if (window._data.MoveNumber%2 == 0) {
                    document.getElementById("MoveList").textContent += Math.floor(window._data.MoveNumber / 2 + 1) + ". " + window._data.MoveNotation;
                } else {
                    document.getElementById("MoveList").textContent += " " + window._data.MoveNotation + " ";
                }
            }

            //document.getElementById("MoveList").textContent += moveNum + ". " + window._data.PieceMoved + notationLetter + row + " ";

        }
        if (window._data.testing != null && window._data.testing == "true") {
            if (stillTesting) {
                $.post("Hello",
                    {
                        TestingStill: "true"
                    }).always(function (data, status) {
                    parseRequest(data);
                });
            }
        } else {

            if (window._data.playedMove != null) {
                if (window._data.playedMove == "User") {
                    if (window._data.isLegal == "No") {
                        console.log("The move that you made is illegal. Please try again.")
                    } else {
                        $.post("Hello",
                            {
                                userMove: false
                            }).always(function (data, status) {
                            parseRequest(data);
                        });
                    }
                } else if (window._data.playedMove == "Computer") {
                    isLatestPosition = true;
                    var explanation = document.createElement("p");
                    explanation.id = "explanation";
                    explanation.innerText = window._data.MoveExplanation;
                    explanation.style.fontSize = '150%';
                    document.getElementById("explanationDiv").appendChild(explanation);
                }
            }
        }
    }


}


