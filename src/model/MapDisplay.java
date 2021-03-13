package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class MapDisplay {
    Canvas myMap;
    ArrayList<String> mapData;
    int planeX, planeY, rows, cols;
    double W, H, w, h, airplaneSize;
    boolean isDestExist;
    int destRow, destCol;
    StringProperty airplaneLatLong;
    StringProperty startString;
    StringProperty destString;
    StringProperty mapString;
    StringProperty mazeSolution;

    public MapDisplay(Canvas map, ArrayList<String> data) throws IOException {
        myMap = map;
        mapData = data;
        airplaneLatLong = new SimpleStringProperty();
        startString = new SimpleStringProperty();
        destString = new SimpleStringProperty();
        mapString = new SimpleStringProperty();
        mazeSolution = new SimpleStringProperty();
        airplaneLatLong.set("0,0");
        rows = mapData.size();
        cols = mapData.get(0).split(",").length;
        W = myMap.getWidth();
        H = myMap.getHeight();
        w = W / cols;
        h = H / rows;
        setAirplaneSize(W / 7);
        isDestExist = false;
        mapString.set(getMapString());
        display();
    }



    public void display() {
        int n, R = 0, G = 0;
        String[] line;
        GraphicsContext gc = myMap.getGraphicsContext2D();

        Image airplane = null;
        Image destination = null;
        try {
            airplane = new Image(ClassLoader.getSystemClassLoader().getResource("airplane2.png").openStream());
            destination = new Image(ClassLoader.getSystemClassLoader().getResource("Destination.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        gc.clearRect(0, 0, W, H);
        for (int i = 0; i < rows; i++) {
            line = mapData.get(i).split(",");
            for (int j = 0; j < cols; j++) {
                n = Integer.parseInt(line[j]) / 10;
                R = (n > 50) ? (255 * (100 - n) / 50) : 255;
                G = (n > 50) ? 255 : (255 * (n * 2)) / 100;

                gc.setFill(Color.rgb(R, G, 0));
                gc.fillRect(j * w, i * h, w, h);
            }
        }
        if (isDestExist)
            gc.drawImage(destination, (int) (destCol * w), (int) (destRow * h), w, h);
        gc.drawImage(airplane, planeX, planeY, getAirplaneSize(), getAirplaneSize());
    }

    public void calculatePlaneXY() {
        String[] parameters = airplaneLatLong.get().split(",");
        double latitude = Double.parseDouble(parameters[0]);
        double longitude = Double.parseDouble(parameters[1]);
        double calculatedWidth = (W - getAirplaneSize());
        double calculatedHeight = (H - getAirplaneSize());
        if (longitude < 0)
            planeX = (int) ((calculatedWidth / 2 / 180) * (longitude + 180));
        else if (longitude >= 0)
            planeX = (int) ((calculatedWidth / 2 / 180) * longitude + calculatedWidth / 2);
        if (latitude < 0)
            planeY = (int) (calculatedHeight - (calculatedHeight / 2 / 90) * (latitude + 90));
        else if (latitude >= 0)
            planeY = (int) ((calculatedHeight / 2 / 90) * latitude + calculatedHeight / 2 - latitude * 2);
    }

    public void updateAirplanePosition() {
        calculatePlaneXY();
        display();
        drawPath();
    }

    public String getDest(double x, double y) {
        int row = (int) (y / h);
        int col = (int) (x / w);
        return row + "," + col;
    }

    public void drawDest(int row, int col) throws IOException {
        isDestExist = true;
        destRow = row;
        destCol = col;
        destString.set(row + "," + col);
        display();
    }

    public void drawPath() {
        if (mazeSolution.get() == null)
            return;
        String[] path = mazeSolution.get().split(",");
        GraphicsContext gc = myMap.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        int r = (int) ((planeY + getAirplaneSize() / 2) / h), c = (int) ((planeX + getAirplaneSize()) / w);
        gc.fillOval(c * w + w / 2, r * h + h / 2, w / 5, h / 5);
        for (int i = 0; i < path.length; i++) {
            if (path[i].equals("Up"))
                r--;
            else if (path[i].equals("Down"))
                r++;
            else if (path[i].equals("Right"))
                c++;
            else if (path[i].equals("Left"))
                c--;
            gc.fillOval(c * w + w / 2, r * h + h / 2, w / 5, h / 5);
        }
    }

    private String getMapString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mapData.size(); i++) {
            stringBuilder.append(mapData.get(i) + "\n");
        }
        return stringBuilder.toString();
    }
    public void setAirplaneLatLong(String airplaneLatLong) {
        this.airplaneLatLong.set(airplaneLatLong);
        calculatePlaneXY();
        setStartString((int) ((planeY + getAirplaneSize() / 2) / h) + "," + (int) ((planeX + getAirplaneSize()) / w));
        display();
    }

    public StringProperty airplaneLatLongProperty() {
        return airplaneLatLong;
    }

    public StringProperty destStringProperty() {
        return destString;
    }

    public StringProperty mapStringProperty() {
        return mapString;
    }

    public StringProperty mazeSolutionProperty() {
        return mazeSolution;
    }

    public double getAirplaneSize() {
        return airplaneSize;
    }

    public void setAirplaneSize(double airplaneSize) {
        this.airplaneSize = airplaneSize;
    }

    public StringProperty startStringProperty() {
        return startString;
    }

    public void setStartString(String startString) {
        this.startString.set(startString);
    }
}
