package com.lugolbis.mathematics;

import java.util.ArrayList;

public class Point {
    private ArrayList<Double> coordinates;

    public Point(double... coordinates) {
        ArrayList<Double> coordinatesValues = new ArrayList<>();
        for (double value : coordinates) {
            coordinatesValues.add(value);
        }
        this.coordinates = coordinatesValues;
    }

    public Point(ArrayList<Double> array) {
        coordinates = array;
    }

    public ArrayList<Double> getCoordinates() {
        return new ArrayList<>(coordinates);
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        if (coordinates instanceof ArrayList<?>) {
            ArrayList<Double> array = new ArrayList<>(coordinates);
            this.coordinates = array;
        }
    }

    public int getDimensions() {
        return coordinates.size();
    }
}
