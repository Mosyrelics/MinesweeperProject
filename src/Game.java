
public class Game {

    private Board board;

    public void reveal(int row, int column) {
        if (board[row][column].revealed == false) {
            if (board[row][column].getMine()) {
                System.out.println("Game Over!");
            } else {
                board[row][column].reveal();
            }
        } else {
            System.out.println("This mine has been revealed already!");
        }
    }
}
