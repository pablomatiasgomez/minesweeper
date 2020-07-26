package com.pablomatiasgomez.minesweeper.controller.model;

import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.domain.GameCell;
import com.pablomatiasgomez.minesweeper.domain.GameStatus;

import javax.annotation.Nullable;

public class GameCellResponse {

	private final boolean revealed;
	private final boolean hasFlag;
	@Nullable
	private final Boolean hasMine;
	@Nullable
	private final Integer adjacentMinesCount;

	public GameCellResponse(Game game, GameCell gameCell) {
		this.revealed = gameCell.isRevealed();
		this.hasFlag = gameCell.getHasFlag();
		// Only expose the mines information if the cell was revealed, or game ended:
		if (GameStatus.FINISHED_STATUES.contains(game.getStatus()) || gameCell.isRevealed()) {
			this.hasMine = gameCell.getHasMine();
			this.adjacentMinesCount = gameCell.getAdjacentMinesCount();
		} else {
			this.hasMine = null;
			this.adjacentMinesCount = null;
		}
	}

	public boolean getRevealed() {
		return revealed;
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