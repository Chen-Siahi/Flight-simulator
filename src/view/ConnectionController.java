package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view_model.ViewModel;

public class ConnectionController {

    ViewModel vm;
    Stage s;
    @FXML
    TextField varIP, varPort;

    public void onConnect() {
        vm.connect();
    }

    public void setViewModel(ViewModel vm, Stage stage) {
        this.vm = vm;
        this.s = stage;
        vm._ip.bind(varIP.textProperty());
        vm._port.bind(varPort.textProperty());

    }

    public void close() {
        s.close();
    }
}
