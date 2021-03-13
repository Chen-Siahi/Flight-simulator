package model.server_side;



import java.io.IOException;

public class MazeServer {

    public static void main(String[] args) {
        Server server = new MySerialServer(new MazeSolver(), new FileCacheManager());
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start(5403);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }
}