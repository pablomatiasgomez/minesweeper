# Update Game

Updates the given game. In general there could be 3 different updates:
- Reveal a cell
- Flag a cell
- Pause/Resume game

**URL** : `/api/games/:id`

**Method** : `PATCH`

**Request Body Schema** :

The body schema is defined by the [rfc6902](https://tools.ietf.org/html/rfc6902) and for this particular case, we only accept one patch operation per request.

**Request body example**

Reveal a cell: 
```json
[
    {
        "op": "REPLACE",
        "path": "cells/0/1/revealed",
        "value": true
    }
]
```

Flag a cell:
```json
[
    {
        "op": "REPLACE",
        "path": "cells/5/3/hasFlag",
        "value": true
    }
]
```

Pause game:
```json
[
    {
        "op": "REPLACE",
        "path": "status",
        "value": "PAUSED"
    }
]
```

Resume game:
```json
[
    {
        "op": "REPLACE",
        "path": "status",
        "value": "PLAYING"
    }
]
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