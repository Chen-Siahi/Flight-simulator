package model;

public class DisconnectCommand implements Command {
    @Override
    public int doCommand(String[] command, int index) {
        try {
            Thread.sleep(2000);
            Connect.setStop();
            //OpenDataServer.setStop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
