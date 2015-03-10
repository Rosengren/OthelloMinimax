# Othello
### MiniMax and Alpha-Beta Pruning

### State Space

The State space of the Othello is 10<sup>28</sup> for an 8 by 8 board with a total of 64 tiles.

### Heuristics

Two Heuristics were used for the MiniMax implementation.

#### Piece Counter

The first heuristic subtracts the number of pieces the opponent has from the number of pieces the computer has. This heuristic was chosen because it is both easy to understand and implement. Since the objective of the game is to finish with the most pieces of a given colour, it makes sense to use this as a metric for evaluating the board.

#### Utility Heuristic

This heuristic is the sum of several different board evaluations. The first measurement is the number of pieces each player has. This is the same as the piece counter heuristic with the exception that the comparison uses the following equation:

    100 * (max - min) / (max + min)

The second measurement is the number of potential moves each player can make. The more moves that the computer can make, the higher the score. The score is lowered based on the number of moves the opponent can make. The third metric is the number of corners that are captured by each player since a corner piece cannot be captured and can help greatly in flipping opponent pieces. The more captured corners the computer has, and the fewer the opponent has, the higher the score is for this measurement. The final measurement sets a number for each tile on the board based on its value. That is, different tiles on the board, such as side and corner tiles, are better than middle tiles because they are harder to capture. Thus, the score is the sum of the computers captured tiles minus the sum of the tiles captured by the opponent.

#### Node Count

The node count is reduced dramatically when switching from MiniMax to MiniMax with alpha-beta pruning. This is because alpha-beta does not visit every node down to a specific depth. Instead, it compares the values at each level of the tree and skips (or prunes) any branch that has a lower score than another. The lower scored branch will not lead to a better move decision which is why it can be skipped without influencing the outcome of the game. With a branching factor b and a depth d, the node count is b<sup>d</sup>  while the node count for alpha-beta is b<sup>d/2</sup>.
