package com.pablomatiasgomez.minesweeper.service;

import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.domain.GameCell;
import com.pablomatiasgomez.minesweeper.domain.GameStatus;
import com.pablomatiasgomez.minesweeper.repository.GameRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameService {

	public static final double MINES_RATIO_PER_CELLS = 0.15;
	private static final int MIN_ROWS = 10;
	private static final int MAX_ROWS = 30;
	private static final int MIN_COLS = 10;
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
	public Game createGame(int rowsCount, int colsCount) {
		rowsCount = Math.max(Math.min(rowsCount, MAX_ROWS), MIN_ROWS);
		colsCount = Math.max(Math.min(colsCount, MAX_COLS), MIN_COLS);
		int minesCount = numberOfMines(rowsCount, colsCount);
		List<List<GameCell>> cells = createCells(rowsCount, colsCount, minesCount);
		Game game = new Game(rowsCount, colsCount, minesCount, GameStatus.PLAYING, cells);
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
		if (GameStatus.FINISHED_STATUES.contains(game.getStatus())) {
			throw new IllegalStateException("Game is already finished!");
		}
		revealCell(game, row, col);
		if (!GameStatus.FINISHED_STATUES.contains(game.getStatus()) && gameIsAlreadyWon(game)) {
			game.setStatus(GameStatus.WON);
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

	/**
	 * mines count will be, for now, 15%, of the total cells, rounding down.
	 */
	private int numberOfMines(int rowsCount, int colsCount) {
		return (int) Math.floor(rowsCount * colsCount * MINES_RATIO_PER_CELLS);
	}

}
