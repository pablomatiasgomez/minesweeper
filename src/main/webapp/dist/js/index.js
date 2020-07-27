(function () {
    let apiConnector = new ApiConnector();
    let gameHandler = new GameHandler(apiConnector);

    let rowsInput = document.getElementById("rows");
    let colsInput = document.getElementById("cols");
    let minesInput = document.getElementById("mines");
    let createGameBtn = document.getElementById("create-game");

    createGameBtn.onclick = function () {
        let rowsCount = parseInt(rowsInput.value);
        let colsCount = parseInt(colsInput.value);
        let minesCount = parseInt(minesInput.value);
        apiConnector.createGame(rowsCount, colsCount, minesCount).then(gameHandler.initGame);
    };

})();