package AI;
import othello.*;

public interface AI {

    public void setStrategy(HeuristicStrategy strategy);

    public void playTurn(OthelloModel game) throws CloneNotSupportedException;
}
