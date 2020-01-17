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
        })
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
    for (var i = 0; i < 64; i++) {
        if (squares[i].children[0] != null) {
            squares[i].removeChild(squares[i].children[0]);
        }
        var pieceAtPosI = data.position[Math.floor(i/8)][i%8];
        //console.log(data.position[i/8]);
        if (pieceAtPosI != 0) {
            var image = document.createElement("img");
            image.setAttribute('src', "/img/" + pieceAtPosI + ".png");
            image.setAttribute('height', "50px");
            image.setAttribute('width', '50px');
            squares[i].appendChild(image);
        }
    }

    if (window._data.playedMove != null && window._data.playedMove == "User") {
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
    }


}