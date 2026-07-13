
import java.util.Random;

public class Board {

    public final Cell[][] board;
    public final int mines;

    public Board(int row, int column) {
        Random random = new Random();

        board = new Cell[row][column];
        mines = (row * column) / 6;

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
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int temprow = row + i;
                int tempcol = column + j;

                if (temprow >= 0 && temprow < board.length) {
                    if (tempcol >= 0 && tempcol < board[0].length) {
                        if (board[temprow][tempcol].getMine()) {
                            n++;
                        }
                    }
                }
            }
        }
        return n;
    }
}
