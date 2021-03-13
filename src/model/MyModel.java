package model;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements Model, Observer {
    Connect connect;
    MazeConnection mazeConnection;
    String simulatorServerStatus;
    String mazeServerStatus;
    Thread autoPilotThread;


    double throttle, rudder;
    private OpenDataServer openDataServer;

    public void disconnect(){
        Connect.setStop();
        OpenDataServer.setStop();
        MazeConnection.setStop();
    }
    public static void sendMessage(String msg) {
        Connect.send(msg);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        if (o == connect) {
            setSimulatorServerStatus(arg.toString());
            notifyObservers("simulatorServerStatus");
        } else if (o == mazeConnection) {
            if(arg.toString().equals("Got maze solution"))
            {
                notifyObservers("GotMazeSolution");
            }
            else{
            setMazeServerStatus(arg.toString());
            notifyObservers("mazeServerStatus");
            }
        }
        else if(o==openDataServer){
            updateSliders();
        }

    }

    public void openDataServer() {
        openDataServer = new OpenDataServer(5400, 1000);
        openDataServer.addObserver(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                openDataServer.runServer();
            }
        }).start();
    }

    @Override
    public void connect(String ip, int port) {
        connect = new Connect(ip, port);
        connect.addObserver(this);//set to viewModel->viewModel set to view(controller)
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect.run();
            }
        }).start();
    }

    @Override
    public void connectToMaze(String ip, int port) {
        mazeConnection = new MazeConnection(ip, port);
        mazeConnection.addObserver(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mazeConnection.run();
            }
        }).start();
    }
    public void startAutoPilot(String[] script) {
        MyInterpreter.setRun(true);
     autoPilotThread = new Thread(new Runnable() {
            public void run() {
                MyInterpreter.interpret(script);
            }
        });
        autoPilotThread.start();
    }


    public void solveProblem(String problem)
    {
        try {
            if(mazeConnection !=null)
                mazeConnection.solve(problem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getSolution(){
        return mazeConnection.getSolution();
    }


    public String getSimulatorServerStatus() {
        return simulatorServerStatus;
    }

    public void setSimulatorServerStatus(String simulatorServerStatus) {
        this.simulatorServerStatus = simulatorServerStatus;
    }
    public String getMazeServerStatus() {
        return mazeServerStatus;
    }

    public void setMazeServerStatus(String mazeServerStatus) {
        this.mazeServerStatus = mazeServerStatus;
    }


    public void stopAutoPilot() {
        MyInterpreter.setRun(false);
    }

    public void updateJoystickValues(double aileron, double elevator) {
        try {
            Connect.commandsQueue.put("set /controls/flight/aileron " + aileron);
            Connect.commandsQueue.put("set /controls/flight/elevator " + elevator);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateSliders(){
        throttle = MyInterpreter.symbolTable.get("/controls/engines/current-engine/throttle");
        rudder = MyInterpreter.symbolTable.get("/controls/flight/rudder");
        setChanged();
        notifyObservers("SlidersUpdated");
    }
    public double[] getSlidersValue() {
        return new double[]{throttle, rudder};
    }

    public void setThrottle(double value)
    {
        try {
            Connect.commandsQueue.put("set /controls/engines/current-engine/throttle " + value);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void setRudder(double value)
    {
        try {
            Connect.commandsQueue.put("set /controls/flight/rudder " + value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
