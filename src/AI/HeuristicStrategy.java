package AI;

import othello.Field;

public interface HeuristicStrategy {

    public int evaluateBoard(Field[][] board);
}
