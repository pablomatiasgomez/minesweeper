package com.pablomatiasgomez.minesweeper.controller;

import static com.google.common.collect.MoreCollectors.onlyElement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.pablomatiasgomez.minesweeper.controller.model.CreateGameRequest;
import com.pablomatiasgomez.minesweeper.controller.model.GameResponse;
import com.pablomatiasgomez.minesweeper.controller.model.JsonPatch;
import com.pablomatiasgomez.minesweeper.controller.model.JsonPatchOp;
import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {

	private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

	// Not the cleanest way of implementing the mutations, but fine for now given that there are only
	// two things that can be done: opening a cell, or adding a flag to the cell..
	private static final Pattern OPEN_CELL_PATH = Pattern.compile("^cells/(\\d+)/(\\d+)/opened$");
	private static final Pattern FLAG_CELL_PATH = Pattern.compile("^cells/(\\d+)/(\\d+)/hasFlag$");

	private final JsonTransformer jsonTransformer;
	private final GameService gameService;
	/**
	 * All the possible patch operations that can be done.
	 */
	private final Map<Pattern, PatchCell> patchOperations;

	public GameController(JsonTransformer jsonTransformer, GameService gameService) {
		this.jsonTransformer = jsonTransformer;
		this.gameService = gameService;
		this.patchOperations = ImmutableMap.of(
				OPEN_CELL_PATH, (gameId, row, col, value) -> gameService.openCell(gameId, row, col),
				FLAG_CELL_PATH, (gameId, row, col, value) -> gameService.flagCell(gameId, row, col, (boolean) value));
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

		Spark.get("/api/games/:id", (request, response) -> {
			String gameId = request.params("id");
			LOG.info("Getting game with id {} ..", gameId);
			return new GameResponse(gameService.getGame(gameId));
		}, jsonTransformer);

		Spark.patch("/api/games/:id", (request, response) -> {
			String gameId = request.params("id");
			LOG.info("Patching cell for game with id {} ..", gameId);
			List<JsonPatch> patches = jsonTransformer.readValue(request.body(), new TypeReference<List<JsonPatch>>() {});
			if (patches.size() != 1) {
				throw new IllegalArgumentException("Only 1 patch operation is allowed.");
			}
			JsonPatch patch = patches.get(0);
			if (!JsonPatchOp.REPLACE.equals(patch.getOp())) {
				throw new IllegalArgumentException("Only REPLACE can be patched.");
			}

			return patchOperations.entrySet()
					.stream()
					.map(entry -> {
						Matcher matcher = entry.getKey().matcher(patch.getPath());
						if (!matcher.matches()) {
							return null;
						}
						int row = Integer.parseInt(matcher.group(1));
						int col = Integer.parseInt(matcher.group(2));
						return entry.getValue().patch(gameId, row, col, patch.getValue());
					})
					.filter(Objects::nonNull)
					.map(GameResponse::new)
					.collect(onlyElement());
		}, jsonTransformer);
	}

	@FunctionalInterface
	private interface PatchCell {
		Game patch(String gameId, int row, int col, Object value);
	}

}
