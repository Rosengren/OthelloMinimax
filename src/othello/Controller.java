package othello;

import AI.AI;
import AI.HeuristicPieceCounter;
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

    public void playMove(Position pos) {

        AI ai = new MiniMaxAI();
        ai.setStrategy(new HeuristicPieceCounter());

        try {
//            model.move(pos);
//            ai.playTurn(model);

            while(model.isRunning()) {
                ai.playTurn(model);
            }

            System.out.println("DONE");

            int[] score = model.getScore();
            System.out.println("Score: Black = " + score[0] + ", White = " + score[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {

            String userInput = e.getActionCommand();

            try {
                String[] str = userInput.split(",");
                int x = Integer.parseInt(str[ROW]);
                int y = Integer.parseInt(str[COL]);

                Position playerMove = new Position(x, y);
                playMove(playerMove);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
