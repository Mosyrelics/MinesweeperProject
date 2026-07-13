
import java.util.Scanner;

public class Game {

    private final Board gameBoard;
    private boolean finished;
    private boolean won;

    public Game(Board gameBoard) {
        this.gameBoard = gameBoard;
        this.finished = false;
        this.won = true;
        play();
    }

    private void play() {
        Scanner in = new Scanner(System.in);
        System.out.print("Mine total: ");
        System.out.println(gameBoard.mines);
        while (!finished) {
            minimap(gameBoard.board.length, gameBoard.board[0].length);
            System.out.println();
            System.out.print("Row: ");
            int row = in.nextInt();
            while (row > gameBoard.board.length || row <= 0) {
                System.out.print("Please input again. Row: ");
                row = in.nextInt();
            }
            System.out.print("Column: ");
            int column = in.nextInt();
            while (column > gameBoard.board[0].length || column <= 0) {
                System.out.print("Please input again. Column: ");
                column = in.nextInt() - 1;
            }
            reveal(row - 1, column - 1);
            checkFinished(gameBoard.board.length, gameBoard.board[0].length);
        }
        in.close();
    }

    public void reveal(int row, int column) {
        if (gameBoard.board[row][column].revealed == false) {
            if (gameBoard.board[row][column].getMine()) {
                won = false;
            } else {
                if (gameBoard.board[row][column].getAdjacent() == 0) {
                    revealCells(row, column);
                } else {
                    gameBoard.board[row][column].reveal();
                }
            }
        } else {
            System.out.println("This mine has been revealed already!");
        }
    }

    public void checkFinished(int row, int column) {
        if(won == false){
            System.out.println("\nYou lose!");
            revealFullmap(gameBoard.board.length, gameBoard.board[0].length);
            finished = true;
        } else {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    finished = true;
                    if (gameBoard.board[i][j].revealed == false && gameBoard.board[i][j].getMine() == false) {
                        finished = false;
                    }
                }
            }
            if(finished == true){
                System.out.println("\nYou won!");
                revealFullmap(gameBoard.board.length, gameBoard.board[0].length);
            }
        }
    }

    public void minimap(int row, int column) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (gameBoard.board[i][j].revealed == false) {
                    System.out.print("-");
                } else if (!gameBoard.board[i][j].getMine()) {
                    System.out.print(gameBoard.board[i][j].getAdjacent());
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void revealFullmap(int row, int column) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (gameBoard.board[i][j].revealed == false && !gameBoard.board[i][j].getMine()) {
                    System.out.print("  ");
                    continue;
                }
                if (gameBoard.board[i][j].getMine()) {
                    System.out.print("x");
                } else {
                    System.out.print(gameBoard.board[i][j].getAdjacent());
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void revealCells(int row, int column) {
        if (row < 0 || column < 0 || row >= gameBoard.board.length || column >= gameBoard.board[0].length) {
            return;
        }

        if (gameBoard.board[row][column].revealed == true) {
            return;
        }

        if (gameBoard.board[row][column].getMine()) {
            return;
        }

        gameBoard.board[row][column].reveal();

        if (gameBoard.board[row][column].getAdjacent() != 0) {
            return;
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                revealCells(row + i, column + j);
            }
        }
    }
}
