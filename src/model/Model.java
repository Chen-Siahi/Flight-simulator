package model;

public interface Model {
    void connect(String ip, int port);
    void startAutoPilot(String[] script);
    void connectToMaze(String ip, int port);


}
