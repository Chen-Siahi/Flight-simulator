package model.server_side;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    // maze data
    int[][] data;
    Grid entrance;
    Grid exit;

    // CTOR
    public Maze(int[][] data, Grid entrance, Grid exit) {
        this.data=data;
        this.entrance = entrance;
        this.exit = exit;
    }

    public int getValue(Grid g) {
        return data[g.row][g.col];
    }
    // returns the grid position of the entrance - 2 in the data
    public Grid getEntrance(){
        return this.entrance;
    }

    // returns the grid position of the entrance - 3 in the data
    public Grid getExit(){
        return this.exit;
    }

    // helper
    private boolean isInBound(int i, int j){
        return(i>=0 && i<data.length && j>=0 && j<data[i].length);
    }

    // returns a list of possible grid positions given a grid position
    List<Grid> getPossibleMoves(Grid g){
        int i=g.row, j=g.col;
        if(isInBound(i,j)){
            List<Grid> neighbors=new ArrayList<>();
            if(isInBound(i, j-1)) neighbors.add(new Grid(i,j-1));
            if(isInBound(i-1, j)) neighbors.add(new Grid(i-1,j));
            if(isInBound(i, j+1)) neighbors.add(new Grid(i,j+1));
            if(isInBound(i+1, j)) neighbors.add(new Grid(i+1,j));
            return neighbors;
        }
        return null;
    }

    public static void followSolverDirectionsGoalCheck(List<String> actions, Grid p) {
        actions.forEach(s->{
            if(s.equals("UP")) p.row--;
            if(s.equals("DOWN")) p.row++;
            if(s.equals("RIGHT")) p.col++;
            if(s.equals("LEFT")) p.col--;
        });
    }

}