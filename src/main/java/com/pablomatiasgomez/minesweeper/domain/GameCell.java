package com.pablomatiasgomez.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single cell in the game.
 * Does not contain any information related ot it's position because that comes from the position in the matrix.
 */
public class GameCell {

	/**
	 * Whether the user already opened this cell or not which means it's visible to them or not.
	 */
	private final boolean opened;
	private final boolean hasFlag;

	// Private fields (not to be exposed, unless already opened):
	private final boolean hasMine;
	/**
	 * Pre calculated number of adjacent mines, storing it to avoid recalculating it every time, as it doesn't change.
	 */
	private final int adjacentMinesCount;

	@JsonCreator
	public GameCell(
			@JsonProperty(value = "opened", required = true) boolean opened,
			@JsonProperty(value = "hasMine", required = true) boolean hasMine,
			@JsonProperty(value = "hasFlag", required = true) boolean hasFlag,
			@JsonProperty(value = "adjacentMinesCount", required = true) int adjacentMinesCount) {
		this.opened = opened;
		this.hasFlag = hasFlag;
		this.hasMine = hasMine;
		this.adjacentMinesCount = adjacentMinesCount;
	}

	public boolean getOpened() {
		return opened;
	}

	public boolean getHasFlag() {
		return hasFlag;
	}

	public boolean getHasMine() {
		return hasMine;
	}

	public int getAdjacentMinesCount() {
		return adjacentMinesCount;
	}
}