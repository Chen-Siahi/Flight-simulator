package model.server_side;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySerialServer implements Server{
    private Boolean stop;
    Solver<Solution,Problem> solver;
    CacheManager cm;
    public MySerialServer(Solver<Solution, Problem> solver, CacheManager cm) {
        this.solver = solver;
        this.cm = cm;
        this.stop = false;
    }
    @Override
    public void start(int port) throws IOException {
        ServerSocket s = new ServerSocket(port);
        MyClientHandler c = new MyClientHandler(solver, cm);
        while (!stop) {
            Socket client = s.accept();
            new Thread(() -> {
                try {
                    c.handleClient(client.getInputStream(), client.getOutputStream());
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    @Override
    public void stop() {
        if(!this.stop)
            this.stop=true;
    }
}