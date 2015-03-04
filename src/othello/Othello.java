package othello;

public class Othello {

    public static void main(String[] args) {
//        OthelloModel model = new OthelloModel();
        OthelloModel model = new OthelloModel(8, 8);
        Controller controller = new Controller(model);
        View view = new View();

        model.addObserver(view);
        view.addController(controller);
        model.updateBoard();
    }
}
