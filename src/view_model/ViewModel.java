package view_model;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.MyModel;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    public StringProperty _ip;
    public StringProperty _port;
    public StringProperty _status;
    public StringProperty _mazeStatus;
    public StringProperty _autoPilotScript;
    public StringProperty _mazeServerIP, _mazeServerPort;
    public DoubleProperty throttle, rudder, aileron, elevator;

    public StringProperty _airplaneLatLong, _mazeStartPosition, _mazeEndPosition, _mazeMap, _mazeSolution;
    MyModel _model;

    public ViewModel(MyModel model) {
        this._model = model;
        this._port = new SimpleStringProperty();
        this._ip = new SimpleStringProperty();

        this._status = new SimpleStringProperty();
        this._autoPilotScript = new SimpleStringProperty();

        this._mazeStatus = new SimpleStringProperty();
        this._mazeServerIP = new SimpleStringProperty();
        this._mazeServerPort = new SimpleStringProperty();
        this._airplaneLatLong = new SimpleStringProperty();
        this._mazeStartPosition = new SimpleStringProperty();
        this._mazeEndPosition = new SimpleStringProperty();
        this._mazeMap = new SimpleStringProperty();
        this._mazeSolution = new SimpleStringProperty();
        this._mazeStatus.set("Maze Server: Not Connected");

        _status.set("Simulator: Not Connected");
        _model.openDataServer();

        throttle = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        if (o == _model) {
            switch (arg.toString()) {
                case "simulatorServerStatus":
                    Platform.runLater(() -> _status.set("Simulator: " + _model.getSimulatorServerStatus()));
                    notifyObservers(arg.toString());
                    break;
                case "mazeServerStatus":
                    Platform.runLater(() -> _mazeStatus.set("Maze Server: " + _model.getMazeServerStatus()));
                    notifyObservers(arg.toString());
                    break;
                case "GotMazeSolution":
                    _mazeSolution.set(_model.getSolution());
                    notifyObservers(arg);
                    break;
                case "SlidersUpdated":
                    throttle.set(_model.getSlidersValue()[0]);
                    rudder.set(_model.getSlidersValue()[1]);
            }

        }
    }

    public void connect() {
        this._model.connect(_ip.get(), Integer.parseInt(_port.get()));
    }

    public void connectMaz() {
        this._model.connectToMaze(this._mazeServerIP.get(), Integer.parseInt(_mazeServerPort.get()));
    }

    public void startAutoPilot() {
        String[] script = _autoPilotScript.get().split("\n");
        this._model.startAutoPilot(script);
    }
    public void stopAutoPilot() {
        _model.stopAutoPilot();
    }

    public void setThrottle() {
        _model.setThrottle(throttle.get());
    }

    public void setRudder() {
        _model.setRudder(rudder.get());
    }


    public void solveProblem() {
        StringBuilder problem = new StringBuilder();
        problem.append(_mazeMap.get());
        problem.append("end\n");
        problem.append(_mazeStartPosition.get() + "\n");
        problem.append(_mazeEndPosition.get());
        _model.solveProblem(problem.toString());
    }

    public void updateJoystickValues() {
        _model.updateJoystickValues(aileron.get(), elevator.get());
    }
}
