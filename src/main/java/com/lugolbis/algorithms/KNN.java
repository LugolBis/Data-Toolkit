package com.lugolbis.algorithms;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lugolbis.mathematics.Point;
import com.lugolbis.mathematics.Distance;

public class KNN {
    private class KNNDistance {
        private String classe;
        private double distance;

        private KNNDistance(String classe, double distance) {
            this.classe = classe;
            this.distance = distance;
        }

        private double getDistance() {
            return distance;
        }

        public String toString() {
            return String.format("KNNDistance [%s - %s]", classe, distance);
        }
    }

    public static Optional<KNNPoint> run(ArrayList<KNNPoint> points, int k, Point point) {
        KNN knn = new KNN();
        if (points == null || point == null || k < 1) {
            return Optional.empty();
        }

        ArrayList<KNNDistance> distances = new ArrayList<>();

        for (KNNPoint knnPoint : points) {
            Optional<Double> distance = Distance.Euclidian(knnPoint.getPoint(), point);
            if (distance.isPresent()) {
                distances.add(knn.new KNNDistance(knnPoint.getClasse(), distance.get()));
            }
            else {
                System.err.println("\nInvalid knnPoint in KNN.run()");
            }
        }

        distances.sort(Comparator.comparingDouble(KNNDistance::getDistance));

        if (distances.size() > 0) {
            return Optional.of(KNNPoint.newKNNPoint(majorityClasse(distances, k), point).get());
        }
        else {
            return Optional.empty();
        }
    }

    private static String majorityClasse(ArrayList<KNNDistance> distances, int k) {
        HashMap<String, Double> map = new HashMap<>();

        for (int index=0; index < distances.size() && index < k; index++) {
            KNNDistance distance = distances.get(index);
            double counter = map.getOrDefault(distance.classe, 0.0) + 1.0;
            map.put(distance.classe, counter);
        }

        List<Map.Entry<String, Double>> entries = new ArrayList<>(map.entrySet());
        entries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        return entries.get(0).getKey();
    }
}
