
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Game {

    private final Board gameBoard;
    private boolean finished;
    private boolean won;
    private int flagCount;
    private JFrame frame;
    private JPanel mineFrame;
    private JPanel information;
    private JButton[][] tiles;
    private JFrame menuFrame;
    private JLabel flags;
    private JButton restart;
    private JLabel timer;
    private JDialog result;
    private boolean restarting;
    private int tileSize;
    private Timer gameTimer;
    private double elapsed;
    private boolean timerStarted;
    private JPanel time;
    private JLabel timeTaken;
    private JPanel name;
    private JLabel nameText;
    private JTextField player;
    private JPanel resultAction;
    private JButton save;
    private JButton noSave;

    public Game(Board gameBoard, JFrame menuFrame) {
        this.gameBoard = gameBoard;
        this.finished = false;
        this.menuFrame = menuFrame;
        this.won = true;
        this.flagCount = gameBoard.mines;
        this.restarting = false;
        this.elapsed = 0.0;
        this.timerStarted = false;
    }

    public void play(int row, int column) {
        frame = new JFrame("Minesweeper");
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        mineFrame = new JPanel();
        tiles = new JButton[row][column];
        information = new JPanel(new GridBagLayout());

        tileSize = Math.min(40, 2500 / Math.max(row, column));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mineFrame.setPreferredSize(new Dimension(tileSize * column, tileSize * row));

        mineFrame.setLayout(new GridLayout(row, column));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (restarting == false) {
                    menuFrame.setVisible(true);
                }
            }
        });

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                tiles[i][j] = makeTiles(i, j);
                mineFrame.add(tiles[i][j]);
            }
        }

        information.setPreferredSize(new Dimension(tileSize * column, 2 * tileSize));

        flags = new JLabel("Flags: " + flagCount + "  ");
        restart = new JButton("Restart");
        restart.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    restarting = true;
                    frame.dispose();

                    Board newBoard = new Board(gameBoard.board.length, gameBoard.board[0].length);

                    Game newGame = new Game(newBoard, menuFrame);
                    newGame.play(newBoard.board.length, newBoard.board[0].length);
                }
            }
        });

        timer = new JLabel("  Time: 0");

        gameTimer = new Timer(10, e -> {
            elapsed += 0.01;
            timer.setText(String.format("  Time: %.2f", elapsed));
        });

        information.add(flags);
        information.add(restart);
        information.add(timer);

        frame.add(mineFrame, BorderLayout.CENTER);
        frame.add(information, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
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
        }
    }

    public void checkFinished(int row, int column) {
        if (won == false) {
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

        gameTimer.stop();
        showResult(elapsed);
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
            tiles[row][column].setText(String.valueOf(gameBoard.board[row][column].getAdjacent()));
            tiles[row][column].setForeground(Color.BLACK);
            tiles[row][column].setBackground(Color.WHITE);
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

    private JButton makeTiles(int row, int column) {
        JButton button = new JButton();
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBackground(Color.WHITE);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(8, tileSize / 2)));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClick(row, column);
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    rightClick(row, column);
                }
            }
        });
        return button;
    }

    private void leftClick(int row, int column) {
        if (finished == true) {
            return;
        }

        if (gameBoard.board[row][column].flag == true) {
            return;
        }

        if (timerStarted == false) {
            timerStarted = true;
            gameTimer.start();
        }

        if (gameBoard.board[row][column].flag == false) {
            reveal(row, column);
            refreshBoard(gameBoard.board.length, gameBoard.board[0].length);
            checkFinished(gameBoard.board.length, gameBoard.board[0].length);
            ifLost(gameBoard.board.length, gameBoard.board[0].length);
        }
    }

    private void rightClick(int row, int column) {
        if (finished == true) {
            return;
        }
        if (gameBoard.board[row][column].revealed == false) {
            gameBoard.board[row][column].setFlag();
            if (gameBoard.board[row][column].flag == false) {
                flagCount++;
                tiles[row][column].setBackground(Color.WHITE);
                flags.setText("Flags: " + flagCount + "  ");
            } else {
                flagCount--;
                tiles[row][column].setBackground(Color.GREEN);
                flags.setText("Flags: " + flagCount + "  ");
            }
        }
    }

    private void ifLost(int row, int column) {
        if (won == false) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (gameBoard.board[i][j].getMine()) {
                        if (gameBoard.board[i][j].flag == false) {
                            tiles[i][j].setBackground(Color.RED);
                        }
                    }
                    if (!gameBoard.board[i][j].getMine() && gameBoard.board[i][j].flag == true) {
                        tiles[i][j].setBackground(Color.PINK);
                    }
                    gameTimer.stop();
                }
            }
        }
    }

    private void refreshBoard(int row, int column) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (gameBoard.board[i][j].revealed == true) {
                    tiles[i][j].setText(String.valueOf(gameBoard.board[i][j].getAdjacent()));
                    tiles[i][j].setForeground(Color.BLACK);
                    tiles[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private void showResult(double timer) {
        result = new JDialog(frame, "You won!", true);
        result.setLayout(new BorderLayout());
        time = new JPanel();
        name = new JPanel();
        resultAction = new JPanel();
        timeTaken = new JLabel(String.format("  Time taken: %.2f", elapsed));
        time.add(timeTaken);
        nameText = new JLabel("Name: ");
        player = new JTextField(15);
        name.add(nameText);
        name.add(player);
        noSave = new JButton("Don't Save");
        noSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.dispose();
            }
        });
        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("history.txt", true))) {
                    writer.write(player.getText() + ": " + gameBoard.board.length + "x" + gameBoard.board[0].length + " - " + String.format("%.2f", elapsed));
                    writer.newLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                result.dispose();
            }
        });
        resultAction.add(noSave);
        resultAction.add(save);
        result.add(time, BorderLayout.NORTH);
        result.add(name, BorderLayout.CENTER);
        result.add(resultAction, BorderLayout.SOUTH);
        result.setResizable(false);
        result.setSize(300, 150);
        result.setLocationRelativeTo(frame);
        result.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        result.setVisible(true);
    }
}
