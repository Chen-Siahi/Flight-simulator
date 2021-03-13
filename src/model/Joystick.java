package model;


import java.util.Observable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Joystick extends Observable {
    Circle border, control;
    boolean joystickEnabled;
    DoubleProperty varX; //aileron
    DoubleProperty varY; //elevator

    public Joystick(Circle borderCircle, Circle controlCircle) {
        varX = new SimpleDoubleProperty();
        varY = new SimpleDoubleProperty();
        this.border = borderCircle;
        this.control = controlCircle;
        returnToCenter();
        control.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (joystickEnabled) {
                    returnToCenter();
                }
            }
        });
        control.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (joystickEnabled) {
                    double mouseX = mouseEvent.getX();
                    double mouseY = mouseEvent.getY();
                    double maxX, maxY;
                    double minX, minY;
                    maxX = maxY = border.getRadius() - control.getRadius();
                    minX = minY = -border.getRadius() + control.getRadius();
                    mouseX = mouseX > maxX ? maxX : mouseX;
                    mouseY = mouseY > maxY ? maxY : mouseY;
                    mouseX = mouseX < minX ? minX : mouseX;
                    mouseY = mouseY < minY ? minY : mouseY;
                    control.setCenterX(mouseX);
                    control.setCenterY(mouseY);
                    varX.set(1 / (border.getRadius() - control.getRadius()) * mouseX);
                    varY.set(1 / (border.getRadius() - control.getRadius()) * mouseY);
                    System.out.println(varX.get() + "#" + varY.get());
                    setChanged();
                    notifyObservers();
                }
            }
        });
    }

    private void returnToCenter() {
        control.setCenterX(border.getCenterX());
        control.setCenterY(border.getCenterY());
        varX.set(0);
        varY.set(0);
        setChanged();
        notifyObservers();
    }

    public void setEnabled(boolean enabled) {
        joystickEnabled = enabled;
    }
    public DoubleProperty varXProperty() {
        return varX;
    }
    public DoubleProperty varYProperty() {
        return varY;
    }
}