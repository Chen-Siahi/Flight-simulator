package model;

import static model.MyInterpreter.cleanLine;
import static model.MyInterpreter.simplify;

public class SleepCommand implements Command {
    @Override
    public int doCommand(String[] command, int index) {

        StringBuilder expression = new StringBuilder();
        for (int i = ++index; i < command.length; i++)
            expression.append(command[i]);

        int toSleep = (int)CalculateExpression.calc(simplify(cleanLine(expression.toString())));
        System.out.println("Sleeping " + toSleep +"ms");
        try {
            Thread.sleep(toSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
