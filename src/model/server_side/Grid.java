package model.server_side;

// a position class for row, col
public class Grid{
    public int row;
    public int col;

    public Grid(int row, int col){
        this.row=row;
        this.col=col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid = (Grid) o;
        return row == grid.row &&
                col == grid.col;
    }

    @Override
    public int hashCode(){
        return (row+","+col).hashCode();
    }

    @Override
    public String toString(){
        return (row+","+col);
    }
}