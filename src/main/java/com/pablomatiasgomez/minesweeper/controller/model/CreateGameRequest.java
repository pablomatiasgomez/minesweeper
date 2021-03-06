package com.pablomatiasgomez.minesweeper.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGameRequest {

	private final int rowsCount;
	private final int colsCount;
	private final int minesCount;

	@JsonCreator
	public CreateGameRequest(
			@JsonProperty(value = "rowsCount", required = true) int rowsCount,
			@JsonProperty(value = "colsCount", required = true) int colsCount,
			@JsonProperty(value = "minesCount", required = true) int minesCount) {
		this.rowsCount = rowsCount;
		this.colsCount = colsCount;
		this.minesCount = minesCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public int getColsCount() {
		return colsCount;
	}

	public int getMinesCount() {
		return minesCount;
	}
}
