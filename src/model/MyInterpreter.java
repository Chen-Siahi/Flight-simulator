package model;

import java.util.HashMap;

public class MyInterpreter {

    public static HashMap<String, Double> symbolTable = new HashMap();
    public static HashMap<String, String> bindTable = new HashMap();
    private static final HashMap<String, Command> commandsTable = new HashMap() {{
        put("return", new ReturnCommand());
        put("while", new LoopCommand());
        put("var", new VarCommand());
        put("openDataServer", new OpenDataServerCommand());
        put("bind", new BindCommand());
        put("disconnect", new DisconnectCommand());
        put("print", new PrintCommand());
        put("sleep", new SleepCommand());

    }};
    private static boolean run = true;

    public static int interpret(String[] lines) {
        return parser(lines);
    }

    public static String[] lexer(String lex) {

        return (lex).replace("=", " = ").trim().split("\\s+");
    }

    public static int parser(String[] lines) {
        StringBuilder commandsInBlock = new StringBuilder();//while x<5 {y=y+2  x=x+1}
        boolean isInBlock = false;
        for (int i = 0; i < lines.length; i++) {
            if(!isRun())
                break;
            String[] splitedLine = lexer(lines[i]);//	}
            String firstWord = splitedLine[0];//}

            if (firstWord.equals("while") || isInBlock) {//}
                isInBlock = true;
                commandsInBlock.append(stringArrayToString(splitedLine, false));//}
                //commandsInBlock: while x < 5 { /n y = y + 2 /n x = x + 1 /n }/n
                if (firstWord.equals("}")) {
                    isInBlock = false;
                    new LoopCommand().doCommand(commandsInBlock.toString().split("\n"), 0);
                }
                continue;
            }


            if (commandsTable.containsKey(firstWord)) {
                Command theCommand = commandsTable.get(firstWord);
                if (theCommand.getClass().getSimpleName().equals("ReturnCommand"))
                    return theCommand.doCommand(splitedLine, 0);
                theCommand.doCommand(splitedLine, 0);
            } else if (symbolTable.containsKey(firstWord)) //x = bind simX    //x = y + 3
            {
                String expressionOrBind = splitedLine[2];
                if (expressionOrBind.equals("bind")) {
                    Command theCommand = commandsTable.get("bind");
                    theCommand.doCommand(splitedLine, 2);
                } else {
                    StringBuilder expression = new StringBuilder();
                    for (int j = 2; j < splitedLine.length; j++)
                        expression.append(splitedLine[j]);//y+3

                    //System.out.println("EXPRESSION: " + expression.toString());
                    Double value = CalculateExpression.calc(simplify(cleanLine(expression.toString())));//703 + 3
                    symbolTable.put(firstWord, value);//(x,706)

                    if (bindTable.containsKey(firstWord))//x,simX
                    {
                        String bindedVariable = bindTable.get(firstWord); //simX
                        //symbolTable.put(bindedVariable, value);    //set simX 706
                        MyModel.sendMessage("set ".concat(bindedVariable).concat(" ").concat(value.toString()));

                    }
                }
            }
        }
        return 0;
    }

    //symbolTable:(x,706) , (simX,706) , (y, 800)
    //bindTable:(x,simX)
    public static String simplify(String expression) { //y + 7
        for (String variable : symbolTable.keySet()) {
            if (bindTable.containsKey(variable))
                expression = expression.replace(variable, String.valueOf(symbolTable.get(bindTable.get(variable))));
            else
                expression = expression.replace(variable, String.valueOf(symbolTable.get(variable)));
        }
        return expression;
    }

    public static String cleanLine(String line) {
        if (line != null) {
            return line.replace("=", " = ")
                    .replace("+", " + ")
                    .replace("-", " - ")
                    .replace("*", " * ")
                    .replace(")", " ) ")
                    .replace("(", " ( ")
                    .replace("<", " < ")
                    .replace(">", " > ")
                    .trim();

        }
        return null;
    }

    public static String stringArrayToString(String[] arr, boolean withNewLine) {//	x = x + 1 {
        StringBuilder theString = new StringBuilder();
        for (String s : arr) {
            theString.append(s).append(" ");//	x = x + 1
            if (withNewLine)
                theString.append("\n");
        }
        return theString.append("\n").toString();//	}/n
    }
    public static void setRun(boolean value){
        run = value;
    }
    public static boolean isRun(){
        return run;
    }
}

