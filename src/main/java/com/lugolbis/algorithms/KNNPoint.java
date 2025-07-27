package com.lugolbis.algorithms;

import java.util.ArrayList;
import java.util.Optional;

import com.lugolbis.mathematics.Point;

public class KNNPoint {
    private String classe;
    private Point point;

    private KNNPoint(String classe, Point point) {
        this.classe = classe;
        this.point = point;
    }

    private KNNPoint(String classe, double... coordinates) {
        this.classe = classe;
        point = new Point(coordinates);
    }

    public static Optional<KNNPoint> newKNNPoint(String classe, double... coordinates) {
        if (classe != null) {
            return Optional.of(new KNNPoint(classe, coordinates));
        }
        else {
            return Optional.empty();
        }
    }

    public static Optional<KNNPoint> newKNNPoint(String classe, ArrayList<Double> coordinates) {
        if (classe != null) {
            Optional<Point> point = Point.newPoint(coordinates);
            return point.isPresent() ? Optional.of(new KNNPoint(classe, point.get())) : Optional.empty();
        }
        else {
            return Optional.empty();
        }
    }

    public static Optional<KNNPoint> newKNNPoint(String classe, Point point) {
        if (classe != null && point != null) {
            return Optional.of(new KNNPoint(classe, point));
        }
        else {
            return Optional.empty();
        }
    }

    public String getClasse() {
        return classe;
    }

    public Point getPoint() {
        return point;
    }

    public String toString() {
        return String.format("\nKNNPoint[\n%s\n%s\n]", classe, point);
    }
}
