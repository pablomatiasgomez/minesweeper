package com.pablomatiasgomez.minesweeper.domain;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

// TODO add PAUSED status and handle timing
public enum GameStatus {
	PLAYING,
	LOST,
	WON,
	;

	public static Set<GameStatus> FINISHED_STATUES = ImmutableSet.of(LOST, WON);

}
