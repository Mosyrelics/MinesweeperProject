package Minesweeper.src;

public class Cell {

    private final boolean mine;
    public boolean revealed = false;
    private int adjacent;

    public Cell(boolean mine) {
        this.mine = mine;
    }

    public boolean getMine() {
        return this.mine;
    }

    public int getAdjacent() {
        return this.adjacent;
    }

    public void setAdjacent(int n) {
        this.adjacent = n;
    }

    public void reveal() {
        if (this.revealed == true) {
            System.out.println("This mine has already been revealed!");
        } else {
            this.revealed = true;
        }
    }
}
