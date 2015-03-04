package AI;

import othello.OthelloModel;

public class HeuristicPieceCounter implements HeuristicStrategy {

    private char computer;
    private char opponent;

    /**
     * constructor
     *
     * initialize computer and opponent pieces
     */
    public HeuristicPieceCounter() {

        computer = 'W';
        opponent = 'B';
    }


    /**
     * evaluate the board by determining the ratio
     * of computer and opponent pieces on the board
     */
    @Override
    public int evaluateBoard(OthelloModel game) {

        char[][] board = game.getBoard();
        int computerCount = 0;
        int opponentCount = 0;


        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                if (board[col][row] == computer) {
                    computerCount++;
                } else if (board[col][row] == opponent) {
                    opponentCount++;
                }
            }
        }

        return computerCount - opponentCount;
    }

}
