package model.server_side;

import java.util.Objects;

public class State<T> {
    private T data;
    private State<T> cameFrom;
    private double cost;

    public State(T data, double cost) {
        this.data = data;
        this.cost = cost;
        this.cameFrom = null;
    }

    public State(T data, State<T> cameFrom, double cost) {
        this.data = data;
        this.cameFrom = cameFrom;
        this.cost = cost;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public State<T> getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(State<T> cameFrom) {
        this.cameFrom = cameFrom;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Backtrace<State<T>> backtrace() {
        Backtrace<State<T>> backtrace = new Backtrace<>(); // Simple ArrayList<State<T>
        State<T> current = this;
        backtrace.add(current);

        while(current.cameFrom != null) {
            backtrace.add(current.cameFrom);
            current = current.cameFrom;
        }

        return backtrace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State<?> state = (State<?>) o;
        return Objects.equals(data, state.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}