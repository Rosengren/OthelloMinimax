package AI;
import othello.*;

public interface AI {

    public void setStrategy(HeuristicStrategy strategy);

    public Position selectMove(OthelloModel game) throws Exception;

    public int getNumOfPositionsVisitedLastMove();

    public int[][] getPreviouslyVisitedNodes();
}
