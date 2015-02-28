package othello;

import commands.Command;
import commands.CommandManager;

import java.util.Observable;

public class OthelloModel extends Observable {

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    private CommandManager commandManager;
    private Board board;

    public boolean blackPlayerTurn;

    public OthelloModel() {
        blackPlayerTurn = false;
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        commandManager = new CommandManager();
    }

    public char[][] getBoard() {
        return board.getBoard();
    }

    public void setBoard(char[][] newBoard) {
        board.setBoard(newBoard);
    }

    public void placeWhite(int row, int col) {
        assert(!blackPlayerTurn);
        assert(board.isEmptyTile(row, col));
        commandManager.executeCommand(new PlaceWhitePieceCommand(this, row, col, BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void placeBlack(int row, int col) {
        assert(blackPlayerTurn);
        assert(board.isEmptyTile(row, col));
        commandManager.executeCommand(new PlaceBlackPieceCommand(this, row, col, BOARD_WIDTH, BOARD_HEIGHT));
    }

    private class PlaceWhitePieceCommand implements Command {

        private OthelloModel model;

        private char[][] previousBoardState;
        private boolean previousTurnState;
        private char[][] nextBoardState;
        private boolean nextTurnState;

        private PlaceWhitePieceCommand(OthelloModel model, int row, int col, int width, int height) {
            this.model = model;

            previousTurnState = model.blackPlayerTurn;
            previousBoardState = new char[width][height];
            nextBoardState = new char[width][height];

            char[][] modelBoard = model.getBoard();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    previousBoardState[i][j] = modelBoard[i][j];
                    nextBoardState[i][j] = modelBoard[i][j];
                }
            }

            // TODO: Change this next line for dealing with flipping pieces
            nextBoardState[row][col] = 'W';
            nextTurnState = false;
        }


        @Override
        public void execute() {
            model.setBoard(nextBoardState);
            model.blackPlayerTurn = nextTurnState;
        }

        @Override
        public void undo() {
            model.setBoard(previousBoardState);
            model.blackPlayerTurn = previousTurnState;
        }
    }

    private class PlaceBlackPieceCommand implements Command {

        private OthelloModel model;
        private boolean previousTurnState;
        private char[][] previousBoardState;
        private boolean nextTurnState;
        private char[][] nextBoardState;

        private PlaceBlackPieceCommand(OthelloModel model, int row, int col, int width, int height) {
            this.model = model;

            previousTurnState = model.blackPlayerTurn;
            previousBoardState = new char[width][height];
            nextBoardState = new char[width][height];
            char[][] modelBoard = model.getBoard();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    previousBoardState[i][j] = modelBoard[i][j];
                    nextBoardState[i][j] = modelBoard[i][j];
                }
            }

            // TODO: Change this for the Tile Flips
            nextBoardState[row][col] = 'B';
            nextTurnState = true;
        }

        @Override
        public void execute() {
            model.setBoard(nextBoardState);
            model.blackPlayerTurn = nextTurnState;
        }

        @Override
        public void undo() {
            model.setBoard(previousBoardState);
            model.blackPlayerTurn = previousTurnState;
        }
    }
}