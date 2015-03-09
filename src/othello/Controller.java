package othello;

import AI.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class Controller implements ActionListener {

//    private static final int TIME_BETWEEN_MOVES = 1000;
    private static final int DELAY = 50;

    private Queue<Position> queue;

    private static final int ROW = 0;
    private static final int COL = 1;

    private AI miniMaxAI;
    private AI miniMaxAlphaBetaAI;

    private boolean playAgainstUtilityHeuristic;
    private boolean playAgainstPieceCounterHeuristic;

    private OthelloModel model;
    private TAdapter keyPress;

    public Controller(OthelloModel model) {
        playAgainstUtilityHeuristic = false;
        playAgainstPieceCounterHeuristic = false;
        this.model = model;
        miniMaxAI = new MiniMaxAI();
        miniMaxAlphaBetaAI = new MiniMaxAlphaBetaAI();
        keyPress = new TAdapter();
        queue = new LinkedList<Position>();
        setTimer();

    }

    public KeyAdapter getKeyListener() {
        return keyPress;
    }


    private void setTimer() {
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void playMove(Position pos) {

        try {
            if (model.isRunning())
                model.move(pos);

        } catch (Exception ignored) {}
    }


    private class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            try {
                switch(key) {
                    case KeyEvent.VK_ENTER:
                        model.resetGame();
                        return;
                    case KeyEvent.VK_SPACE:
                        return;
                    case KeyEvent.VK_ESCAPE:
                        return;
                    case KeyEvent.VK_1:
                        playMove(miniMaxAI.selectMove(model));
                        model.highlightTiles(miniMaxAI.getPreviouslyVisitedNodes());
                        printMsg("Nodes Visited (MiniMax): " + miniMaxAI.getNumOfPositionsVisitedLastMove());
                        return;
                    case KeyEvent.VK_2:
                        playMove(miniMaxAlphaBetaAI.selectMove(model));
                        model.highlightTiles(miniMaxAlphaBetaAI.getPreviouslyVisitedNodes());
                        printMsg("Nodes Visited (MiniMax Alpha-Beta): " + miniMaxAlphaBetaAI.getNumOfPositionsVisitedLastMove());
                        return;
                    case KeyEvent.VK_3:
                        while (model.isRunning()) {
                            playMove(miniMaxAI.selectMove(model));
                        }
                        return;
                    case KeyEvent.VK_4:
                        while (model.isRunning()) {
                            playMove(miniMaxAlphaBetaAI.selectMove(model));
                        }
                        return;
                    case KeyEvent.VK_5:
                        printMsg("MiniMaxAI = BLACK, MiniMaxAlphaBetaAI = WHITE");
                        while (model.isRunning()) {
                            playMove(miniMaxAI.selectMove(model));
                            playMove(miniMaxAlphaBetaAI.selectMove(model));
                        }
                        return;
                    case KeyEvent.VK_6:
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicPieceCounter());
                        playAgainstPieceCounterHeuristic = !playAgainstPieceCounterHeuristic;
                        playAgainstUtilityHeuristic = false;
                        return;
                    case KeyEvent.VK_7:
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicUtility());
                        playAgainstUtilityHeuristic = !playAgainstUtilityHeuristic;
                        playAgainstPieceCounterHeuristic = false;
                        return;
                    case KeyEvent.VK_8:
                        return;
                    case KeyEvent.VK_P:
                        miniMaxAI.setStrategy(new HeuristicPieceCounter());
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicPieceCounter());
                        printMsg("Heuristics set to: Piece Counter");
                        return;
                    case KeyEvent.VK_U:
                        miniMaxAI.setStrategy(new HeuristicUtility());
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicUtility());
                        printMsg("Heuristics set to: Utility");
                        return;
                    case KeyEvent.VK_T:
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicUtility());
                        printMsg("MiniMax Alpha-Beta Heuristic set to: Utility");
                        return;
                    default:
                        break;
                }
            } catch (Exception ignored) {

            }
        }
    }

    private void printMsg(String msg) {
        System.out.println(msg);
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

                if (playAgainstUtilityHeuristic ||
                        playAgainstPieceCounterHeuristic) {
                    playMove(miniMaxAlphaBetaAI.selectMove(model));
                    model.highlightTiles(miniMaxAlphaBetaAI.getPreviouslyVisitedNodes());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            if (!queue.isEmpty()) {
                model.move(queue.remove());
                model.updateBoard();
            }
        }
    }
}
