package AI;
import othello.Field;
import othello.OthelloModel;
import othello.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * MiniMaxAI: implementation for the random move generator
 * @author mitchcail / Kevin Rosengren
 *
 */
public class MiniMaxAI implements AI {

    private static final int MAX_DEPTH = 3;

    private static final int BEST_ROW = 1;
    private static final int BEST_COL = 2;

    private OthelloModel game;

    private Stack<OthelloModel> games;
    private HeuristicStrategy evaluate;
    private ArrayList<int[]> visitedNodes;

    private Field computer;
    private Field opponent;

    public MiniMaxAI() {
        evaluate = new HeuristicPieceCounter(); // default strategy
    }

    @Override
    public void setStrategy(HeuristicStrategy strategy) {
        evaluate = strategy;
    }

    @Override
    public Position selectMove(OthelloModel game) throws Exception {

        visitedNodes = new ArrayList<int[]>();

        games = new Stack<OthelloModel>();
        this.game = (OthelloModel)game.clone();

        int[] bestOption;

        computer = game.getCurrentPlayer();
        opponent = game.getOpponentPlayer();
        evaluate.setComputerAndOpponent(computer, opponent);

        bestOption = miniMax(MAX_DEPTH, computer);
        return new Position(bestOption[BEST_ROW], bestOption[BEST_COL]);
    }

    @Override
    public int getNumOfPositionsVisitedLastMove() {
        return visitedNodes.size();
    }

    @Override
    public int[][] getPreviouslyVisitedNodes() {
        return visitedNodes.toArray(new int[visitedNodes.size()][]);
    }


    /**
     * implementation of the recursive MiniMax Algorithm
     * @param depth: how deep the tree we are searching
     * @param player: the current players turn (computer or opponent)
     * @return best score along with the row and column the computer should play
     */
    private int[] miniMax(int depth, Field player) throws Exception {

        List<Position> nextMoves = game.getPossibleMoves(player);

        // computer is maximizing; while opponent is minimizing
        int bestScore = (player == computer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        Position bestPosition = new Position(-1, -1);

        if (!game.isRunning() || depth == 0 || nextMoves.isEmpty()) {
            // Game over or depth reached, evaluate score
            bestScore = evaluate.evaluateBoard(game.getBoard().getBoard());
        } else {

            for (Position position : nextMoves) {

                visitedNodes.add(new int[] {position.x, position.y});

                // Try this move for the current "player"
                // push game to the stack and clone it
                games.push(game);
                game = (OthelloModel)game.clone();
                game.move(position);

                if (player == computer) {  // computer is maximizing player
                    currentScore = miniMax(depth - 1, opponent)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestPosition = position;
                    }

                } else {  // opponent is minimizing player
                    currentScore = miniMax(depth - 1, computer)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestPosition = position;
                    }
                }

                // go to previous clone
                if (!games.isEmpty())
                    game = games.pop();
            }
        }

        return new int[] {bestScore, bestPosition.x, bestPosition.y};
    }


}
