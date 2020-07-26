package com.pablomatiasgomez.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Represents all the data related to a given Game instance, that is:
 * - Metadata like status, number of rows and cols, etc.
 * - The matrix itself which contains all the information of the state of each cell.
 */
public class Game extends BaseDbEntity {

	private final int rowsCount;
	private final int colsCount;
	private final int minesCount;
	private GameStatus status;
	// Access is matrix[row][col]
	private final List<List<GameCell>> cells;

	@JsonCreator
	public Game(
			@JsonProperty(value = "rowsCount", required = true) int rowsCount,
			@JsonProperty(value = "colsCount", required = true) int colsCount,
			@JsonProperty(value = "minesCount", required = true) int minesCount,
			@JsonProperty(value = "status", required = true) GameStatus status,
			@JsonProperty(value = "cells", required = true) List<List<GameCell>> cells) {
		this.rowsCount = rowsCount;
		this.colsCount = colsCount;
		this.minesCount = minesCount;
		this.status = Objects.requireNonNull(status);
		this.cells = Objects.requireNonNull(cells);
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

	public GameStatus getStatus() {
		return status;
	}

	public Game setStatus(GameStatus status) {
		this.status = status;
		return this;
	}

	public List<List<GameCell>> getCells() {
		return cells;
	}
}