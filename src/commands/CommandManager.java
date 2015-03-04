package commands;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> undos = new Stack<Command>();

    public CommandManager() {}

    public void executeCommand(Command c) {
        c.execute();
        undos.push(c);
    }


    public boolean isUndoAvailable() {
        return !undos.empty();
    }

    public void undo() {
        if(!undos.empty()) {
            Command command = undos.pop();
            command.undo();
        }
    }

}
