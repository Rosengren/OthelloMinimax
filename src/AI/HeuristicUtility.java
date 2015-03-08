package AI;

import othello.Field;

public class HeuristicUtility implements HeuristicStrategy {

    private final int[][] adjacentFields = {{-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

    private Field computer;
    private Field opponent;

    /**
     * constructor
     *
     * initialize computer and opponent pieces
     */
    public HeuristicUtility() {

        // default
        this.computer = Field.WHITE;
        this.opponent = Field.BLACK;
    }

    @Override
    public void setComputerAndOpponent(Field computer, Field opponent) {
        this.computer = computer;
        this.opponent = opponent;
    }

    @Override
    public int evaluateBoard(Field[][] board) {
//        return parity(board);
        return mobility(board);
    }

    /**
     * Parity
     *
     * This component of the utility function captures the difference
     * in coins between the max player and the min player
     */
    private int parity(Field[][] board) {
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

        // 100 * (max - min) / (max + min)
        return 100 * (computerCount - opponentCount) / (computerCount + opponentCount);
    }


    /**
     * Mobility
     *
     * It attempts to capture the relative difference between the number of
     * possible moves for the max and the min players, with the intent of
     * restricting the opponent’s mobility and increasing one’s own mobility.
     *
     *  if ( Max Player Moves + Min Player Moves != 0)
     *      return 100 * (Max Player Moves - Min Player Moves) / (Max Player Moves + Min Player Moves)
     *  else
     *    return 0
     */
    private int mobility(Field[][] board) {
        int maxPlayerMoves = num_valid_moves(computer, opponent, board);
        int minPlayerMoves = num_valid_moves(opponent, computer, board);
        if (maxPlayerMoves + minPlayerMoves != 0) {
            return 100 * (maxPlayerMoves - minPlayerMoves) / (maxPlayerMoves + minPlayerMoves);
        }
        return 0;
    }

    /** HELPER METHODS **/

    private int num_valid_moves(Field self, Field opp, Field[][] board)   {
        int count = 0, i, j;
        for(i = 0; i < board.length; i++)
            for(j = 0; j < board[i].length; j++)
                if(isLegalMove(self, opp, board, i, j))
                    count++;

        return count;
    }


    private boolean isLegalMove(Field self, Field opp, Field[][] board, int startX, int startY)   {
        if (board[startX][startY] != null && board[startX][startY] != Field.HOLE)
            return false;

        Field[] str = new Field[10];
        int x, y, dx, dy, ctr;
        for (dy = -1; dy <= 1; dy++)
            for (dx = -1; dx <= 1; dx++)    {
                // keep going if both velocities are zero
                if (dy == 0 && dx == 0)
                    continue;
                for (ctr = 1; ctr < 8; ctr++)   {
                    x = startX + ctr*dx;
                    y = startY + ctr*dy;
                    if (x >= 0 && y >= 0 && x<8 && y<8)
                        str[ctr-1] = board[x][y];
                    else str[ctr-1] = Field.HOLE;
                }

                if (canMove(self, opp, str)) return true;
            }

        return false;
    }

    private boolean canMove(Field self, Field opp, Field[] str)  {
        if (str[0] != opp) return false;
        for (int ctr = 1; ctr < 8; ctr++) {
            if (str[ctr] == null)
                return false;
            if (str[ctr] == self)
                return true;
        }
        return false;
    }

}
