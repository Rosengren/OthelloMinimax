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

    private static final int DELAY = 50;

    private Queue<Position> queue;

    private static final int ROW = 0;
    private static final int COL = 1;

    private AI miniMaxAI;
    private AI miniMaxAlphaBetaAI;

    private boolean playAgainstUtilityHeuristic;
    private boolean playAgainstPieceCounterHeuristic;
    private boolean highlightTiles;

    private OthelloModel model;
    private TAdapter keyPress;

    public Controller(OthelloModel model) {
        playAgainstUtilityHeuristic = false;
        playAgainstPieceCounterHeuristic = false;
        highlightTiles = false;
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

    public void playMove(Position pos, String player) {

        try {
            if (model.isRunning())
                model.move(pos);

//            printMsg("Player " + player + " played: (" + pos.x + "," + pos.y + ")");
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
                    case KeyEvent.VK_1: // MiniMax Makes 1 Move
                        playMove(miniMaxAI.selectMove(model), "MiniMax");
                        if (highlightTiles)
                            model.highlightTiles(miniMaxAI.getPreviouslyVisitedNodes());
                        printMsg("Nodes Visited (MiniMax): " + miniMaxAI.getNumOfPositionsVisitedLastMove());
                        return;
                    case KeyEvent.VK_2: // Alpha-Beta Makes 1 Move
                        playMove(miniMaxAlphaBetaAI.selectMove(model), "AlphaBeta");
                        if (highlightTiles)
                            model.highlightTiles(miniMaxAlphaBetaAI.getPreviouslyVisitedNodes());
                        printMsg("Nodes Visited (Alpha-Beta): " + miniMaxAlphaBetaAI.getNumOfPositionsVisitedLastMove());
                        return;
                    case KeyEvent.VK_3: // Play complete game with miniMax AI
                        while (model.isRunning()) {
                            playMove(miniMaxAI.selectMove(model), "MiniMax");
                            printMsg("Nodes visited: " + miniMaxAI.getNumOfPositionsVisitedLastMove());
                        }
                        return;
                    case KeyEvent.VK_4: // Play complete game with Alpha-Beta AI
                        while (model.isRunning()) {
                            playMove(miniMaxAlphaBetaAI.selectMove(model), "AlphaBeta");
                            printMsg("Nodes visited: " + miniMaxAlphaBetaAI.getNumOfPositionsVisitedLastMove());
                        }
                        return;
                    case KeyEvent.VK_5: // Play complete game with MiniMax vs Alpha-Beta
                        printMsg("MiniMaxAI = BLACK, MiniMaxAlphaBetaAI = WHITE");
                        while (model.isRunning()) {
                            playMove(miniMaxAI.selectMove(model), "MiniMax");
                            printMsg("Nodes visited (MiniMax): " + miniMaxAI.getNumOfPositionsVisitedLastMove());
                            playMove(miniMaxAlphaBetaAI.selectMove(model), "AlphaBeta");
                            printMsg("Nodes visited (Alpha-beta): " + miniMaxAlphaBetaAI.getNumOfPositionsVisitedLastMove());
                        }
                        return;
                    case KeyEvent.VK_6: // Set the Alpha-Beta Heuristic to Piece Counter
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicPieceCounter());
                        playAgainstPieceCounterHeuristic = !playAgainstPieceCounterHeuristic;
                        playAgainstUtilityHeuristic = false;
                        printMsg("Playing against Alpha-Beta Piece Counter Heuristic");
                        return;
                    case KeyEvent.VK_7: // Set Alpha-Beta Heuristic to Utility
                        miniMaxAlphaBetaAI.setStrategy(new HeuristicUtility());
                        playAgainstUtilityHeuristic = !playAgainstUtilityHeuristic;
                        playAgainstPieceCounterHeuristic = false;
                        printMsg("Playing against Utility Heuristic");
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
                    case KeyEvent.VK_H:
                        highlightTiles = !highlightTiles;
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
                playMove(playerMove, "Human");

                if (playAgainstUtilityHeuristic ||
                        playAgainstPieceCounterHeuristic) {
                    playMove(miniMaxAlphaBetaAI.selectMove(model), "AlphaBeta");
                    if (highlightTiles)
                        model.highlightTiles(miniMaxAlphaBetaAI.getPreviouslyVisitedNodes());
                    printMsg("Nodes visited (Alpha-beta): " + miniMaxAlphaBetaAI.getNumOfPositionsVisitedLastMove());
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
