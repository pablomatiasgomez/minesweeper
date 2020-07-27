function GameHandler(apiConnector) {

    const PLAYING_STATUS = "PLAYING";
    const PAUSED_STATUS = "PAUSED";
    const WON_STATUS = "WON";
    const LOST_STATUS = "LOST";

    const CONTENT_BY_STATUS = {
        [PLAYING_STATUS]: `Status: Playing`,
        [PAUSED_STATUS]: `Status: Paused`,
        [WON_STATUS]: `Status: <span style="color: green;">You won!</span>`,
        [LOST_STATUS]: `Status: <span style="color: red;">You lost :(</span>`,
    };

    let gameStatus = document.getElementById("game-status");
    let gameTime = document.getElementById("game-time");
    let pauseGameBtn = document.getElementById("pause-game");
    let gameCells = document.getElementById("game-cells");
    let currentGame;

    function getTotalPlayedTimeMs() {
        if (!currentGame) return;
        let playedTimeMs = currentGame.playedMs;
        if (currentGame.playingSince) {
            playedTimeMs += new Date() - new Date(currentGame.playingSince);
        }
        return playedTimeMs;
    }

    setInterval(() => {
        let totalPlayedTimeMs = getTotalPlayedTimeMs();
        if (!totalPlayedTimeMs) {
            gameTime.innerText = ""
        } else {
            let seconds = Math.floor(totalPlayedTimeMs / 1000);
            let minutes = Math.floor(seconds / 60);
            let millis = totalPlayedTimeMs - seconds * 1000;
            seconds = seconds - minutes * 60;
            gameTime.innerText = `Time: ${minutes.pad(2, "0")}:${seconds.pad(2, "0")}.${millis.pad(3, "0")}`;
        }
    }, 30);

    function isPlayingGame() {
        return currentGame && currentGame.status === PLAYING_STATUS;
    }

    pauseGameBtn.onclick = function () {
        if (currentGame.status === PLAYING_STATUS) {
            return apiConnector.pauseGame(currentGame.id).then(updateGame);
        } else if (currentGame.status === PAUSED_STATUS) {
            return apiConnector.resumeGame(currentGame.id).then(updateGame);
        }
    };

    // Left click handling (reveal cell)
    gameCells.onclick = function (e) {
        if (!e.target) return;
        if (!isPlayingGame()) return;

        let row = e.target.getAttribute("data-row");
        let col = e.target.getAttribute("data-col");
        let cell = currentGame.cells[row][col];
        if (cell.revealed) return;
        apiConnector.revealCell(currentGame.id, row, col).then(updateGame);
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
        apiConnector.flagCell(currentGame.id, row, col, !cell.hasFlag).then(updateGame);
        return false;
    }, false);

    function updateGame(game) {
        currentGame = game;
        gameStatus.innerHTML = CONTENT_BY_STATUS[currentGame.status];
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

    return {
        initGame: game => {
            document.getElementById("game-container").classList.remove("d-none");
            updateGame(game);
        }
    }
}

