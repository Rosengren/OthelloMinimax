package othello;

import AI.MiniMaxAI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private static final int ROW = 0;
    private static final int COL = 1;

    private OthelloModel model;

    public Controller(OthelloModel model) {
        this.model = model;
    }

    public void playMove(int row, int col) {

        MiniMaxAI ai = new MiniMaxAI();

        try {
            model.move(row, col);
            ai.playTurn(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Play Move: " + row + "," + col);
//                model.makeMove(row, col);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {

            String userInput = e.getActionCommand();

            try {
                String[] str = userInput.split(",");
                int row = Integer.parseInt(str[ROW]);
                int col = Integer.parseInt(str[COL]);

                playMove(row, col);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
