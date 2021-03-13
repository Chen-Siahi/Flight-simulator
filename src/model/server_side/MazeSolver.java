package model.server_side;

import java.util.Collections;

public class MazeSolver implements Solver<Solution, Problem> {
    @Override
    public Solution solve(Problem problem) {
        Searcher searcher = new BestFirst();
        Backtrace<State<Grid>> bt = searcher.search(MazeHelper.problemToMazeSearchable(problem));
        Collections.reverse(bt);

        return MazeHelper.backtraceToSolution(bt);
    }

    public static void main(String[] args) {

        Problem problem = new Problem();
        problem.add("10,15,2,8");
        problem.add("20,4,14,5");
        problem.add("1,4,100,3");
        problem.add("end");
        problem.add("0,2");
        problem.add("2,1");
        System.out.println(new MazeSolver().solve(problem));

    }
}