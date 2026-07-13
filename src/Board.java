package Minesweeper.src;

import java.util.Random;

public class Board {

    private final Cell[][] board;
    private final int mines;

    public Board(int row, int column) {
        Random random = new Random();

        board = new Cell[row][column];
        mines = (row * column) / 10;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = new Cell(false);
            }
        }

        int tempMines = mines;
        while (tempMines > 0) {
            int randomRow = random.nextInt(row);
            int randomColumn = random.nextInt(column);
            if (!board[randomRow][randomColumn].getMine()) {
                board[randomRow][randomColumn] = new Cell(true);
                tempMines -= 1;
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j].setAdjacent(calculateAdjacent(i, j));
            }
        }
    }

    private int calculateAdjacent(int row, int column) {
        int n = 0;
        if (board[row - 1][column].getMine()) {
            n += 1;
        }
        if (board[row + 1][column].getMine()) {
            n += 1;
        }
        if (board[row][column - 1].getMine()) {
            n += 1;
        }
        if (board[row][column + 1].getMine()) {
            n += 1;
        }
        if (board[row - 1][column - 1].getMine()) {
            n += 1;
        }
        if (board[row - 1][column + 1].getMine()) {
            n += 1;
        }
        if (board[row + 1][column - 1].getMine()) {
            n += 1;
        }
        if (board[row + 1][column + 1].getMine()) {
            n += 1;
        }
        return n;
    }
}
