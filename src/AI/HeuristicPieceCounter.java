package AI;

import othello.Field;

public class HeuristicPieceCounter implements HeuristicStrategy {

    private Field computer;
    private Field opponent;

    /**
     * constructor
     *
     * initialize computer and opponent pieces
     */
    public HeuristicPieceCounter() {

        // default
        computer = Field.WHITE;
        opponent = Field.BLACK;
    }


    @Override
    public void setComputerAndOpponent(Field computer, Field opponent) {
        this.computer = computer;
        this.opponent = opponent;
    }

    /**
     * evaluate the board by determining the ratio
     * of computer and opponent pieces on the board
     */
    @Override
    public int evaluateBoard(Field[][] board) {

        int computerCount = 0;
        int opponentCount = 0;


        for (Field[] row : board) {
            for (Field tile : row) {
                if (tile == computer) {
                    computerCount++;
                } else if (tile == opponent) {
                    opponentCount++;
                }
            }
        }

        return computerCount - opponentCount;
    }

}
