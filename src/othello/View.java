package othello;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


/**
 * GUI for the Othello game
 * @author kevinrosengren
 *
 */
public class View extends JFrame implements Observer {

    private static final int HEADER_HEIGHT = 20;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600 + HEADER_HEIGHT;
    private static final int ICON_WIDTH = 50;
    private static final int ICON_HEIGHT = 50;

    private static final String OTHELLO_TITLE = "Othello";

    private static final String BLACK_DISC_FILE_PATH = "../images/black_piece.png";
    private static final String WHITE_DISC_FILE_PATH = "../images/white_piece.png";

    private static final Color LIGHT_COLORED_TILE = new Color(56,122,54);
    private static final Color DARK_COLORED_TILE = new Color(35,80,33);
    private static final Color HIGHLIGHT_COLORED_TILE = new Color(101, 0, 7);

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;

    private JPanel boardPanel;
    private JPanel scorePanel;

    private JLabel scoreField;
    private JLabel blackScore;
    private JLabel whiteScore;

    private JButton[][] tiles;

    private Image blackDisc;
    private Image whiteDisc;


    /**
     * Constructor
     *
     * initialize graphics user interface
     * for the Othello game
     */
    public View() {

        initIconImages();
        initScoreBoard();
        initBoardLayout();
        initWindow();
    }


    /**
     * initialize board piece icons
     * for black and white discs
     */
    private void initIconImages() {
        try {
            blackDisc = ImageIO.read(getClass().getResource(BLACK_DISC_FILE_PATH));
            blackDisc = blackDisc.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);

            whiteDisc = ImageIO.read(getClass().getResource(WHITE_DISC_FILE_PATH));
            whiteDisc = whiteDisc.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);

        } catch (Exception e) {
            System.out.println("Could not load icon images");
            JOptionPane.showMessageDialog(this, "Error: The game could not locate the images:\n\n\t\t\t\t" + BLACK_DISC_FILE_PATH
                            + "\n\t\t\t\t" + WHITE_DISC_FILE_PATH + "\n\nExiting Application.",
                    "Error Loading Images", JOptionPane.INFORMATION_MESSAGE);

            System.exit(0);
        }
    }

    /**
     * initialize score board.
     * set initial score of zero
     */
    private void initScoreBoard() {

        scorePanel = new JPanel();
        scoreField = new JLabel("Current Score: ");

        whiteScore = new JLabel("White: 0");
        blackScore = new JLabel("Black: 0");

        scorePanel.add(scoreField);
        scorePanel.add(whiteScore);
        scorePanel.add(blackScore);
    }


    /**
     * initialize array of buttons for
     * the checker board and add tiles
     */
    private void initBoardLayout() {

        int hGap = 0;
        int vGap = 0;

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BOARD_WIDTH, BOARD_HEIGHT, hGap, vGap));

        tiles = new JButton[BOARD_HEIGHT][BOARD_WIDTH];

        for (int row = 0; row < tiles.length; row++) {
            if (row % 2 == 0) {
                for (int col = 0; col < tiles[row].length; col++) {
                    if (col % 2 == 0) { // even
                        tiles[row][col] = newSquareTile("dark");
                        boardPanel.add(tiles[row][col]);
                    } else {
                        tiles[row][col] = newSquareTile("light");
                        boardPanel.add(tiles[row][col]);
                    }
                }
            } else {
                for (int col = 0; col < tiles[row].length; col++) {
                    if (col % 2 == 0) { // even
                        tiles[row][col] = newSquareTile("light");
                        boardPanel.add(tiles[row][col]);
                    } else {
                        tiles[row][col] = newSquareTile("dark");
                        boardPanel.add(tiles[row][col]);
                    }
                }
            }
        }
    }


    /**
     * create a single board tile button
     *
     * @param color: light or dark color
     * @return newly created button
     */
    private JButton newSquareTile(String color) {

        JButton button = new JButton("");

        // create border
        Border line = new LineBorder(null, 0);
        Border margin = new EmptyBorder(0, 0, 0, 0); // no padding
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);

        // set tile color
        if (color.equals("dark")) {
            button.setBackground(DARK_COLORED_TILE);
            button.setForeground(DARK_COLORED_TILE);

        } else if (color.equals("light")){
            button.setBackground(LIGHT_COLORED_TILE);
            button.setForeground(LIGHT_COLORED_TILE);
        }

        button.setOpaque(true);

        return button;
    }

    /**
     * initialize window properties
     * and set it to visible
     */
    private void initWindow() {
        setTitle(OTHELLO_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);

        add(scorePanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(boardPanel);
        pack();
        setVisible(true);
    }


    /**
     * replace tile icon with
     * white or black disc
     *
     * @param row: y coordinate
     * @param col: x coordinate
     */
    public void displayPiece(int row, int col, Field color) {
        if (color == Field.BLACK)
            tiles[row][col].setIcon(new ImageIcon(blackDisc));
        else if (color == Field.WHITE)
            tiles[row][col].setIcon(new ImageIcon(whiteDisc));
        else if (color == Field.HOLE)
            tiles[row][col].setIcon(null);
    }

    public void resetBoardColors() {
        for (int row = 0; row < tiles.length; row++) {
            if (row % 2 == 0) {
                for (int col = 0; col < tiles[row].length; col++) {
                    if (col % 2 == 0) { // even
                        colorTile(row, col, DARK_COLORED_TILE);
                    } else {
                        colorTile(row, col, LIGHT_COLORED_TILE);
                    }
                }
            } else {
                for (int col = 0; col < tiles[row].length; col++) {
                    if (col % 2 == 0) { // even
                        colorTile(row, col, LIGHT_COLORED_TILE);
                    } else {
                        colorTile(row, col, DARK_COLORED_TILE);
                    }
                }
            }
        }
    }

    public void colorTile(int row, int col, Color color) {
        tiles[row][col].setBackground(color);
        tiles[row][col].setForeground(color);
        repaint();
    }


    /**
     * set coordinates and add
     * actionListeners to each button
     *
     * @param controller: the controller that holds
     * 					  all of the actionListeners
     */
    public void addController(ActionListener controller) {

        // add buttons
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].setActionCommand( i + "," + j ); // set coordinates
                tiles[i][j].addActionListener(controller);
            }
        }
    }


    /**
     * redraw the graphical board with
     * the current location of each piece
     *
     * @param board: grid to be redrawn
     */
    public void redrawBoard(Board board) {
        Field[][] grid = board.getBoard();

        int white = 0;
        int black = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                if (grid[i][j] == Field.WHITE)  {
                    white++;
                } else if (grid[i][j] == Field.BLACK) {
                    black++;
                }

                displayPiece(i, j, grid[i][j]);
            }
        }

        // Update score board
        whiteScore.setText("White: " + white);
        blackScore.setText("Black: " + black);

        repaint();
    }


    /**
     * display winner and disable board
     */
    public void displayWinner(String winner) {
        scoreField.setText("The Winner is: " + winner);
        JOptionPane.showMessageDialog(this, "The Winner is: " + winner,
                "Game Over", JOptionPane.INFORMATION_MESSAGE);

        close();
    }

    /**
     * update the board and display message
     * when the game model has changed
     */
    @Override
    public void update(Observable o, Object arg) {
        requestFocusInWindow();
        if (arg instanceof String) {
            displayWinner((String)arg);

        } else if (arg instanceof Board) {
            Board board = (Board)arg;
            resetBoardColors();
            redrawBoard(board);
        } else if (arg instanceof int[][]) {

            resetBoardColors();

            int[][] highlightCells = (int[][])arg;
            for (int[] cell : highlightCells) {
                colorTile(cell[0], cell[1], HIGHLIGHT_COLORED_TILE);
            }
        }
    }

    public void close() {
        dispose();
        System.exit(0); // EXIT STATUS 0
    }
}
