package com.lugolbis.mathematics;

import java.util.ArrayList;
import java.util.Optional;

public class Point {
    private ArrayList<Double> coordinates;

    public Point(double... coordinates) {
        ArrayList<Double> coordinatesValues = new ArrayList<>();
        for (double value : coordinates) {
            coordinatesValues.add(value);
        }
        this.coordinates = coordinatesValues;
    }

    private Point(ArrayList<Double> array) {
        coordinates = array;
    }

    public static Optional<Point> newPoint(ArrayList<Double> array) {
        for (Double number : array) {
            if (number == null) {
                return Optional.empty();
            }
        }
        return Optional.of(new Point(array));
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

    public String toString() {
        return coordinates.toString();
    }
}
