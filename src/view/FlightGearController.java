package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Joystick;
import model.MapDisplay;
import view_model.ViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class FlightGearController implements Observer {
    ViewModel vm;
    Joystick joystick;
    Stage s1, s2;//Window
    @FXML
    Circle big;
    @FXML
    Circle small;
    MapDisplay map;
    @FXML
    Slider rudder;
    @FXML
    Slider throttle;
    @FXML
    Button file;
    @FXML
    Label simulatorStatus;
    @FXML
    Label mazeStatus;
    @FXML
    Canvas MapDisplayer;
    @FXML
    Button loadMap;
    @FXML
    Button CalculatePath;
    @FXML
    TextArea autoPilot;
    @FXML
    RadioButton autoRadioButton;
    @FXML
    RadioButton manualRadioButton;

    public void setViewModel(ViewModel vm) {
        this.vm = vm;
        simulatorStatus.textProperty().bind(vm._status);
        mazeStatus.textProperty().bind(vm._mazeStatus);


        joystick = new Joystick(big, small);
        joystick.addObserver(this);
        vm._autoPilotScript.bind(autoPilot.textProperty());
        vm.aileron.bind(joystick.varXProperty());
        vm.elevator.bind(joystick.varYProperty());
        vm.rudder.bindBidirectional(rudder.valueProperty());
        vm.throttle.bindBidirectional(throttle.valueProperty());
    }

    public void connectWindow() throws IOException {
        FXMLLoader fxl = new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("Connection.fxml").openStream());
        ConnectionController cw = fxl.getController();
        s1 = new Stage();
        s1.setScene(new Scene(root));
        s1.setTitle("Connection");
        s1.setResizable(false);
        cw.setViewModel(vm, s1);
        s1.show();
    }

    public void MazConnectWindow() throws IOException {
        FXMLLoader fxl = new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("MazeConnection.fxml").openStream());
        MazeConnectionController cw = fxl.getController();
        s2 = new Stage();
        s2.setScene(new Scene(root));
        s2.setTitle("Maze Server Connection");
        s2.setResizable(false);
        cw.setViewModel(vm, s2);
        s2.show();
    }

    public void openTextFile() throws IOException {
        if (autoPilot.getText().equals("")) {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open script file");
            fc.setInitialDirectory(new File("."));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File chosen = fc.showOpenDialog(null);
            if (chosen != null) {
                BufferedReader reader = new BufferedReader(new FileReader(new File(chosen.getPath())));
                String line;
                while ((line = reader.readLine()) != null) {
                    autoPilot.appendText(line + "\n");
                    System.out.println(line);
                }
            }
        }
    }

    public void loadCSVFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open map file");
        fc.setInitialDirectory(new File("."));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            String csvFile = chosen.getPath();
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String[] params;
                double latitude, longitude, scale;
                ArrayList<String> mapArray = new ArrayList<>();
                params = br.readLine().split(",");
                latitude = Double.parseDouble(params[1]);//-157
                longitude = Double.parseDouble(params[0]);
                params = br.readLine().split(",");
                scale = Double.parseDouble((params[0]));//152X247  0.006
                System.out.println(latitude + "#" + longitude);
                System.out.println(scale);
                while ((line = br.readLine()) != null) {
                    mapArray.add(line);
                }
                map = new MapDisplay(MapDisplayer, mapArray);
                map.airplaneLatLongProperty().bindBidirectional(vm._airplaneLatLong);
                vm._mazeStartPosition.bind(map.startStringProperty());
                vm._mazeEndPosition.bind(map.destStringProperty());
                vm._mazeMap.bind(map.mapStringProperty());
                map.mazeSolutionProperty().bind(vm._mazeSolution);
                map.setAirplaneLatLong(latitude + "," + longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void mapClicked(MouseEvent event) throws IOException {
        String[] location = map.getDest(event.getX(), event.getY()).split(",");
        map.drawDest(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
        //if (mazeServerConnected)
        vm.solveProblem();

    }

    public void onAutoRadioButtonChecked() {
        setSlidersDisable(true);
        joystick.setEnabled(false);
        vm.startAutoPilot();

    }

    public void onManualRadioButtonChecked() {
        setSlidersDisable(false);
        joystick.setEnabled(true);
        vm.stopAutoPilot();
    }

    private void setSlidersDisable(boolean val) {
        throttle.setDisable(val);
        rudder.setDisable(val);
    }

    public void onThrottleDragged() {
        vm.setThrottle();

    }

    public void onRudderDragged() {
        vm.setRudder();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == vm) {
            switch (arg.toString()) {
                case "simulatorServerStatus": {
                    Platform.runLater(() -> {

                        if (simulatorStatus.getText().equals("Simulator: Error connecting to simulator")) {
                            setSimulatorStatusColor(Color.RED);
                            popUpMessage(Alert.AlertType.ERROR, "Simulator connection", "Connection failed!");
                            disableRadioButtons(true);
                        } else if (simulatorStatus.getText().equals("Simulator: Connected to simulator")) {
                            setSimulatorStatusColor(Color.GREEN);
                            popUpMessage(Alert.AlertType.INFORMATION, "Simulator connection", "Connected successfully!!");
                            s1.close();
                            disableRadioButtons(false);
                        }
                    });
                    break;
                }
                case "mazeServerStatus": {
                    Platform.runLater(() -> {

                        if (mazeStatus.getText().equals("Maze Server: Error connecting to maze server")) {
                            setMazeStatusColor(Color.RED);
                            popUpMessage(Alert.AlertType.ERROR, "Maze server connection", "Connection failed!");
                        } else if (mazeStatus.getText().equals("Maze Server: Connected to maze server")) {
                            setMazeStatusColor(Color.GREEN);
                            popUpMessage(Alert.AlertType.INFORMATION, "Maze server connection", "Connected successfully!!");
                            s2.close();
                        }
                    });
                    break;
                }
                case "GotMazeSolution":
                    map.drawPath();
                    break;
                case "AirplanePositionChanged":
                    if (map != null)
                        map.updateAirplanePosition();
                    break;
            }
        }
        if (o == joystick)
            vm.updateJoystickValues();
    }

    private void setSimulatorStatusColor(Color color) {
        simulatorStatus.textFillProperty().setValue(color);
    }

    private void setMazeStatusColor(Color color) {
        mazeStatus.textFillProperty().setValue(color);
    }

    private void popUpMessage(Alert.AlertType information, String title, String message) {
        Alert a = new Alert(information);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(message);
        a.showAndWait();
    }

    private void disableRadioButtons(boolean value) {
        autoRadioButton.setDisable(value);
        manualRadioButton.setDisable(value);
    }



}

