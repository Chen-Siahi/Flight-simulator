package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.MyModel;
import view_model.ViewModel;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage s1) throws Exception {
        MyModel m = new MyModel();
        ViewModel vm = new ViewModel(m);
        m.addObserver(vm);
        FXMLLoader fx = new FXMLLoader();
        Parent root = fx.load(getClass().getResource("FlightGear.fxml").openStream());
        FlightGearController c = fx.getController(); // View
        c.setViewModel(vm);
        vm.addObserver(c);
        s1.setTitle("FlightGear");
        s1.setResizable(false);
        s1.setScene(new Scene(root));

        s1.show();
        s1.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                m.disconnect();
                System.exit(0);
            }
        });
    }
}
