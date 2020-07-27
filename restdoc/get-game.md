# Get Game

Gets the given game

**URL** : `/api/games/:id`

**Method** : `GET`

**Response Body Schema**

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
    "playedMs": 234443,
    "cells": [
        [
            {
                "revealed": true,
                "hasFlag": false,
                "hasMine": false,
                "adjacentMinesCount": 2
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
                "revealed": true,
                "hasFlag": false,
                "hasMine": false,
                "adjacentMinesCount": 2
            },
            {
                "revealed": false,
                "hasFlag": false
            }
        ],
        [
            {
                "revealed": false,
                "hasFlag": true
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
                "hasFlag": true
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