(function () {
    let apiConnector = new ApiConnector();

    let rowsInput = document.getElementById("rows");
    let colsInput = document.getElementById("cols");
    let createGameBtn = document.getElementById("create-game");

    let gameCells = document.getElementById("game-cells");

    let currentGameId;
    gameCells.onclick = function (e) {
        if (!e.target) return;
        let row = e.target.getAttribute("data-row");
        let col = e.target.getAttribute("data-col");
        apiConnector.openCell(currentGameId, row, col).then(res => {
            console.log(res);
        });
    };

    function initGame(game) {
        currentGameId = game.id;
        gameCells.innerHTML = game.cells.map((cells, rowIndex) => {
            let colTds = cells.map((cell, colIndex) => {
                return `<td data-row="${rowIndex}" data-col="${colIndex}">&nbsp;</td>`;
            }).join("");
            return `<tr>${colTds}</tr>`;
        }).join("");
    }

    createGameBtn.onclick = function () {
        let rows = parseInt(rowsInput.value);
        let cols = parseInt(colsInput.value);
        apiConnector.createGame(rows, cols).then(initGame);
    };


})();