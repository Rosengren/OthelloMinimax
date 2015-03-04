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
    private static final int ROW = 0;
    private static final int COLUMN = 1;

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
        opponent = (computer == Field.BLACK) ? Field.WHITE : Field.BLACK;

        bestOption = miniMax(MAX_DEPTH, computer);

        game.move(bestOption[BEST_ROW], bestOption[BEST_COL]);
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
        int[] bestTile = {-1,-1};

        if (nextMoves.isEmpty() || depth == 0) {
            // Game over or depth reached, evaluate score
            bestScore = evaluate.evaluateBoard(game);
        } else {
            for (Position move : nextMoves) {
                // Try this move for the current "player"
                // push game to the stack and clone it
                games.push(game);
                game = (OthelloModel)game.clone();
                System.out.println("Clonning board #" + games.size());
                game.move(move);
//                game.move(move[ROW], move[COLUMN]);

                if (player == computer) {  // (computer) is maximizing player
                    currentScore = miniMax(depth - 1, this.opponent)[ROW];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestTile[ROW] = move.x; // [ROW];
                        bestTile[COLUMN] = move.y; //[COLUMN];

                    }
                } else {  // opponent is minimizing player
                    currentScore = miniMax(depth - 1, computer)[ROW];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestTile[ROW] = move.x; // move[ROW];
                        bestTile[COLUMN] = move.y; // [COLUMN];
                    }
                }

                // go to previous clone
                if (!games.isEmpty()) {
                    System.out.println("Popping clone #" + games.size());
                    game = games.pop();
                }
            }
        }

        return new int[] {bestScore, bestTile[ROW], bestTile[COLUMN]};
    }


}
