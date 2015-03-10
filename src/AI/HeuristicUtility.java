package AI;

import othello.Field;

public class HeuristicUtility implements HeuristicStrategy {

    private static final int[][] positionScore =
            new int[][] {{20, -3, 11,  8,  8, 11, -3, 20},
                         {-3, -7, -4,  1,  1, -4, -7, -3},
                         {11, -4,  2,  2,  2,  2, -4, 11},
                         {8,   1,  2, -3, -3,  2,  1,  8},
                         {8,   1,  2, -3, -3,  2,  1,  8},
                         {11, -4,  2,  2,  2,  2, -4, 11},
                         {-3, -7, -4,  1,  1, -4, -7, -3},
                         {20, -3, 11,  8,  8, 11, -3, 20}};


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
        return ( 80 * mobility(board)) + (10 * parity(board)) +
               (800 * capturedCorners(board)) + (10 * weightedPositions(board));
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
        int maxPlayerMoves = numberOfValidMoves(computer, opponent, board);
        int minPlayerMoves = numberOfValidMoves(opponent, computer, board);
        if(maxPlayerMoves > minPlayerMoves)
            return (100 * maxPlayerMoves)/(maxPlayerMoves + minPlayerMoves);
        else if(maxPlayerMoves < minPlayerMoves)
            return -(100 * minPlayerMoves)/(maxPlayerMoves + minPlayerMoves);
        else
            return 0;
    }

    /**
     * Captured Corners
     *
     * Corners hold special importance because once captured, they cannot be
     * flanked by the opponent. They also allow a player to build coins around
     * them and provide stability to the player’s coins.
     */
    private int capturedCorners(Field[][] board) {
        int maxPlayer = numberOfCapturedCorners(computer, board);
        int minPlayer = numberOfCapturedCorners(opponent, board);

        if (maxPlayer + minPlayer != 0) {
            return 25 * (maxPlayer - minPlayer);
        }

        return 0;
    }

    /**
     * Weighted Positions
     *
     * Each position on the board has a different value. Corners are most
     * valuable, followed by the sides. Center positions have the lowest
     * value.
     */
    private int weightedPositions(Field[][] board) {

        int maxPlayer = getPositionWeights(computer, board);
        int minPlayer = getPositionWeights(opponent, board);

        if (maxPlayer + minPlayer == 0) return 0;

        if (maxPlayer > minPlayer)
            return 100 * maxPlayer / (maxPlayer + minPlayer);
        else if (maxPlayer < minPlayer)
            return -(100 * maxPlayer / (maxPlayer + minPlayer));

        return 0;
    }

    /** HELPER METHODS **/

    private int getPositionWeights(Field player, Field[][] board) {

        int result = 0;

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == player)
                    result += positionScore[x][y];
            }
        }

        return  result;
    }


    private int numberOfCapturedCorners(Field player, Field[][] board) {
        int result = 0;
        if (board.length == 0) return result;
        if (board[0].length == 0) return result;
        if (board[0][0] == player) result++;
        if (board[0][board[0].length - 1] == player) result++;
        if (board[board.length - 1][0] == player) result++;
        if (board[board.length -1][board[0].length - 1] == player) result++;

        return result;
    }

    private int numberOfValidMoves(Field self, Field opp, Field[][] board)   {
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
