package com.pablomatiasgomez.minesweeper.service;

import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.domain.GameCell;
import com.pablomatiasgomez.minesweeper.domain.GameStatus;
import com.pablomatiasgomez.minesweeper.repository.GameRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameService {

	public static final double MIN_MINES_RATIO_PER_CELLS = 0.05;
	public static final double MAX_MINES_RATIO_PER_CELLS = 0.5;
	private static final int MIN_ROWS = 5;
	private static final int MAX_ROWS = 30;
	private static final int MIN_COLS = 5;
	private static final int MAX_COLS = 30;
	private static final Random random = new Random();

	private final GameRepository gameRepository;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	/**
	 * Creates a new game with the given configuration.
	 * Mines will be placed in 15% of the cells.
	 *
	 * @return the created {@link Game}
	 */
	public Game createGame(int rowsCount, int colsCount, int minesCount) {
		rowsCount = Math.max(Math.min(rowsCount, MAX_ROWS), MIN_ROWS);
		colsCount = Math.max(Math.min(colsCount, MAX_COLS), MIN_COLS);
		int minMines = (int) Math.floor(rowsCount * colsCount * MIN_MINES_RATIO_PER_CELLS);
		int maxMines = (int) Math.floor(rowsCount * colsCount * MAX_MINES_RATIO_PER_CELLS);
		minesCount = Math.max(Math.min(minesCount, maxMines), minMines);
		List<List<GameCell>> cells = createCells(rowsCount, colsCount, minesCount);
		Game game = new Game(rowsCount, colsCount, minesCount, GameStatus.PLAYING, Instant.now(), 0, cells);
		return gameRepository.createGame(game);
	}

	/**
	 * Get a game by id.
	 */
	public Game getGame(String gameId) {
		return gameRepository.getGame(gameId)
				.orElseThrow(() -> new IllegalArgumentException("Game not found!"));
	}

	/**
	 * Reveals the cell at the given position.
	 * If it has a mine, the game ends with a {@link GameStatus#LOST} status.
	 * If the cell has no adjacent mines, all the adjacent cells are revealed.
	 *
	 * @return the updated {@link Game}
	 */
	public Game revealCell(String gameId, int row, int col) {
		Game game = getGame(gameId);
		if (!GameStatus.PLAYING.equals(game.getStatus())) {
			throw new IllegalStateException("Game is not being played!");
		}
		revealCell(game, row, col);
		if (!GameStatus.FINISHED_STATUES.contains(game.getStatus()) && gameIsAlreadyWon(game)) {
			game.setStatus(GameStatus.WON);
			updateAndStopPlayedGameTime(game);
		}
		return gameRepository.updateGame(game);
	}

	private void revealCell(Game game, int row, int col) {
		GameCell cell = game.getCells().get(row).get(col);
		if (cell.isRevealed()) {
			return;
		}
		cell.setRevealed(true);
		if (cell.getHasMine()) {
			game.setStatus(GameStatus.LOST);
			updateAndStopPlayedGameTime(game);
			return;
		}
		if (cell.getAdjacentMinesCount() == 0) {
			executeForAllAdjacentCells(game.getRowsCount(), game.getColsCount(), row, col, (x, y) -> revealCell(game, x, y));
		}
	}

	public Game flagCell(String gameId, int row, int col, boolean hasFlag) {
		Game game = getGame(gameId);
		GameCell cell = game.getCells().get(row).get(col);
		cell.setHasFlag(hasFlag);
		return gameRepository.updateGame(game);
	}

	/**
	 * Allow pausing/resuming games
	 */
	public Game updateGameStatus(String gameId, GameStatus newStatus) {
		Game game = getGame(gameId);
		if (GameStatus.PLAYING.equals(game.getStatus()) && GameStatus.PAUSED.equals(newStatus)) {
			return pauseGame(game);
		} else if (GameStatus.PAUSED.equals(game.getStatus()) && GameStatus.PLAYING.equals(newStatus)) {
			return resumeGame(game);
		} else {
			throw new IllegalArgumentException("Invalid game status " + newStatus);
		}
	}

	private Game pauseGame(Game game) {
		game.setStatus(GameStatus.PAUSED);
		updateAndStopPlayedGameTime(game);
		return gameRepository.updateGame(game);
	}

	private Game resumeGame(Game game) {
		game.setStatus(GameStatus.PLAYING);
		game.setPlayingSince(Instant.now());
		return gameRepository.updateGame(game);
	}

	private void updateAndStopPlayedGameTime(Game game) {
		long playedTimeMs = Duration.between(Objects.requireNonNull(game.getPlayingSince()), Instant.now()).toMillis();
		game.addPlayedMs(playedTimeMs);
		game.setPlayingSince(null);
	}

	/**
	 * Checks if the given game is already won, which happens if all the cells,
	 * except the ones that have mines, were already revealed.
	 */
	private boolean gameIsAlreadyWon(Game game) {
		for (List<GameCell> cells : game.getCells()) {
			for (GameCell cell : cells) {
				if (!cell.isRevealed() && !cell.getHasMine()) {
					return false;
				}
			}
		}
		return true;
	}

	private List<List<GameCell>> createCells(int rowsCount, int colsCount, int minesCount) {
		// First we create a matrix where all the mines will be placed:
		boolean[][] mines = createRandomMinesMatrix(rowsCount, colsCount, minesCount);

		// Now we create the cells by using the matrix of mines to calculate the adjacent count
		return IntStream.range(0, rowsCount).boxed()
				.map(row -> IntStream.range(0, colsCount).boxed()
						.map(col -> {
							boolean hasMine = mines[row][col];
							// Eventually we could only calculate the count for cells that do not have mines because we don't need the information there.
							int adjacentMinesCount = calculateAdjacentMinesCount(mines, row, col);
							return new GameCell(false, false, hasMine, adjacentMinesCount);
						})
						.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	private boolean[][] createRandomMinesMatrix(int rowsCount, int colsCount, int minesCount) {
		boolean[][] mines = new boolean[rowsCount][colsCount];
		int placedMines = 0;
		while (placedMines < minesCount) {
			int row = random.nextInt(rowsCount);
			int col = random.nextInt(colsCount);
			if (!mines[row][col]) {
				mines[row][col] = true;
				placedMines++;
			}
		}
		return mines;
	}

	/**
	 * Calculates the number of adjacent mines related to the given cell (row and col)
	 * Does not includes the same cell if it has a mine.
	 *
	 * @return the number of adjacent mines.
	 */
	private int calculateAdjacentMinesCount(boolean[][] mines, int row, int col) {
		AtomicInteger count = new AtomicInteger();
		executeForAllAdjacentCells(mines.length, mines[0].length, row, col, (x, y) -> {
			if (mines[x][y]) {
				count.getAndIncrement();
			}
		});
		return count.get();
	}

	/**
	 * Executes the provided function for all the adjacent cells, that is,
	 * if the cell is not in a border, all then 9 cells that are around it, no including it self.
	 */
	private void executeForAllAdjacentCells(int rowsCount, int colsCount, int row, int col, BiConsumer<Integer, Integer> fn) {
		for (int x = row - 1; x <= row + 1; x++) {
			if (x < 0 || x >= rowsCount) {
				continue;
			}
			for (int y = col - 1; y <= col + 1; y++) {
				if (y < 0 || y >= colsCount) {
					continue;
				}
				if (x == row && y == col) {
					continue;
				}
				fn.accept(x, y);
			}
		}
	}

}
