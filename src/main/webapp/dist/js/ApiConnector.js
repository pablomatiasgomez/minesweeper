function ApiConnector() {

    const GAMES_PATH = "/api/games";

    function request(url, method, body) {
        return fetch(url, {
            method: method,
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }).then(response => {
            if (response.status !== 200) {
                throw response;
            }
            return response.json();
        });
    }

    return {
        createGame: (rowsCount, colsCount) => {
            return request(GAMES_PATH, "POST", {
                rowsCount: rowsCount,
                colsCount: colsCount
            });
        },
        openCell: (gameId, row, col) => {
            return request(GAMES_PATH + "/" + gameId, "PATCH", [
                {op: "REPLACE", path: `cells/${row}/${col}/opened`, value: true}
            ]);
        }
    };
}

