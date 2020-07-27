# Create Game

Creates a new game instance

**URL** : `/api/games/`

**Method** : `POST`

**Request Body Schema** :

```json
{
    "rowsCount": "number",
    "colsCount": "number",
    "minesCount": "number"
}
```

Note that `rowsCount` and `colsCount` both have a constraint to be a number between `5` and `30`.
The `minesCount` should be between 5% and 50% of the total cells.
If any of the constraints is not met, the lower or upper bound will be used instead, and no error will be returned. 

**Request body example**

```json
{
    "rowsCount": 10,
    "colsCount": 15,
    "minesCount": 5
}
```

**Response body Schema** :

```json
{
    "id": "string",
    "rowsCount": "number",
    "colsCount": "number",
    "minesCount": "number",
    "status": "string",
    "playingSince": "string [datetime]",
    "playedMs": "number",
    "cells": "array[array[object]]"
}
```

Cell schema:

```json
{
    "revealed": "boolean",
    "hasFlag": "boolean",
    "hasMine": "boolean",
    "adjacentMinesCount": "number"
}
```

## Success Response

**Code** : `200 OK`

**Response body** :

```json
{
    "id": "5f1e58c3b2de146c6c0dbc9c",
    "rowsCount": 5,
    "colsCount": 5,
    "minesCount": 5,
    "status": "PLAYING",
    "playingSince": "2020-07-27T04:32:02.968716Z",
    "playedMs": 0,
    "cells": [
        [
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            }
        ],
        [
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            }
        ],
        [
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            }
        ],
        [
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            }
        ],
        [
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            },
            {
                "revealed": false,
                "hasFlag": false
            }
        ]
    ]
}
```