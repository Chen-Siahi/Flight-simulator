package model.server_side;

import java.util.List;

public interface Searchable<T extends State> {
    T startState();
    Boolean isGoal(T state);
    List<T> getPossibleNeighbors(T state);

}