package othello;

public class Othello {

    public static void main(String[] args) {
        OthelloModel model = new OthelloModel();
        Controller controller = new Controller(model);
        View view = new View();

        model.addObserver(view);
        view.addController(controller);
    }
}
