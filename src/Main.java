
public class Main {

    public static void main(String[] args) {
        int row = 10;
        int column = 10;

        Board theBoard = new Board(row, column);
        Game playGame = new Game(theBoard);
    }
}
