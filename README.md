# minesweeper game

This is a REST API based implementation of the classic [Minesweeper Game](https://en.wikipedia.org/wiki/Minesweeper_(video_game))

Given that this is meant to be used to evaluate development skills, and that the time was limited, there are some decisions that were taken in order to achieve most of the features.

## Play online

The game is deployed and can be publicly accessed and played at: https://minesweeper.pablomatiasgomez.com.ar 

Take into consideration that the app is hosted in an NYC datacenter and therefore the latency could be high (~300ms), which does not mean that the app is running slow.

## Running the app locally.

In order to run the app locally, you need to have
* Maven v3+
* Java 11
* MongoDB v4+

Make sure you have the mongo server running at port 27017, or change the file located at `src/main/resources/database.properties` which contains the connection string used to connect to the mongo server.

You could either run the app using an IDE, or using the terminal, by executing:

```bash
./scripts/build.sh
./scripts/start.sh
```

Then access the app by going to http://localhost:8080/ in your browser.

## REST API Documentation
  
The game was implemented using a REST API

Available methods are:

* [Create game](restdoc/post-game.md) : `POST /api/games`  
* [Get game](restdoc/get-game.md) : `GET /api/games/:id`
* [Update game](restdoc/patch-game.md) : `PATCH /api/games/:id`

## Implementation details & decisions:

* I set myself a limit of 10 hours to build this, so a few features have been left out:
    * A game can be paused/resumed but not after leaving the page (there is no way to retrieve a previous created game).
    * Accounts are not supported.
    * FE improvements that could be made:
        * The feedback exposed to the user when the game finishes is vague, and could be improved. E.g. show which flags where wrong, which mines they missed, etc.
        * The way to handle the game updates in the FE could be improved a lot, currently it throws away the entire table and renders it back again.
        * Show the number of missing mines based of the flagged cells.
* For simplicity and speed, I only wrote backend integration tests, so that I test all layers at once, except the FE. This gives a lines coverage of 88%. Unit tests could easily be created because classes are correctly isolated with their own logic, and external services/models could be mocked.
* Most of the tests cover the successful cases, if more time is available, there should be added other tests that cover edge cases, and not valid requests.
* All the patch operations currently return the entire game with all their cells. This works fine because the game entity is not big (and I'm using gzip because the content is pretty much repeated), but it is not ideal. Eventually the cells could be moved to a sub resource, or we could return only the modified cells when patching a game, and update those in the frontend instead of rendering the entire game again.
* The patch request is based on rfc6902. This is because the cells are represented as an array of arrays, and modifying an array cannot be represented with a partial game entity (unless using empty objects which is not ideal).
