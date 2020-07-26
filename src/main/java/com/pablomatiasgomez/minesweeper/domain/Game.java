package com.pablomatiasgomez.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Represents all the data related to a given Game instance, that is:
 * - Metadata like name, number of rows and columns, etc
 * - The matrix itself which contains all the information of the state of each cell.
 */
public class Game extends BaseDbEntity {

	private final String name;
	private final String account;
	private final int rowsCount;
	private final int columnsCount;
	// Access is matrix[row][column]
	private final List<List<Integer>> cells;

	@JsonCreator
	public Game(
			@JsonProperty(value = "name", required = true) String name,
			@JsonProperty(value = "account", required = true) String account,
			@JsonProperty(value = "rowsCount", required = true) int rowsCount,
			@JsonProperty(value = "columnsCount", required = true) int columnsCount,
			@JsonProperty(value = "cells", required = true) List<List<Integer>> cells) {
		this.name = Objects.requireNonNull(name);
		this.account = Objects.requireNonNull(account);
		this.rowsCount = rowsCount;
		this.columnsCount = columnsCount;
		this.cells = Objects.requireNonNull(cells);
	}

	public String getName() {
		return name;
	}

	public String getAccount() {
		return account;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public int getColumnsCount() {
		return columnsCount;
	}

	public List<List<Integer>> getCells() {
		return cells;
	}

}