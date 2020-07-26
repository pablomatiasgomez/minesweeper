package com.pablomatiasgomez.minesweeper.controller.model;

import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.domain.GameCell;
import com.pablomatiasgomez.minesweeper.domain.GameStatus;

import javax.annotation.Nullable;

public class GameCellResponse {

	private final boolean opened;
	private final boolean hasFlag;
	@Nullable
	private final Boolean hasMine;
	@Nullable
	private final Integer adjacentMinesCount;

	public GameCellResponse(Game game, GameCell gameCell) {
		this.opened = gameCell.isOpened();
		this.hasFlag = gameCell.getHasFlag();
		// Only expose the mines information if the cell was opened, or game ended:
		if (GameStatus.FINISHED_STATUES.contains(game.getStatus()) || gameCell.isOpened()) {
			this.hasMine = gameCell.getHasMine();
			this.adjacentMinesCount = gameCell.getAdjacentMinesCount();
		} else {
			this.hasMine = null;
			this.adjacentMinesCount = null;
		}
	}

	public boolean getOpened() {
		return opened;
	}

	public boolean getHasFlag() {
		return hasFlag;
	}

	@Nullable
	public Boolean getHasMine() {
		return hasMine;
	}

	@Nullable
	public Integer getAdjacentMinesCount() {
		return adjacentMinesCount;
	}
}