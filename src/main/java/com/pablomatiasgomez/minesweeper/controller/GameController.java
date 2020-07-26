package com.pablomatiasgomez.minesweeper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class GameController {

	private static final Logger LOG = LoggerFactory.getLogger(GameController.class);
	private final JsonTransformer jsonTransformer;

	public GameController(JsonTransformer jsonTransformer) {
		this.jsonTransformer = jsonTransformer;
	}

	public void registerRoutes() {
		Spark.get("/games", (request, response) -> {
			LOG.info("Getting games..");
			// TODO.
			return "";
		}, jsonTransformer);
	}

}
