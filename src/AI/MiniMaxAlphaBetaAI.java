package AI;

import othello.OthelloModel;

public class MiniMaxAlphaBetaAI implements AI {

    protected HeuristicStrategy evaluate;

    @Override
    public void setStrategy(HeuristicStrategy strategy) {
        evaluate = strategy;
    }

    @Override
    public void playTurn(OthelloModel game) throws CloneNotSupportedException {

    }
}
