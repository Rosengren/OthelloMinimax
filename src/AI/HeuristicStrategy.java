package AI;

import othello.Field;

public interface HeuristicStrategy {

    public void setComputerAndOpponent(Field computer, Field opponent);

    public int evaluateBoard(Field[][] board);
}
