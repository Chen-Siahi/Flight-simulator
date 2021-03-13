package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view_model.ViewModel;

public class MazeConnectionController {
    ViewModel vm;
    Stage s;
    @FXML
    TextField IP, Port;

    public void onConnect() {
        vm.connectMaz();
    }

    public void setViewModel(ViewModel vm, Stage stage) {
        this.vm = vm;
        this.s = stage;
        vm._mazeServerIP.bind(IP.textProperty());
        vm._mazeServerPort.bind(Port.textProperty());
    }

    public void close() {
        s.close();
    }

}
