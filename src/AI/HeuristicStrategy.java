package AI;

import othello.OthelloModel;

public interface HeuristicStrategy {

    public int evaluateBoard(OthelloModel game);
}
