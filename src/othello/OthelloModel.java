package othello;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class OthelloModel extends Observable implements Cloneable {

    public static final String ERR_PLAYER_MOVED
            = "Cannot add hole area. A player did move.";

    public static final String ERR_NO_ACTIVE_GAME
            = "No active game";

    public static final String ERR_OFF_BOARD_MOVE
            = "The move position has to be on the board";

    public static final String ERR_COLOR_IN_RECTANGLE
            = "You can't place the hole here. There are color pieces.";

    public static final String ERR_NO_VALID_RECTANGLE
            = "The specified rectangle isn't valid. Valid is something"
            + "like A1:B3 or A1:A1. The first position has to be on the"
            + "top left.";

    private Field currentPlayer = Field.BLACK;

    private boolean isRunning = true;

    private boolean submittedMove = false;

    public Board board;

    private final int[][] adjacentFields = {{-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

    public OthelloModel(int width, int height) {
        board = new Board(width, height);
        checkState();
    }

    public OthelloModel(int width, int height, String situation) {
        board = new Board(width, height, situation);
        checkState();
    }

    private void checkState() {
        if (!isMovePossible(Field.BLACK)) {
            if (!isMovePossible(Field.WHITE)) {
                this.isRunning = false;
            } else {
                this.currentPlayer = Field.WHITE;
            }
        }
    }

    private boolean isMovePossible(Field player) {
        return (getPossibleMoves(player).size() > 0);
    }

    public List<Position> getPossibleMoves(Field player) {
        if (!isRunning) {
            throw new IllegalStateException(ERR_NO_ACTIVE_GAME);
        }

        List<Position> possibleMoves = new ArrayList<Position>();

        Position pos;
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                pos = new Position(x, y);
                if (isMovePositionValid(pos)
                        && (getNrOfSwitches(player, pos) > 0)) {
                    possibleMoves.add(pos);
                }
            }
        }

        return possibleMoves;
    }

    private boolean hasPiece(Position pos) {
        return (board.isPositionOnBoard(pos) && board.get(pos) != null
                && board.get(pos) != Field.HOLE);
    }

    private boolean isMovePositionValid(Position pos) {
        boolean isMovePositionValid = false;

        if (!board.isPositionOnBoard(pos))
            return false;

        for (int[] field : adjacentFields) {
            Position tmp = new Position(pos.x + field[0],
                    pos.y + field[1]);
            if (hasPiece(tmp))
                isMovePositionValid = true;
        }

        if (board.get(pos.x, pos.y) != null) {
            isMovePositionValid = false;
        }

        return isMovePositionValid;
    }

    public void nextPlayer() {
        if (!isRunning)
            throw new IllegalStateException(ERR_NO_ACTIVE_GAME);

        currentPlayer = (currentPlayer == Field.BLACK) ? Field.WHITE : Field.BLACK;
    }

    public int move(int row, int col) {
        return move(new Position(row, col));
    }

    public int move(Position pos) {
        if (!isRunning)
            throw new IllegalStateException(ERR_NO_ACTIVE_GAME);

        int returnCode = -1;
        int switches;

        if (!board.isPositionOnBoard(pos))
            throw new IllegalArgumentException(ERR_OFF_BOARD_MOVE);

        if (isMovePositionValid(pos)
                && (getNrOfSwitches(currentPlayer, pos) > 0)) {
            board.set(pos, currentPlayer);

            for (int[] direction : adjacentFields) {
                switches = getNrOfIncludedPieces(currentPlayer, pos,
                        direction[0], direction[1]);

                if (switches > 0)
                    switchPieces(currentPlayer, pos, direction[0], direction[1]);
            }

            nextPlayer();

            if (!isMovePossible(getCurrentPlayer())) {
                Field nextPlayer = getWaitingPlayer();
                if (isMovePossible(nextPlayer)) {
                    nextPlayer();
                    returnCode = 1;
                } else {
                    setFinished();
                    returnCode = 2;
                }
            } else {
                returnCode = 0;
            }

            submittedMove = true;
        }

        updateBoard();
        return returnCode;
    }

    public Field getCurrentPlayer() {
        return currentPlayer;
    }

    public Field getOpponentPlayer() {
        return (currentPlayer == Field.WHITE) ? Field.BLACK : Field.WHITE;
    }

    private int getNrOfIncludedPieces(Field player, Position pos, int xDir, int yDir) {
        int switches = 0;
        int opponentCount = 0;
        Field opponent = (player == Field.WHITE) ? Field.BLACK : Field.WHITE;

        for (int tmp = 1;
             (pos.x + tmp * xDir >= 0)
             && (pos.x + tmp * xDir < board.width)
             && (pos.y + tmp * yDir >= 0)
             && (pos.y + tmp * yDir < board.height);
             tmp++) {

            Field piece = board.get(pos.x + tmp * xDir, pos.y + tmp * yDir);

            if (piece == player) {
                switches += opponentCount;
                break;
            } else if (piece == Field.HOLE) {
                return 0;
            } else if (piece == opponent) {
                opponentCount++;
            } else if (piece == null) {
                return 0;
            }
        }

        return switches;
    }

    private void switchPieces(Field player, Position pos, int xDir, int yDir) {
        if (!isRunning)
            throw new IllegalStateException(ERR_NO_ACTIVE_GAME);

        Field opponent = (player == Field.WHITE) ? Field.BLACK : Field.WHITE;

        for (int tmp = 1;; tmp++) {
            if (board.get(pos.x + tmp * xDir, pos.y + tmp * yDir) == player)
                break;
            else if (board.get(pos.x + tmp * xDir, pos.y + tmp * yDir) == opponent)
                board.set(pos.x + tmp * xDir, pos.y + tmp * yDir, player);
        }
    }

    private int getNrOfSwitches(Field player, Position pos) {
        int switches = 0;

        for (int[] direction : adjacentFields) {
            switches += getNrOfIncludedPieces(player, pos, direction[0], direction[1]);
        }

        return switches;
    }

    public int[] getResult() {
        int[] result = new int[2];
        result[0] = countPieces(Field.WHITE);
        result[1] = countPieces(Field.BLACK);
        return result;
    }

    private int countPieces(Field player) {
        int counter = 0;
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                if (board.get(x, y) == player) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public void setFinished() {
        if (!isRunning)
            throw new IllegalStateException(ERR_NO_ACTIVE_GAME);
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isValidRectangle(Position[] rectangle) {
        return board.isPositionOnBoard(rectangle[0])
                && board.isPositionOnBoard(rectangle[1])
                && rectangle[0].x <= rectangle[1].x
                && rectangle[0].y <= rectangle[1].y;
    }

    public boolean isColorInRectangle(Position[] rectangle) {
        if (!isValidRectangle(rectangle))
            throw new IllegalArgumentException(ERR_NO_VALID_RECTANGLE);

        for (int x = rectangle[0].x; x <= rectangle[1].x; x++) {
            for (int y = rectangle[0].y; y <= rectangle[1].y; y++) {
                if (board.get(x, y) == Field.BLACK || board.get(x, y) == Field.WHITE)
                    return true;
            }
        }
        return false;
    }

    public boolean makeHole(Position[] rectangle) {
        if (submittedMove) {
            throw new IllegalStateException(ERR_PLAYER_MOVED);
        } else if (!isValidRectangle(rectangle)) {
            throw new IllegalStateException(ERR_NO_VALID_RECTANGLE);
        } else if (isColorInRectangle(rectangle)) {
            throw new IllegalArgumentException(ERR_COLOR_IN_RECTANGLE);
        }

        for (int x = rectangle[0].x; x <= rectangle[1].x; x++) {
            for (int y = rectangle[0].y; y <= rectangle[1].y; y++) {
                board.set(x, y, Field.HOLE);
            }
        }

        if (getPossibleMoves(currentPlayer).size() == 0)
            nextPlayer();

        return true;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean wasMoveSubmitted() {
        return submittedMove;
    }

    public int[] getScore() {

        int[] score = new int[2];

        for (Field[] row : board.getBoard()) {
            for (Field tile : row) {
                if (tile == Field.BLACK)
                    score[0]++;
                else if (tile == Field.WHITE)
                    score[1]++;
            }
        }

        return score;
    }

    public int[] abortGame() {
        int[] result = getResult();
        setFinished();
        return result;
    }

    public Field getWaitingPlayer() {
        return (getCurrentPlayer() == Field.BLACK) ? Field.WHITE : Field.BLACK;
    }

    public void updateBoard() {
        setChanged();
        notifyObservers(board);
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        Board clonedBoard = new Board(board.width, board.height);
        for (int x = 0; x < board.width; x++)
            for (int y = 0; y < board.height; y++)
                clonedBoard.set(x, y, board.get(x, y)); // Deep Clone

        OthelloModel cloned = (OthelloModel)super.clone();
        cloned.setBoard(clonedBoard);

        return cloned;
    }
}
