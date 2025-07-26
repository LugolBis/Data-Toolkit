package com.lugolbis.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.lugolbis.mathematics.Distance;
import com.lugolbis.mathematics.Point;

public class CAH {
    public static class Group {
        private Group gpA;
        private Group gpB;
        private final int stage;
        private Point point;

        public Group(Point point) {
            this.point = point;
            stage = 0;
        }

        public Group(Group groupA, Group groupB) {
            gpA = groupA;
            gpB = groupB;
            stage = Math.max(gpA.stage + 1, gpB.stage + 1);
        }

        public int getStage() {
            return stage;
        }

        public List<Point> getPoints() {
            if (point == null) {
                List<Point> list = new ArrayList<>();
                list.addAll(gpA.getPoints());
                list.addAll(gpB.getPoints());
                return list;
            }
            else {
                return List.of(point);
            }
        }

        public Optional<Group> getGroupA() {
            return gpA != null ? Optional.of(gpA) : Optional.empty();
        }

        public Optional<Group> getGroupB() {
            return gpB != null ? Optional.of(gpB) : Optional.empty();
        }

        public Optional<Point> getPoint() {
            if (point != null) {
                return Optional.of(point);
            }
            else {
                return Optional.empty();
            }
        }

        public String toString() {
            return display(0);
        }

        private String display(int level) {
            String result = "\n" + "| ".repeat(level) + "Stage : " + stage;
            if (point == null) {
                result += "\n" + "| ".repeat(level);
                result += (gpA != null) ? "Group A -> " + gpA.display(level + 1) : "";
                result += "\n" + "| ".repeat(level);
                result += (gpB != null) ? "Group B -> " + gpB.display(level + 1) : "";
            }
            else {
                result += "\n" + "| ".repeat(level) + "--[ " + point;
            }
            
            return result;
        }
    }

    public static enum Strategy {
        CompleteLinkage,
        SingleLinkage,
        AverageLinkage
    }

    private static class Dissimilarity {
        private double distance;
        private int indexA;
        private int indexB;

        private Dissimilarity(double distance, int indexA, int indexB) {
            this.distance = distance;
            this.indexA = indexA;
            this.indexB = indexB;
        }

        private static Dissimilarity compute(Group grpA, Group grpB, Strategy strategy, int indexA, int indexB) {
            List<Double> distances = new ArrayList<>();
            List<Point> pointsA = grpA.getPoints();
            List<Point> pointsB = grpB.getPoints();

            for (Point pointA : pointsA) {
                for (Point pointB : pointsB) {
                    distances.add(Distance.Euclidian(pointA, pointB).orElse(Double.POSITIVE_INFINITY));
                }
            }

            double value;
            if (strategy == Strategy.CompleteLinkage) {
                value = Collections.max(distances);
            }
            else if (strategy == Strategy.SingleLinkage) {
                value = Collections.min(distances);
            }
            else {
                double sum = 0.0;
                for (double distance : distances) {
                    sum += distance;
                }
                value = sum / (double)(distances.size());
            }
            return new Dissimilarity(value, indexA, indexB);
        }
    }

    public static Optional<Group> run(List<Point> points, Strategy strategy) {
        ArrayList<Group> groups = new ArrayList<>();

        if(points.size() < 1) {
            return Optional.empty();
        }
        int dimensions = points.get(0).getDimensions();

        for (Point point : points) {
            if (point.getDimensions() == dimensions) {
                groups.add(new Group(point));
            }
            else {
                return Optional.empty();
            }
        }

        while (groups.size() > 1) {
            Dissimilarity dissimilarity = dissimilarity(groups, strategy);

            Group groupA = groups.remove(dissimilarity.indexA);
            Group groupB = groups.remove(dissimilarity.indexB);

            groups.add(new Group(groupA, groupB));
        }
        return Optional.of(groups.get(0));
    }

    private static Dissimilarity dissimilarity(List<Group> groups, Strategy strategy) {
        Dissimilarity dissimilarity = new Dissimilarity(Double.POSITIVE_INFINITY, 0, 1);

        for (int indexRow=0; indexRow < groups.size(); indexRow++) {
            Group groupA = groups.get(indexRow);

            for (int indexColumn=indexRow+1; indexColumn < groups.size(); indexColumn++) {
                Group groupB = groups.get(indexColumn);

                Dissimilarity new_dissimilarity = Dissimilarity.compute(groupA, groupB, strategy, indexColumn, indexRow);
                if (dissimilarity.distance > new_dissimilarity.distance) {
                    dissimilarity = new_dissimilarity;
                }
            }
        }
        return dissimilarity;
    }
}
