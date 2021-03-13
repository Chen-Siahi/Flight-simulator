package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MazeConnection extends Observable {
    public static boolean stop= false;
    String ip;
    int port;
    public static Socket client;
    public static BlockingQueue<String> toSend;
    String solution;
    public MazeConnection(String ip, int port) {
        stop = false;
        this.ip = ip;
        this.port = port;
        toSend = new LinkedBlockingDeque();
    }

    public void run() {
        try {
            client = new Socket(ip, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            updateStatus("Connected to maze server");
            while (!stop) {
                try {
                    String s = toSend.take();
                    out.println(s);
                    out.flush();
                    System.out.println(s);
                    solution = in.readLine();
                    System.out.println("Got solution:");
                    System.out.println(solution);
                    if(solution == null) {
                        updateStatus("Error connecting to maze server");
                        break;
                    }
                    updateStatus("Got maze solution");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus("Error connecting to maze server");
        }
    }
    public static void setStop() {
        try {
            stop= true;
            if(client != null)
                client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void updateStatus(String status){
        setChanged();
        notifyObservers(status);
        System.out.println(status);
    }
    ///
    public void solve(String s) throws InterruptedException {
        toSend.put(s);
    }
    ///
    public String getSolution()
    {
        return solution;
    }
}




