function GameHandler(apiConnector) {

    let gameStatus = document.getElementById("game-status");
    let gameCells = document.getElementById("game-cells");
    let currentGame;

    function isPlayingGame() {
        return currentGame && currentGame.status === "PLAYING";
    }

    // Left click handling (reveal cell)
    gameCells.onclick = function (e) {
        if (!e.target) return;
        if (!isPlayingGame()) return;

        let row = e.target.getAttribute("data-row");
        let col = e.target.getAttribute("data-col");
        let cell = currentGame.cells[row][col];
        if (cell.revealed) return;
        apiConnector.revealCell(currentGame.id, row, col).then(game => {
            currentGame = game;
            updateGame();
        });
    };

    // Right click handling (flag cell)
    gameCells.addEventListener('contextmenu', function (e) {
        e.preventDefault();
        if (!e.target) return false;
        if (!isPlayingGame()) return false;

        let row = e.target.getAttribute("data-row");
        let col = e.target.getAttribute("data-col");
        let cell = currentGame.cells[row][col];
        if (cell.revealed) return false;
        apiConnector.flagCell(currentGame.id, row, col, !cell.hasFlag).then(game => {
            currentGame = game;
            updateGame();
        });
        return false;
    }, false);

    function updateGame() {
        gameStatus.innerText = `Status: ${currentGame.status}`;
        gameCells.innerHTML = currentGame.cells.map((cells, rowIndex) => {
            let colTds = cells.map((cell, colIndex) => {
                let classes = "";
                if (cell.revealed) classes += " revealed";
                if (cell.hasMine) classes += " has-mine";
                if (cell.hasFlag) classes += " has-flag";
                let content = cell.adjacentMinesCount && !cell.hasMine ? cell.adjacentMinesCount : "";
                return `<td class="${classes}" data-row="${rowIndex}" data-col="${colIndex}">${content}</td>`;
            }).join("");
            return `<tr>${colTds}</tr>`;
        }).join("");
    }

    function initGame(game) {
        currentGame = game;
        updateGame();
    }

    return {
        initGame: initGame
    }
}

