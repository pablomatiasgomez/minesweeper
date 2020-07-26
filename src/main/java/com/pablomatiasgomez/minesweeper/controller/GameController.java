package com.pablomatiasgomez.minesweeper.controller;

import com.pablomatiasgomez.minesweeper.controller.model.CreateGameRequest;
import com.pablomatiasgomez.minesweeper.controller.model.GameResponse;
import com.pablomatiasgomez.minesweeper.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class GameController {

	private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

	private final JsonTransformer jsonTransformer;
	private final GameService gameService;

	public GameController(JsonTransformer jsonTransformer, GameService gameService) {
		this.jsonTransformer = jsonTransformer;
		this.gameService = gameService;
		registerRoutes();
	}

	public void registerRoutes() {
		Spark.before("/api/*", (request, response) -> {
			response.type("application/json");
		});

		Spark.post("/api/games", (request, response) -> {
			LOG.info("Creating new game..");
			CreateGameRequest createGameRequest = jsonTransformer.readValue(request.body(), CreateGameRequest.class);
			return new GameResponse(gameService.createGame(createGameRequest.getRowsCount(), createGameRequest.getColsCount()));
		}, jsonTransformer);

		Spark.get("/api/games", (request, response) -> {
			LOG.info("Getting games..");
			// TODO.
			return "";
		}, jsonTransformer);
	}

}
