import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Main {
    public static void main(String[] args) {
        JFrame menuFrame = new JFrame("Minesweeper");

        JPanel header = new JPanel();
        JPanel playPanel = new JPanel();
        JPanel size = new JPanel();

        JLabel title = new JLabel("MINESWEEPER");

        JLabel playTitle = new JLabel("Choose the tile size");
        
        SpinnerNumberModel row = new SpinnerNumberModel(10,5,100,1);
        JSpinner rowSpinner = new JSpinner(row);
        rowSpinner.setPreferredSize(new Dimension(50,25));
        
        SpinnerNumberModel column = new SpinnerNumberModel(10,5,200,1);
        JSpinner colSpinner = new JSpinner(column);
        colSpinner.setPreferredSize(new Dimension(50,25));
        
        JButton play = new JButton("Play");
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board board = new Board(((SpinnerNumberModel) rowSpinner.getModel()).getNumber().intValue(), ((SpinnerNumberModel) colSpinner.getModel()).getNumber().intValue());
                menuFrame.setVisible(false);
                Game start = new Game(board, menuFrame);
                start.play(board.board.length,board.board[0].length);
            }
        });

        JButton history = new JButton("Recent wins");
        
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
}
