package AI;
import othello.Field;
import othello.OthelloModel;
import othello.Position;

import java.util.List;
import java.util.Stack;


/**
 * MiniMaxAI: implementation for the random move generator
 * @author mitchcail / Kevin Rosengren
 *
 */
public class MiniMaxAI {

    private static final int MAX_DEPTH = 3;

    private static final int BEST_ROW = 1;
    private static final int BEST_COL = 2;

    private OthelloModel game;

    protected Stack<OthelloModel> games;
    protected HeuristicStrategy evaluate;

    protected Field computer;
    protected Field opponent;

    public void setStrategy() {
        evaluate = new HeuristicPieceCounter();
    }

    public void playTurn(OthelloModel game) throws CloneNotSupportedException {

        games = new Stack<OthelloModel>();
        this.game = (OthelloModel)game.clone();
        setStrategy();

        int[] bestOption;

        computer = game.getCurrentPlayer();
        opponent = game.getOpponentPlayer();

        bestOption = miniMax(MAX_DEPTH, computer);

        Position pos = new Position(bestOption[BEST_ROW], bestOption[BEST_COL]);
        game.move(pos);
    }


    /**
     * implementation of the recursive MiniMax Algorithm
     * @param depth: how deep the tree we are searching
     * @param player: the current players turn (computer or opponent)
     * @return best score along with the row and column the computer should play
     */
    private int[] miniMax(int depth, Field player) throws CloneNotSupportedException {

        List<Position> nextMoves = game.getPossibleMoves(player);

        // computer is maximizing; while opponent is minimizing
        int bestScore = (player == computer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        Position bestPosition = new Position(-1, -1);

        if (nextMoves.isEmpty() || depth == 0) {
            // Game over or depth reached, evaluate score
            bestScore = evaluate.evaluateBoard(game);
        } else {
            for (Position move : nextMoves) {
                // Try this move for the current "player"
                // push game to the stack and clone it
                games.push(game);
                game = (OthelloModel)game.clone();
                game.move(move);

                if (player == computer) {  // (computer) is maximizing player
                    currentScore = miniMax(depth - 1, this.opponent)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestPosition = move;
                    }

                } else {  // opponent is minimizing player
                    currentScore = miniMax(depth - 1, computer)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestPosition = move;
                    }
                }

                // go to previous clone
                if (!games.isEmpty()) {
                    game = games.pop();
                }
            }
        }

        return new int[] {bestScore, bestPosition.x, bestPosition.y};
    }


}
