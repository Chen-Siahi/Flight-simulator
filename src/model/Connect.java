package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Connect extends Observable {
    private static volatile boolean stop = false;
    private Socket client;
    private String ip;
    private int port;
    public static BlockingQueue<String> commandsQueue = new LinkedBlockingQueue<>();

    public Connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        try {
            stop = false;
            client = new Socket(ip, port);
            updateStatus("Connected to simulator");
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            while (!stop) {
                try {
                    String command = commandsQueue.take();
                    out.println(command);
                    System.out.println("Client: " +command);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.flush();

            }
            out.close();
            client.close();
            updateStatus("Disconnected from simulator");
        } catch (IOException e) {
            //e.printStackTrace();
            updateStatus("Error connecting to simulator");
        }
    }

    public static void setStop() {
        stop = true;
        send("bye");
    }
    public static void send(String message)
    {
        commandsQueue.add(message);
    }

    public void updateStatus(String status){
        setChanged();
        notifyObservers(status);
        System.out.println(status);
    }

}
