package othello;

public class Othello {

    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;

    public static void main(String[] args) {
        OthelloModel model = new OthelloModel(WIDTH, HEIGHT);
        Controller controller = new Controller(model);
        View view = new View();

        model.addObserver(view);
        view.addController(controller);
        model.updateBoard();
    }
}
