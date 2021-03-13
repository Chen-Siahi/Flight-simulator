package model.server_side;

public interface Searcher {
    Backtrace search(Searchable<State> searchable);
}