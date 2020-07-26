package com.pablomatiasgomez.minesweeper.controller.model;

import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.domain.GameStatus;

import java.util.List;
import java.util.stream.Collectors;

public class GameResponse {

	private final String id;
	private final int rowsCount;
	private final int colsCount;
	private final int minesCount;
	private final GameStatus status;
	private final List<List<GameCellResponse>> cells;

	public GameResponse(Game game) {
		this.id = game.getId();
		this.rowsCount = game.getRowsCount();
		this.colsCount = game.getColsCount();
		this.minesCount = game.getMinesCount();
		this.status = game.getStatus();
		this.cells = game.getCells().stream()
				.map(cells -> cells.stream()
						.map(cell -> new GameCellResponse(game, cell))
						.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public String getId() {
		return id;
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

	public List<List<GameCellResponse>> getCells() {
		return cells;
	}
}