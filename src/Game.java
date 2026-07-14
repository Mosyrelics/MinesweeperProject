
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {

    private final Board gameBoard;
    private boolean finished;
    private boolean won;
    private JFrame frame;
    private JButton[][] tiles;

    public Game(Board gameBoard) {
        this.gameBoard = gameBoard;
        this.finished = false;
        this.won = true;
        play(gameBoard.board.length, gameBoard.board[0].length);
    }

    private void play(int row, int column) {
        frame = new JFrame("Minesweeper");
        tiles = new JButton[row][column];
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new GridLayout(row, column));

        frame.setSize(50*row, 50*column);
        frame.setLocationRelativeTo(null);

        for(int i = 0 ; i < row ; i++){
            for(int j = 0 ; j < column ; j++){
                tiles[i][j] = makeTiles(i, j);
                frame.add(tiles[i][j]);
            }
        }
        frame.setVisible(true);

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
            finished = true;
            return;
        }
        finished = true;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (gameBoard.board[i][j].revealed == false && gameBoard.board[i][j].getMine() == false) {
                    finished = false;
                    return;
                }
            }
        }
        System.out.println("\nYou won!");
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
        
        if(gameBoard.board[row][column].getAdjacent() != 0){
            tiles[row][column].setText(String.valueOf(gameBoard.board[row][column].getAdjacent()));
            tiles[row][column].setForeground(Color.BLACK);
        }

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

    private JButton makeTiles(int row, int column){
        JButton button = new JButton();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                if(SwingUtilities.isLeftMouseButton(e)){
                    leftClick(row, column);
                }

                if(SwingUtilities.isRightMouseButton(e)){
                    rightClick(row, column);
                }
            }
        });
        return button;
    }

    private void leftClick(int row, int column){
        if(finished == true){
            return;
        }
        if(gameBoard.board[row][column].flag == true){
            System.out.println("This cell is flagged!");
            return;
        }
        if(gameBoard.board[row][column].flag == false){
            reveal(row, column);
            checkFinished(gameBoard.board.length, gameBoard.board[0].length);
            refreshBoard(gameBoard.board.length, gameBoard.board[0].length);
            ifLost(gameBoard.board.length, gameBoard.board[0].length);
        }

    }

    private void rightClick(int row, int column){
        if(finished == true){
            return;
        }
        if(gameBoard.board[row][column].revealed == false){
            gameBoard.board[row][column].setFlag();
            if(gameBoard.board[row][column].flag == false){
                tiles[row][column].setText("");
            } else {
                tiles[row][column].setText("F");
                tiles[row][column].setForeground(Color.GREEN);
            }
        }
    }

    private void ifLost(int row, int column){
        if(won == false){
            for(int i = 0 ; i < row ; i++){
                for(int j = 0 ; j < column ; j++){
                    if(gameBoard.board[i][j].getMine()){
                        if(gameBoard.board[i][j].flag == false){
                            tiles[i][j].setText("x");
                            tiles[i][j].setForeground(Color.RED);
                        }
                    }
                    if(!gameBoard.board[i][j].getMine() && gameBoard.board[i][j].flag == true){
                        tiles[i][j].setText("Z");
                        tiles[i][j].setForeground(Color.PINK);
                    }
                }
            }
        }
    }

    private void refreshBoard(int row, int column){
        for(int i = 0 ; i < row ; i++){
            for(int j = 0 ; j < column ; j++){
            if(gameBoard.board[i][j].revealed == true){
                    tiles[i][j].setText(String.valueOf(gameBoard.board[i][j].getAdjacent()));
                    tiles[i][j].setForeground(Color.BLACK);
                }
            }
        }
    }
}
