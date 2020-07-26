package com.pablomatiasgomez.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single cell in the game.
 * Does not contain any information related to it's position because that comes from the position in the matrix.
 */
public class GameCell {

	/**
	 * Whether the user already opened this cell or not which means it's visible to them or not.
	 */
	private boolean opened;
	private boolean hasFlag;

	// Private fields (not to be exposed, unless already opened):
	private final boolean hasMine;
	/**
	 * Pre calculated number of adjacent mines, storing it to avoid recalculating it every time, as it doesn't change.
	 */
	private final int adjacentMinesCount;

	@JsonCreator
	public GameCell(
			@JsonProperty(value = "opened", required = true) boolean opened,
			@JsonProperty(value = "hasFlag", required = true) boolean hasFlag,
			@JsonProperty(value = "hasMine", required = true) boolean hasMine,
			@JsonProperty(value = "adjacentMinesCount", required = true) int adjacentMinesCount) {
		this.opened = opened;
		this.hasFlag = hasFlag;
		this.hasMine = hasMine;
		this.adjacentMinesCount = adjacentMinesCount;
	}

	public boolean isOpened() {
		return opened;
	}

	public GameCell setOpened(boolean opened) {
		this.opened = opened;
		return this;
	}

	public boolean getHasFlag() {
		return hasFlag;
	}

	public GameCell setHasFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
		return this;
	}

	public boolean getHasMine() {
		return hasMine;
	}

	public int getAdjacentMinesCount() {
		return adjacentMinesCount;
	}
}
