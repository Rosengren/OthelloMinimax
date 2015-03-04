package othello;

public class Board {

    private static final int FIRST_ROW = 0;

    private char[][] board;

    public Board(int rows, int cols) {
        board = new char[rows][cols];
    }

    public void reset() {
        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[row].length; col++)
                board[row][col] = ' ';
    }

    public void setTile(int row, int col, char item) {
        if (isValidTile(row, col)) {
            board[row][col] = item;
        }
    }

    public char getTile(int row, int col) {
        return (isValidTile(row, col)) ? board[row][col] : ' ';
    }

    public boolean isEmptyTile(int row, int col) {
        return board[row][col] == ' ';
    }

    public boolean isValidTile(int row, int col) {
        return row >= 0 && row < board.length
                && col >= 0 && col < board[FIRST_ROW].length;
    }

    public char[][] getBoard() {
        return board;
    }

    public void printBoard() {
        String result = "";

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                result += board[row][col] + " ";
            }
            result += "\n";
        }

        System.out.println(result);
    }

    public void setBoard(char[][] newBoard) {
        board = newBoard;
    }

    public int getHeight() {
        return (board == null) ? -1 : board[FIRST_ROW].length;
    }

    public int getWidth() {
        return (board == null) ? -1 : board.length;
    }
}
