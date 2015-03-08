package AI;

import othello.Field;
import othello.OthelloModel;
import othello.Position;

import java.util.List;
import java.util.Stack;

public class MiniMaxAlphaBetaAI implements AI {

    private static final int MAX_DEPTH = 3;

    private static final int BEST_ROW = 1;
    private static final int BEST_COL = 2;

    private OthelloModel game;

    private HeuristicStrategy evaluate;
    private Stack<OthelloModel> games;

    private Field computer;
    private Field opponent;

    private int totalNodesVisited;

    public MiniMaxAlphaBetaAI() {
        evaluate = new HeuristicPieceCounter(); // default strategy
    }

    @Override
    public void setStrategy(HeuristicStrategy strategy) {
        evaluate = strategy;
    }

    @Override
    public void playTurn(OthelloModel game) throws CloneNotSupportedException {
        totalNodesVisited = 0;
        games = new Stack<OthelloModel>();
        this.game = (OthelloModel)game.clone();

        int[] bestOption;

        computer = game.getCurrentPlayer();
        opponent = game.getOpponentPlayer();

        bestOption = miniMax(MAX_DEPTH, computer, Integer.MIN_VALUE, Integer.MAX_VALUE);

        Position pos = new Position(bestOption[BEST_ROW], bestOption[BEST_COL]);
        game.move(pos);

        System.out.println("Nodes Visited: " + totalNodesVisited);
    }

    /** Minimax (recursive) at level of depth for maximizing or minimizing player
     with alpha-beta cut-off. Return int[3] of {score, row, col}  */
    private int[] miniMax(int depth, Field player, int alpha, int beta) throws CloneNotSupportedException {

        Position bestPosition = new Position(-1, -1);
        int score;

        if (!game.isRunning() || depth == 0) {
            // Game over or depth reached, evaluate score
            score = evaluate.evaluateBoard(game.getBoard().getBoard());
            return new int[] {score, bestPosition.x, bestPosition.y};
        } else {

            List<Position> nextMoves = game.getPossibleMoves(player);

            for (Position position : nextMoves) {
                totalNodesVisited++;
                // Try this move for the current "player"
                // push game to the stack and clone it
                games.push(game);
                game = (OthelloModel)game.clone();
                game.move(position);

                if (player == computer) {  // mySeed (computer) is maximizing player
                    score = miniMax(depth - 1, opponent, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestPosition = position;
                    }
                } else {  // oppSeed is minimizing player
                    score = miniMax(depth - 1, computer, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestPosition = position;
                    }
                }
                // undo move
                // go to previous clone
                if (!games.isEmpty())
                    game = games.pop();

                // cut-off
                if (alpha >= beta) break;
            }
            return new int[] {(player == computer) ? alpha : beta, bestPosition.x, bestPosition.y};
        }
    }
}
