package model;

import static model.MyInterpreter.cleanLine;
import static model.MyInterpreter.simplify;

public class PrintCommand implements Command{

    @Override
    public int doCommand(String[] command, int index) {
        if(command[index +1].startsWith("\""))
            System.out.println(command[index + 1].substring(1, command[index + 1].length() - 1));
        else {

            StringBuilder expression = new StringBuilder();
            for (int i = ++index; i < command.length; i++)
                expression.append(command[i]);

            System.out.println(CalculateExpression.calc(simplify(cleanLine(expression.toString()))));
        }
        return 0;
    }
}
