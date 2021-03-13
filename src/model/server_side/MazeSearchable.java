package model.server_side;

import java.util.ArrayList;
import java.util.List;

public class MazeSearchable implements Searchable<State<Grid>> {
    Maze maze;

    public MazeSearchable(Maze maze) {
        this.maze = maze;
    }

    @Override
    public State<Grid> startState() {
        return new State<Grid>(maze.getEntrance(), maze.getValue(maze.entrance));
    }

    @Override
    public Boolean isGoal(State<Grid> state) {
        return maze.getExit().equals(state.getData());
    }

    @Override
    public List<State<Grid>> getPossibleNeighbors(State<Grid> state) {
        List<State<Grid>> result = new ArrayList<>();

        maze.getPossibleMoves(state.getData())
                .forEach(move -> result.add(new State<Grid>(move, state, state.getCost() + maze.getValue(move))));

        return result;
    }
}