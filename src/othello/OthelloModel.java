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
        countBoardPieces();
        commandManager = new CommandManager();
        initializeBoard();
    }

    public void playMove(int row, int col) {
        System.out.println("isBlack: " + blackPlayerTurn);
        if (blackPlayerTurn)
            placeBlack(row, col);
        else
            placeWhite(row, col);

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

    public void placeWhite(int row, int col) {
        if (!blackPlayerTurn && board.isEmptyTile(row, col))
            commandManager.executeCommand(
                    new PlaceWhitePieceCommand(this, row, col, BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void placeBlack(int row, int col) {
        if(blackPlayerTurn && board.isEmptyTile(row, col))
            commandManager.executeCommand(
                    new PlaceBlackPieceCommand(this, row, col, BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void updateBoard() {
        setChanged();
        notifyObservers(board);
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

            nextBoardState = model.makeMove(row, col);
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

            nextBoardState = model.makeMove(row,col);
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

//    TO SIFT THROUGH
    /**
     * This method finds all of the pieces that can be flipped
     * @param row: row that piece will be added to
     * @param col: column that piece will be added to
     * @param deltaRow: vertical direction
     * @param deltaCol: horizontal direction
     * @return ArrayList of all the pieces that can be flipped
     */
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

    /**
     * determine whether the specified tile is
     * occupied by the current player
     * @param row of tile
     * @param col of tile
     * @return true if occupied, false otherwise
     */
    private boolean occupiedByPlayer(int row, int col) {
        return (board.isValidTile(row, col) && (board.getBoard()[row][col] == player));
    }


    /**
     * This method adds the possible moves to a list
     * @return list of the possible moves
     */
    public ArrayList<int[]> getPossibleMoves() {
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        for (int row = 0; row< board.getWidth(); row++)
            for (int col = 0; col< board.getHeight(); col++)
                if (isLegalMove(row,col))
                    legalMoves.add(new int[] {row,col});


        return legalMoves;
    }

    /**
     * determine whether or not a move is legal
     * @param row of tile
     * @param col of tile
     * @return true if the move is legal, false otherwise
     */
    public boolean isLegalMove(int row, int col) {
        if (!board.isEmptyTile(row, col))
            return false;

        for (int[] direction : ALL_DIRECTIONS)
            if (!getListOfPiecesToFlip(row, col, direction[ROW], direction[COL]).isEmpty())
                return true;

        return false; // no pieces can flip
    }


    /**
     * Flips all of the pieces on the board as well
     * as sets the given tile as the current player
     * @param row of tile
     * @param col of tile
     */
    private void flipAllPieces(int row, int col) {

        for (int[] direction : ALL_DIRECTIONS)
            flipPieces(getListOfPiecesToFlip(row, col, direction[ROW], direction[COL]));

        board.setTile(row, col, player);
    }

    /**
     * This method flips the opponent pieces
     * @param piecesToFlip: list of the pieces to flip
     */
    private void flipPieces(ArrayList<int[]> piecesToFlip) {

        for (int[] tile: piecesToFlip)
            if (tile.length == 2)
                board.setTile(tile[ROW], tile[COL], player);
    }



    /**
     * count up all of the black and white
     * pieces on the board
     */
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


    /**
     * determine if the game has a winner or
     * ends in a tie. Also checks if neither
     * player can go. That is, if there are
     * still available tiles on the board
     * but neither player can legally play
     */
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

    public char[][] makeMove(int row, int col) {

        if (isLegalMove(row, col)) {
            flipAllPieces(row, col);

            switchPlayer();
            if (getPossibleMoves().size() == 0) { // empty
                if (player == WHITE) {
                    System.out.println("BLACK PASS TURN");
//                    othelloState.aiPassTurn();
                } else {
                    System.out.println("WHITE PASS TURN");
//                    othelloState.playerPassTurn();
                }

                switchPlayer(); // pass turn
            } else {
                System.out.println("NOBODY PASS TURN");
//                othelloState.nobodyPassTurn();
            }

            if (!haveWinner()) {
                if (player == WHITE) {
                    System.out.println("Other Player's Turn");
//                    playAITurn();
                }

                if (haveWinner())
                    endGame();

            } else {
                endGame();
            }

            updateBoard();
            updatePlayer();

        } else {
            setChanged();
            notifyObservers("Invalid Move");
        }

        return board.getBoard();

    }


    /**
     * determines whether the game
     * has a winner
     * @return true if there's a winner,
     * 		   false otherwise
     */
    public boolean haveWinner() {
        return (!winner.equals(""));
    }


    /**
     * performs the final operations
     * for the game before being
     * terminated
     */
    public void endGame() {
        updateBoard();
        updatePlayer();
        setChanged();
        notifyObservers(new String[] {"game over", getWinner()});
    }

    /**
     * gets the winner of the game
     * @return char winner or empty
     * 		   string if no winner
     */
    public String getWinner() {
        return winner;
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