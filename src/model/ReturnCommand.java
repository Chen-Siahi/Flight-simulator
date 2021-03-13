package model;

import static model.MyInterpreter.cleanLine;
import static model.MyInterpreter.simplify;

public class ReturnCommand implements Command {
    @Override
    public int doCommand(String[] command, int index) {
        StringBuilder expression = new StringBuilder();
        for (int i = ++index; i < command.length; i++)
            expression.append(command[i]);
        return (int)CalculateExpression.calc(simplify(cleanLine(expression.toString())));
    }
}
