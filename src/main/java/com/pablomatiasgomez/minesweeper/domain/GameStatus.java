package com.pablomatiasgomez.minesweeper.domain;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public enum GameStatus {
	PLAYING,
	PAUSED,
	LOST,
	WON,
	;

	public static Set<GameStatus> FINISHED_STATUES = ImmutableSet.of(LOST, WON);

}
