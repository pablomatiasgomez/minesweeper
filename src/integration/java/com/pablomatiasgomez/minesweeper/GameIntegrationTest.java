package com.pablomatiasgomez.minesweeper;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.mongodb.client.MongoClient;
import com.pablomatiasgomez.minesweeper.controller.model.CreateGameRequest;
import com.pablomatiasgomez.minesweeper.controller.model.JsonPatch;
import com.pablomatiasgomez.minesweeper.controller.model.JsonPatchOp;
import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.repository.GameRepository;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

public class GameIntegrationTest {

	private static GameRepository gameRepository;

	@BeforeClass
	public static void startApp() {
		Main.main(new String[]{});

		MongoClient mongoClient = Main.getMongoClient(Main.getDatabaseProperties());
		gameRepository = new GameRepository(Main.createObjectMapper(), mongoClient.getDatabase("minesweeper"));
	}

	@Test
	public void testCreateGame() {
		given()
				.contentType(ContentType.JSON)
				.body(new CreateGameRequest(10, 15))
				.when()
				.post("/api/games")
				.then()
				.statusCode(HttpServletResponse.SC_OK)
				.body("id", notNullValue())
				.body("rowsCount", equalTo(10))
				.body("colsCount", equalTo(15))
				.body("minesCount", equalTo(22))
				.body("status", equalTo("PLAYING"))
				.body("cells", hasSize(10))
				.body("cells[1]", hasSize(15))
				.body("cells", hasItem(hasItem(allOf(
						hasEntry("opened", Boolean.FALSE),
						hasEntry("hasFlag", Boolean.FALSE)))));
	}

	@Test
	public void testGetById() {
		String gameId = given()
				.contentType(ContentType.JSON)
				.body(new CreateGameRequest(15, 20))
				.when()
				.post("/api/games")
				.then()
				.statusCode(HttpServletResponse.SC_OK)
				.extract().body().jsonPath().getString("id");

		when()
				.get("/api/games/" + gameId)
				.then()
				.statusCode(HttpServletResponse.SC_OK)
				.body("id", equalTo(gameId))
				.body("rowsCount", equalTo(15))
				.body("colsCount", equalTo(20))
				.body("minesCount", equalTo(45));
	}

	@Test
	public void testOpenCell() {
		String gameId = given()
				.contentType(ContentType.JSON)
				.body(new CreateGameRequest(15, 20))
				.when()
				.post("/api/games")
				.then()
				.statusCode(HttpServletResponse.SC_OK)
				.extract().body().jsonPath().getString("id");

		// Get the game from the database so that we know where the mines are and where not..
		Game game = getGame(gameId);
		Pair<Integer, Integer> cellWithNoMine = getCellWithNoMine(game);

		Integer row = cellWithNoMine.getKey();
		Integer col = cellWithNoMine.getRight();
		String path = "cells/" + row + "/" + col + "/opened";
		given()
				.contentType(ContentType.JSON)
				.body(Collections.singletonList(new JsonPatch(JsonPatchOp.REPLACE, path, Boolean.TRUE)))
				.patch("/api/games/" + gameId)
				.then()
				.statusCode(HttpServletResponse.SC_OK)
				.body("id", equalTo(gameId))
				.body("cells[" + row + "][" + col + "].opened", equalTo(true))
				.body("cells[" + row + "][" + col + "].hasFlag", equalTo(false))
				// The cell is now reveled so we are able to see if it has a mine and the number of adjacent mines.
				.body("cells[" + row + "][" + col + "].hasMine", equalTo(false))
				.body("cells[" + row + "][" + col + "].adjacentMinesCount", notNullValue());
	}

	private Pair<Integer, Integer> getCellWithNoMine(Game game) {
		for (int row = 0; row < game.getRowsCount(); row++) {
			for (int col = 0; col < game.getColsCount(); col++) {
				if (!game.getCells().get(row).get(col).getHasMine()) {
					return Pair.of(row, col);
				}
			}
		}
		throw new IllegalStateException("Can't find a cell without a mine.");
	}

	private Game getGame(String id) {
		return gameRepository.getGame(id).orElseThrow();
	}

}


