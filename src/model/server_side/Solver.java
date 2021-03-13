package model.server_side;

public interface Solver<S,P> {
    S solve(P problem);
}