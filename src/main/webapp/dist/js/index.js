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
        apiConnector.openCell(currentGameId, row, col).then(updateAllCells);
    };

    gameCells.addEventListener('contextmenu', function (e) {
        e.preventDefault();
        if (!e.target) return;
        let row = e.target.getAttribute("data-row");
        let col = e.target.getAttribute("data-col");
        apiConnector.flagCell(currentGameId, row, col, true).then(updateAllCells);
        return false;
    }, false);

    function updateAllCells(game) {
        gameCells.innerHTML = game.cells.map((cells, rowIndex) => {
            let colTds = cells.map((cell, colIndex) => {
                let classes = "";
                if (cell.opened) classes += " opened";
                if (cell.hasMine) classes += " has-mine";
                if (cell.hasFlag) classes += " has-flag";
                let content = cell.adjacentMinesCount && !cell.hasMine ? cell.adjacentMinesCount : "";
                return `<td class="${classes}" data-row="${rowIndex}" data-col="${colIndex}">${content}</td>`;
            }).join("");
            return `<tr>${colTds}</tr>`;
        }).join("");
    }

    function initGame(game) {
        currentGameId = game.id;
        updateAllCells(game);
    }

    createGameBtn.onclick = function () {
        let rows = parseInt(rowsInput.value);
        let cols = parseInt(colsInput.value);
        apiConnector.createGame(rows, cols).then(initGame);
    };

})();