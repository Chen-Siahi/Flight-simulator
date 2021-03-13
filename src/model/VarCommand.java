package model;

import static model.MyInterpreter.cleanLine;
import static model.MyInterpreter.simplify;

public class VarCommand implements Command {
    @Override
    public int doCommand(String[] command, int index) {//var x = bind simX
        StringBuilder expression = new StringBuilder();
        String variable = command[++index];

        MyInterpreter.symbolTable.put(variable, (double) 0);

        if (command.length >= 3) {
            if (command[3].equals("bind")) {//var x = bind ""
                new BindCommand().doCommand(command, 3);
            } else {
                for (int i = 3; i < command.length; i++) {
                    expression.append(command[i]);
                }
                if (!expression.toString().isEmpty()) {
                    String cleanedSimplifiedExpression = cleanLine(simplify(expression.toString()));
                    MyInterpreter.symbolTable.put(variable, CalculateExpression.calc(cleanedSimplifiedExpression));
                    //MyInterpreter.bindTable.put(variable,cleanedSimplifiedExpression);
                }
            }
        }
        return 0;
    }
}
