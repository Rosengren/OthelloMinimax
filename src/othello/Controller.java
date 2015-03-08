package othello;

import AI.AI;
import AI.HeuristicPieceCounter;
import AI.MiniMaxAI;
import AI.MiniMaxAlphaBetaAI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller implements ActionListener {

    private static final int TIME_BETWEEN_MOVES = 1000;

    private static final int ROW = 0;
    private static final int COL = 1;

    private AI miniMaxAI;
    private AI miniMaxAlphaBetaAI;

    private OthelloModel model;
    private TAdapter keyPress;

    public Controller(OthelloModel model) {
        this.model = model;
        miniMaxAI = new MiniMaxAI();
        miniMaxAlphaBetaAI = new MiniMaxAlphaBetaAI();
        keyPress = new TAdapter();
    }

    public KeyAdapter getKeyListener() {
        return keyPress;
    }

    public void playMove(Position pos) {

        AI ai2 = new MiniMaxAI();
        ai2.setStrategy(new HeuristicPieceCounter());
        AI ai = new MiniMaxAlphaBetaAI();
        ai.setStrategy(new HeuristicPieceCounter());

        try {
            model.move(pos);
            ai.playTurn(model);

//            while(model.isRunning()) {
//                ai.playTurn(model);
//                ai2.playTurn(model);

//            }

            System.out.println("DONE");

            int[] score = model.getScore();
            System.out.println("Score: Black = " + score[0] + ", White = " + score[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            try {
                switch(key) {
                    case KeyEvent.VK_ENTER:
                        // restart game here
                        return;
                    case KeyEvent.VK_SPACE:
                        return;
                    case KeyEvent.VK_ESCAPE:
                        return;
                    case KeyEvent.VK_1:
                        miniMaxAI.playTurn(model);
                        return;
                    case KeyEvent.VK_2:
                        miniMaxAlphaBetaAI.playTurn(model);
                        return;
                    case KeyEvent.VK_3:
                        while (model.isRunning()) {
                            miniMaxAI.playTurn(model);
                            Thread.sleep(TIME_BETWEEN_MOVES);
                        }
                        return;
                    case KeyEvent.VK_4:
                        while (model.isRunning()) {
                            miniMaxAlphaBetaAI.playTurn(model);
                            Thread.sleep(TIME_BETWEEN_MOVES);
                        }
                        return;
                    case KeyEvent.VK_5:
                        while (model.isRunning()) {
                            miniMaxAI.playTurn(model);
                            Thread.sleep(TIME_BETWEEN_MOVES);
                            miniMaxAlphaBetaAI.playTurn(model);
                            Thread.sleep(TIME_BETWEEN_MOVES);
                        }
                        return;
                    case KeyEvent.VK_6:
                        return;
                    case KeyEvent.VK_7:
                        return;
                    case KeyEvent.VK_8:
                        return;
                    case KeyEvent.VK_P:
                        miniMaxAI.setStrategy(new HeuristicPieceCounter());
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicPieceCounter());
                        return;
                    default:
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
