package model.server_side;
import java.util.HashSet;

public abstract class CommonSearcher implements Searcher{
    Solution solution;
    HashSet<State> visited;

    public CommonSearcher() {
        this.solution = new Solution();
        this.visited = new HashSet<>();
    }

}