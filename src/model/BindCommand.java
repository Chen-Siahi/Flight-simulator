package model;

import static model.MyInterpreter.bindTable;
import static model.MyInterpreter.symbolTable;

public class BindCommand implements Command{
    @Override
    public int doCommand(String[] command, int index) {
        String bindTo = command[++index];
        String firstWord = command[0];
        String secondWord = command[1];

        if(symbolTable.containsKey(firstWord)){
            bindTable.put(firstWord, bindTo);
        }
        else if(symbolTable.containsKey(secondWord)){
            bindTable.put(secondWord, bindTo);
        }
        return 0;
    }
}
