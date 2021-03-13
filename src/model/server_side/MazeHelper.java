package model.server_side;

public class MazeHelper {
    static Searchable problemToMazeSearchable(Problem problem) {
        int numOfRows = problem.size()-3;
        int numofCols = problem.get(0).split(",").length;
        int[][] data = new int[ numOfRows][numofCols];

        for (int i = 0; i < numOfRows; i++) {
            String[] row = problem.get(i).split(",");
            for(int j=0; j < numofCols; j++) {
                data[i][j] = Integer.valueOf(row[j]);
            }
        }
        String[] tmpValues = problem.get(problem.size()-2).split(","); // "0,12" => ["0","12"] for example;
        Grid enter = new Grid(Integer.valueOf(tmpValues[0]),Integer.valueOf(tmpValues[1]));

        tmpValues = problem.get(problem.size()-1).split(","); // "0,12" => ["0","12"] for example;
        Grid exit = new Grid(Integer.valueOf(tmpValues[0]),Integer.valueOf(tmpValues[1]));

        return new MazeSearchable(new Maze(data, enter,exit));
    }

    static Solution backtraceToSolution(Backtrace<State<Grid>> bt) {
        Solution solution = new Solution();
        for (int i = 0; i < bt.size()-1; i++) {
            if(bt.get(i).getData().row == bt.get(i+1).getData().row) {
                // so to moving horizontal
                if(bt.get(i).getData().col > bt.get(i+1).getData().col) {
                    solution.add("Left");
                } else {
                    solution.add("Right");
                }
            }
            if(bt.get(i).getData().col == bt.get(i+1).getData().col) {
                // moving is vertical
                if(bt.get(i).getData().row > bt.get(i+1).getData().row) {
                    solution.add("Up");
                } else {
                    solution.add("Down");
                }
            }
        }
        return solution;
    }
}