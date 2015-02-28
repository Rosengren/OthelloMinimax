package testSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import othello.Board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BoardTest {

    private static final int BOARD_HEIGHT = 8;
    private static final int BOARD_WIDTH = 8;

    private Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
    }

    @Test
    public void testBoardDimensions() {
        assertEquals("Board height should be: " + BOARD_HEIGHT, board.getHeight(), BOARD_HEIGHT);
        assertEquals("Board width should be: " + BOARD_WIDTH, board.getWidth(), BOARD_WIDTH);
    }

    @Test
    public void testSetTile() {
        char item = 'A';
        board.setTile(0, 0, item);
        assertEquals("Board item at (0,0) should be: " + item, board.getTile(0,0), item);
    }

    @Test
    public void testGetBoard() {
        char[][] testBoard;
        testBoard = board.getBoard();
        assertNotNull("Board should not be null", testBoard);
        assertEquals("Board height should be: " + BOARD_HEIGHT, testBoard.length, BOARD_HEIGHT);
        assertEquals("Board width should be: " + BOARD_WIDTH, testBoard[0].length, BOARD_WIDTH);
    }

    @Test
    public void testSetBoard() {
        char item = 'A';
        char[][] newBoard = new char[BOARD_WIDTH][BOARD_HEIGHT];
        newBoard[0][0] = item;
        board.setBoard(newBoard);
        assertNotNull("Board should not be null", board.getBoard());
        assertEquals("Board should have item '" + item +"' at (0,0)", board.getBoard()[0][0], item);
    }

    @After
    public void tearDown() throws Exception {

    }
}