package othello;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    OthelloModel model;

    public Controller(OthelloModel model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
