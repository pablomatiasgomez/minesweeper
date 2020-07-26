package com.pablomatiasgomez.minesweeper;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.pablomatiasgomez.minesweeper.controller.model.CreateGameRequest;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

public class GameIntegrationTest {

	@BeforeClass
	public static void startApp() {
		Main.main(new String[]{});
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

}


