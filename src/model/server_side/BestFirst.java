package model.server_side;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BestFirst extends CommonSearcher{


    @Override
    public Backtrace search(Searchable<State> searchable) {
        PriorityQueue<State> q = new PriorityQueue<>(Comparator.comparingDouble(State::getCost));
        q.add(searchable.startState());
        while(!q.isEmpty())
        {
            State n = q.poll();
            visited.add(n);
            if(searchable.isGoal(n)){
                return n.backtrace();
            }
            searchable.getPossibleNeighbors(n).forEach(s -> {
                if(!visited.contains(s)&&!q.contains(s)) {
                    q.add(s);
                } else {
                    if(!q.contains(s)) {
                        q.add(s);
                    } else {
                        State tmp = null;
                        for (State s1 : q) {
                            if(s.equals(s1) && s.getCost() < s1.getCost()) {
                                tmp = s1;
                                break;
                            }
                        }
                        if(tmp != null) {
                            q.remove(tmp);
                            q.add(s);
                        }
                    }
                }

            });
        }
        return null;
    }
}