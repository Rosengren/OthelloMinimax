package othello;

public class Board {

    public int width;
    public int height;
    public String situation;
    private Field[][] board;

    public Board(int width, int height) {
        board = new Field[width][height];
        this.width = width;
        this.height = height;
        hardCodedInitialBoard();
    }

    public Board(int width, int height, String situation) {
        this(width, height);
        this.situation = situation;
    }

    public void hardCodedInitialBoard() { // TODO: replace this
        board[3][3] = Field.WHITE;
        board[4][4] = Field.WHITE;
        board[3][4] = Field.BLACK;
        board[4][3] = Field.BLACK;
    }

    public void set(int x, int y, Field f) {
        board[x][y] = f;
    }

    public Field get(int x, int y) {
        return board[x][y];
    }

    public void set(Position pos, Field f) {
        board[pos.x][pos.y] = f;
    }

    public Field get(Position pos) {
        return board[pos.x][pos.y];
    }

    public boolean isPositionOnBoard(Position pos) {
        return pos.x < width && pos.y < height && pos.x >= 0 && pos.y >= 0;
    }

    public Field[][] getBoard() {
        Field[][] clonedBoard = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (board[x][y] == null) {
                    clonedBoard[x][y] = Field.HOLE;
                } else if (board[x][y] == Field.BLACK) {
                    clonedBoard[x][y] = Field.BLACK;
                } else if (board[x][y] == Field.WHITE) {
                    clonedBoard[x][y] = Field.WHITE;
                } else if (board[x][y] == Field.HOLE) {
                    clonedBoard[x][y] = Field.HOLE;
                }
            }
        }

        return clonedBoard;
    }
}