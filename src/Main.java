
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main {

    private static JFrame historyFrame;
    private static JLabel historyTitle;
    private static JTextArea histories;
    private static JButton historyBack;
    private static JFrame menuFrame;

    public static void main(String[] args) {
        menuFrame = new JFrame("Minesweeper");

        JPanel header = new JPanel();
        JPanel playPanel = new JPanel();
        JPanel size = new JPanel();
        JLabel title = new JLabel("MINESWEEPER");
        JLabel playTitle = new JLabel("Choose the tile size");

        SpinnerNumberModel row = new SpinnerNumberModel(10, 5, 100, 1);
        JSpinner rowSpinner = new JSpinner(row);
        rowSpinner.setPreferredSize(new Dimension(50, 25));

        SpinnerNumberModel column = new SpinnerNumberModel(10, 5, 200, 1);
        JSpinner colSpinner = new JSpinner(column);
        colSpinner.setPreferredSize(new Dimension(50, 25));

        JButton play = new JButton("Play");
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board board = new Board(((SpinnerNumberModel) rowSpinner.getModel()).getNumber().intValue(), ((SpinnerNumberModel) colSpinner.getModel()).getNumber().intValue());
                menuFrame.setVisible(false);
                Game start = new Game(board, menuFrame);
                start.play(board.board.length, board.board[0].length);
            }
        });

        JButton history = new JButton("Win history");
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistory();
                menuFrame.setVisible(false);
            }
        });

        header.add(title);

        size.add(rowSpinner);
        size.add(colSpinner);

        playPanel.add(playTitle);
        playPanel.add(size);
        playPanel.add(play);

        menuFrame.add(header, BorderLayout.NORTH);
        menuFrame.add(playPanel, BorderLayout.CENTER);
        menuFrame.add(history, BorderLayout.SOUTH);

        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 200);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setResizable(false);
        menuFrame.setVisible(true);
    }

    public static void showHistory() {
        historyFrame = new JFrame("Win History");
        historyFrame.setLayout(new BorderLayout());
        historyFrame.setSize(400, 600);
        historyFrame.setMinimumSize(new Dimension(400, 200));
        historyTitle = new JLabel("History", SwingConstants.CENTER);
        historyTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        histories = new JTextArea();
        histories.setLineWrap(true);
        histories.setWrapStyleWord(true);
        histories.setMargin(new Insets(20, 20, 20, 20));
        histories.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(histories);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        try (BufferedReader reader = new BufferedReader(new FileReader("history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                histories.append(line + "\n");
            }
        } catch (IOException e) {
            histories.setText("Error reading file:\n" + e.getMessage());
        }

        historyBack = new JButton("Back to menu");
        historyBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.setVisible(true);
                historyFrame.dispose();
            }
        });
        historyFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                menuFrame.setVisible(true);
            }
        });

        historyFrame.add(historyTitle, BorderLayout.NORTH);
        historyFrame.add(scrollPane, BorderLayout.CENTER);
        historyFrame.add(historyBack, BorderLayout.SOUTH);
        historyFrame.setLocationRelativeTo(null);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setVisible(true);
    }
}
