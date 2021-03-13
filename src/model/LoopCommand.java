package model;

import static model.MyInterpreter.*;

public class LoopCommand implements Command{//while x < 5 {  /n y = y + 2 /n x = x + 1 /n }/n

    @Override
    public int doCommand(String[] command, int index) {
        double arg1 = getValue(command[0], ++index);//x->0
        String operator = lexer(command[0])[++index];//<
        double arg2 = getValue(command[0], ++index);//5

        String toString = stringArrayToString(command, true).trim();
        String block = toString.substring(toString.indexOf("{")+1, toString.indexOf('}')).trim();
        String[] commands = block.split("\n");
        switch (operator) {
            case "<":
                while (arg1 < arg2 && MyInterpreter.isRun()) { //0<5
                    parser(commands);
                    arg1 = getValue(command[0], 1);
                    arg2 = getValue(command[0], 3);
                }break;
            case ">":
                while (arg1 > arg2 && MyInterpreter.isRun()) {
                    parser(commands);
                    arg1 = getValue(command[0], 1);
                    arg2 = getValue(command[0], 3);
                }break;
            case "==":
                while (arg1 == arg2 && MyInterpreter.isRun()) {
                    parser(commands);
                    arg1 = getValue(command[0], 1);
                    arg2 = getValue(command[0], 3);
                }break;
            case "!=":
                while (arg1 != arg2 && MyInterpreter.isRun()) {
                    parser(commands);
                    arg1 = getValue(command[0], 1);
                    arg2 = getValue(command[0], 3);
                }break;
            case ">=":
                while (arg1 >= arg2 && MyInterpreter.isRun()) {
                    parser(commands);
                    arg1 = getValue(command[0], 1);
                    arg2 = getValue(command[0], 3);
                }break;
            case "<=":
                while (arg1 <= arg2 && MyInterpreter.isRun()) {
                    parser(commands);
                    arg1 = getValue(command[0], 1);
                    arg2 = getValue(command[0], 3);
                }break;
        }
        return 0;
    }
    public static double getValue(String line, int index){
        return Double.parseDouble(simplify(cleanLine(lexer(line)[index])));

    }
}
