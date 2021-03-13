package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class OpenDataServer extends Observable {
    public static ServerSocket openDataServer;
    public static Socket sendingDataClient;
    public static volatile boolean stop = false;
    public static volatile boolean isRunning = false;
    public int port,timeOut;
    public OpenDataServer(int port, int timeOut) {
        this.port=port;
        this.timeOut=timeOut;
    }
   public void runServer(){
        try {
            stop = false;
            openDataServer = new ServerSocket(port);
            sendingDataClient = openDataServer.accept();

            isRunning = true;
            BufferedReader in = new BufferedReader(new InputStreamReader(sendingDataClient.getInputStream()));
            String line = "";
            while (!stop && (line = in.readLine()) != null) {

                System.out.println(line.length());
                String[] values = line.split(",");

                MyInterpreter.symbolTable.put("/instrumentation/airspeed-indicator/indicated-speed-kt", Double.parseDouble(values[0]));
                MyInterpreter.symbolTable.put("/instrumentation/altimeter/indicated-altitude-ft", Double.parseDouble(values[1]));
                MyInterpreter.symbolTable.put("/instrumentation/altimeter/pressure-alt-ft", Double.parseDouble(values[2]));
                MyInterpreter.symbolTable.put("/instrumentation/attitude-indicator/indicated-pitch-deg", Double.parseDouble(values[3]));
                MyInterpreter.symbolTable.put("/instrumentation/attitude-indicator/indicated-roll-deg", Double.parseDouble(values[4]));
                MyInterpreter.symbolTable.put("/instrumentation/attitude-indicator/internal-pitch-deg", Double.parseDouble(values[5]));
                MyInterpreter.symbolTable.put("/instrumentation/attitude-indicator/internal-roll-deg", Double.parseDouble(values[6]));
                MyInterpreter.symbolTable.put("/instrumentation/encoder/indicated-altitude-ft", Double.parseDouble(values[7]));
                MyInterpreter.symbolTable.put("/instrumentation/encoder/pressure-alt-ft", Double.parseDouble(values[8]));
                MyInterpreter.symbolTable.put("/instrumentation/gps/indicated-altitude-ft", Double.parseDouble(values[9]));
                MyInterpreter.symbolTable.put("/instrumentation/gps/indicated-ground-speed-kt", Double.parseDouble(values[10]));
                MyInterpreter.symbolTable.put("/instrumentation/gps/indicated-vertical-speed", Double.parseDouble(values[11]));
                MyInterpreter.symbolTable.put("/instrumentation/heading-indicator/indicated-heading-deg", Double.parseDouble(values[12]));
                MyInterpreter.symbolTable.put("/instrumentation/magnetic-compass/indicated-heading-deg", Double.parseDouble(values[13]));
                MyInterpreter.symbolTable.put("/instrumentation/slip-skid-ball/indicated-slip-skid", Double.parseDouble(values[14]));
                MyInterpreter.symbolTable.put("/instrumentation/turn-indicator/indicated-turn-rate", Double.parseDouble(values[15]));
                MyInterpreter.symbolTable.put("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", Double.parseDouble(values[16]));
                MyInterpreter.symbolTable.put("/controls/flight/aileron", Double.parseDouble(values[17]));
                MyInterpreter.symbolTable.put("/controls/flight/elevator", Double.parseDouble(values[18]));
                MyInterpreter.symbolTable.put("/controls/flight/rudder", Double.parseDouble(values[19]));
                MyInterpreter.symbolTable.put("/controls/flight/flaps", Double.parseDouble(values[20]));
                MyInterpreter.symbolTable.put("/controls/engines/current-engine/throttle", Double.parseDouble(values[21]));
                MyInterpreter.symbolTable.put("/engines/engine/rpm", Double.parseDouble(values[22]));
                MyInterpreter.symbolTable.put("/position/latitude-deg", Double.parseDouble(values[23]));
                MyInterpreter.symbolTable.put("/position/longitude-deg", Double.parseDouble(values[24]));
                try {
                    Thread.sleep(timeOut/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setChanged();
                notifyObservers();
            }
            in.close();
            sendingDataClient.close();
            openDataServer.close();
            isRunning = false;
        } catch (IOException e) {
            isRunning = false;
            e.printStackTrace();
        }
    }
    public static void setStop() {

            stop= true;
        try {
            if(sendingDataClient != null)
            sendingDataClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}