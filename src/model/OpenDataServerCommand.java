package model;

public class OpenDataServerCommand implements Command {

    @Override
    public int doCommand(String[] command, int index) {

        int port = Integer.parseInt(command[++index]);
        int timeOut = Integer.parseInt(command[++index]);
        OpenDataServer openDataServer=new OpenDataServer(port,timeOut);
        if(!OpenDataServer.isRunning) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    openDataServer.runServer();
                }
            }).start();
        }
        return 0;
    }
}
