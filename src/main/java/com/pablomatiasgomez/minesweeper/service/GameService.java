package com.pablomatiasgomez.minesweeper.service;

import com.pablomatiasgomez.minesweeper.domain.Game;
import com.pablomatiasgomez.minesweeper.domain.GameCell;
import com.pablomatiasgomez.minesweeper.domain.GameStatus;
import com.pablomatiasgomez.minesweeper.repository.GameRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameService {

	private static final int MAX_ROWS = 30;
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
		rowsCount = Math.min(rowsCount, MAX_ROWS);
		colsCount = Math.min(colsCount, MAX_COLS);
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

	public Game openCell(String gameId, int row, int col) {
		Game game = getGame(gameId);
		GameCell cell = game.getCells().get(row).get(col);
		if (cell.isOpened()) {
			throw new IllegalStateException("Cell is already opened!");
		}
		cell.setOpened(true);
		if (cell.getHasMine()) {
			game.setStatus(GameStatus.LOST);
		}
		// TODO handle win case (mines == 0?)
		if (cell.getAdjacentMinesCount() == 0) {
			// TODO open all adjacent mines.
		}
		return gameRepository.updateGame(game);
	}

	public Game flagCell(String gameId, int row, int col, boolean hasFlag) {
		Game game = getGame(gameId);
		GameCell cell = game.getCells().get(row).get(col);
		cell.setHasFlag(hasFlag);
		return gameRepository.updateGame(game);
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
		int count = 0;
		for (int x = row - 1; x <= row + 1; x++) {
			if (x < 0 || x >= mines.length) {
				continue;
			}
			for (int y = col - 1; y <= col + 1; y++) {
				if (y < 0 || y >= mines[x].length) {
					continue;
				}
				if (x == row && y == col) {
					continue;
				}
				if (mines[x][y]) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * mines count will be, for now, 15%, of the total cells, rounding down.
	 *
	 * @return the number of mines that the game will have.
	 */
	private int numberOfMines(int rowsCount, int colsCount) {
		return (int) Math.floor(rowsCount * colsCount * 0.15);
	}

}
