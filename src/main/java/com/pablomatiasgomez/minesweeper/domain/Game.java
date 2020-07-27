package com.pablomatiasgomez.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

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
	@Nullable
	private Instant playingSince;
	private long playedMs;
	// Access is cells[row][col]
	private final List<List<GameCell>> cells;

	@JsonCreator
	public Game(
			@JsonProperty(value = "rowsCount", required = true) int rowsCount,
			@JsonProperty(value = "colsCount", required = true) int colsCount,
			@JsonProperty(value = "minesCount", required = true) int minesCount,
			@JsonProperty(value = "status", required = true) GameStatus status,
			@JsonProperty(value = "playingSince") @Nullable Instant playingSince,
			@JsonProperty(value = "playedMs", required = true) long playedMs,
			@JsonProperty(value = "cells", required = true) List<List<GameCell>> cells) {
		this.rowsCount = rowsCount;
		this.colsCount = colsCount;
		this.minesCount = minesCount;
		this.status = Objects.requireNonNull(status);
		this.playingSince = playingSince;
		this.playedMs = playedMs;
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

	/**
	 * The {@link Instant} since the game is in status {@link GameStatus#PLAYING},
	 * or {@code null} if the game is in a different status.
	 * This, along with {@link #playedMs} should be used to count the total time spent in this game.
	 */
	@Nullable
	public Instant getPlayingSince() {
		return playingSince;
	}

	public Game setPlayingSince(@Nullable Instant playingSince) {
		this.playingSince = playingSince;
		return this;
	}

	public long getPlayedMs() {
		return playedMs;
	}

	public Game addPlayedMs(long playedTimeMs) {
		this.playedMs += playedTimeMs;
		return this;
	}

	public List<List<GameCell>> getCells() {
		return cells;
	}

}