
public class Main {

    public static void main(String[] args) {
        int row = 5;
        int column = 5;

        Board theBoard = new Board(row, column);
        Game playGame = new Game(theBoard);
    }
}
