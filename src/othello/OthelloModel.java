package othello;

import commands.Command;
import commands.CommandManager;

import java.util.ArrayList;
import java.util.Observable;

public class OthelloModel extends Observable {

    private static final int BOARD_HEIGHT = 8;
    private static final int BOARD_WIDTH = 8;
    private static final int ROW = 0;
    private static final int COL = 1;

    private static final int[][] ALL_DIRECTIONS =
            new int[][] { {0,1}, {0,-1}, {1,-1}, {1,0},
                          {1,1}, {-1,0}, {-1,1}, {-1,-1} };

    private static final char WHITE = 'W';
    private static final char BLACK = 'B';

    private int whiteCount, blackCount;

    private char player;
    protected String winner;

    private CommandManager commandManager;
    private Board board;

    public boolean blackPlayerTurn;

    public OthelloModel() {
        blackPlayerTurn = false;
        winner = "";
        player = BLACK;
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
//        countBoardPieces();
        commandManager = new CommandManager();
        initializeBoard();
    }

    public void playMove(int row, int col) {
        if (isLegalMove(row, col)) {
            commandManager.executeCommand(new PlacePieceCommand(this, row, col, BOARD_WIDTH, BOARD_HEIGHT));
        }

        updateBoard();
    }

    private void initializeBoard() {
        board.reset();
        board.setTile(3, 3, 'W');
        board.setTile(4, 4, 'W');
        board.setTile(3, 4, 'B');
        board.setTile(4, 3, 'B');
    }

    public char[][] getBoard() {
        return board.getBoard();
    }

    public void setBoard(char[][] newBoard) {
        board.setBoard(newBoard);
    }


    public void updateBoard() {
        setChanged();
        notifyObservers(board);
    }

    private class PlacePieceCommand implements Command {

        private OthelloModel model;

        private char[][] previousBoardState;
        private char previousPlayer;
        private char[][] nextBoardState;
        private char nextPlayer;

        private PlacePieceCommand(OthelloModel model, int row, int col, int width, int height) {
            this.model = model;

            previousPlayer = getCurrentPlayer();
            previousBoardState = new char[width][height];
            nextBoardState = new char[width][height];

            char[][] modelBoard = model.getBoard();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    previousBoardState[i][j] = modelBoard[i][j];
                    nextBoardState[i][j] = modelBoard[i][j];
                }
            }

            nextBoardState = makeMove(row, col);
            nextPlayer = getCurrentPlayer();
        }

        public char[][] makeMove(int row, int col) {

            if (model.isLegalMove(row, col)) {
                model.flipAllPieces(row, col);

                model.switchPlayer();
                if (model.getPossibleMoves().size() == 0) { // empty
                    if (model.player == WHITE) {
                        System.out.println("BLACK PASS TURN");
//                    othelloState.aiPassTurn();
                    } else {
                        System.out.println("WHITE PASS TURN");
//                    othelloState.playerPassTurn();
                    }

                    model.switchPlayer(); // pass turn
                } else {
                    System.out.println("NOBODY PASS TURN");
//                othelloState.nobodyPassTurn();
                }

                if (!model.haveWinner()) {
                    if (model.player == WHITE) {
                        System.out.println("Other Player's Turn");
//                    playAITurn();
                    }

                    if (model.haveWinner())
                        model.endGame();

                } else {
                    model.endGame();
                }

                model.updateBoard();
                model.updatePlayer();

            } else {
                model.setChanged();
                model.notifyObservers("Invalid Move");
            }

            return board.getBoard();

        }

        @Override
        public void execute() {
            model.setBoard(nextBoardState);
            model.player = nextPlayer;
        }

        @Override
        public void undo() {
            model.setBoard(previousBoardState);
            model.player = previousPlayer;
        }

    }


    public boolean haveWinner() {
        return (!winner.equals(""));
    }

    public void endGame() {
        updateBoard();
        updatePlayer();
        setChanged();
        notifyObservers(new String[] {"game over", getWinner()});
    }

    public String getWinner() {
        return winner;
    }

    public void checkForWinner() {

        countBoardPieces();

        if (getPossibleMoves().size() == 0) { // empty
            switchPlayer();
            if (getPossibleMoves().size() == 0) { // empty

                if (whiteCount > blackCount)
                    winner = Character.toString(WHITE);
                else if (blackCount > whiteCount)
                    winner = Character.toString(BLACK);
                else
                    winner = "TIE";

            } else {
                switchPlayer();
            }
        }

    }

    private void switchPlayer() {
        player = (player == BLACK) ? WHITE : BLACK;
    }


    private ArrayList<int[]> getListOfPiecesToFlip(int row, int col, int deltaRow, int deltaCol) {

        ArrayList<int[]> piecesThatCanBeFlipped = new ArrayList<int[]>();

        for (col += deltaCol, row += deltaRow ; board.isValidTile(row, col) ; col += deltaCol, row+= deltaRow) {

            if (board.isEmptyTile(row, col)) {
                piecesThatCanBeFlipped = new ArrayList<int[]>();
                break;
            } else if (occupiedByPlayer(row, col)) {
                break;
            }

            piecesThatCanBeFlipped.add(new int[] {row, col});
        }

        if (!board.isValidTile(row, col))
            piecesThatCanBeFlipped = new ArrayList<int[]>();

        return piecesThatCanBeFlipped;

    }

    private boolean occupiedByPlayer(int row, int col) {
        return (board.isValidTile(row, col) && (board.getBoard()[row][col] == player));
    }


    public ArrayList<int[]> getPossibleMoves() {
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        for (int row = 0; row< board.getWidth(); row++)
            for (int col = 0; col< board.getHeight(); col++)
                if (isLegalMove(row,col))
                    legalMoves.add(new int[] {row,col});


        return legalMoves;
    }

    public boolean isLegalMove(int row, int col) {
        if (!board.isEmptyTile(row, col))
            return false;

        for (int[] direction : ALL_DIRECTIONS)
            if (!getListOfPiecesToFlip(row, col, direction[ROW], direction[COL]).isEmpty())
                return true;

        return false; // no pieces can flip
    }


    private void flipAllPieces(int row, int col) {

        for (int[] direction : ALL_DIRECTIONS)
            flipPieces(getListOfPiecesToFlip(row, col, direction[ROW], direction[COL]));

        board.setTile(row, col, player);
    }

    private void flipPieces(ArrayList<int[]> piecesToFlip) {

        for (int[] tile: piecesToFlip)
            if (tile.length == 2)
                board.setTile(tile[ROW], tile[COL], player);
    }

    public void countBoardPieces() {

        whiteCount = blackCount = 0;

        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col< board.getHeight(); col++) {
                char piece = board.getTile(row, col);
                if ( piece == WHITE)
                    whiteCount++;

                else if (piece == BLACK)
                    blackCount++;
            }
        }
    }

    public char getCurrentPlayer() {
        return player;
    }



    public void updatePlayer() {
        setChanged();

        notifyObservers("Black Count: " + blackCount +
                "\nWhite Count: " + whiteCount +
                "\nPlayer " + player + "'s turn");
    }

    public void undo() {
        commandManager.undo();
        updateBoard();
        updatePlayer();
    }

}