package com.lugolbis.mathematics;

import java.util.ArrayList;
import java.util.Optional;

public class Distance {
    public static Optional<Double> Minkowski(Point p1, Point p2, int k) {
        if (p1.getDimensions() == p2.getDimensions()) {
            double result = 0;

            ArrayList<Double> coordinates1 = p1.getCoordinates();
            ArrayList<Double> coordinates2 = p2.getCoordinates();

            for (int index=0; index < coordinates1.size(); index++) {
                result += Math.pow(coordinates1.get(index) - coordinates2.get(index), k);
            }

            double exposant = 1/(double)(k);
            return Optional.of(Math.abs(Math.pow(result, exposant)));
        }
        else {
            return Optional.empty();
        }
    }

    public static Optional<Double> Euclidian(Point p1, Point p2) {
        return Minkowski(p1, p2, 2);
    }

    public static Optional<Double> Manhattan(Point p1, Point p2) {
        return Minkowski(p1, p2, 1);
    }

    public static double Levenshtein(String a, String b) {
        int cardA = a.length();
        int cardB = b.length();

        if (Math.min(cardA, cardB) == 0) {
            return Math.max(cardA, cardB);
        }
        else if (a.charAt(0) == b.charAt(0)) {
            return Levenshtein(safeSubString(a, 1), safeSubString(b, 1));
        }
        else {
            double x = Levenshtein(safeSubString(a, 1), b);
            double y = Levenshtein(a, safeSubString(b, 1));
            double z = Levenshtein(safeSubString(a, 1), safeSubString(b, 1));

            return 1.0 + Math.min(x, Math.min(y, z));
        }
    }

    private static String safeSubString(String value, int index) {
        if (value.length() > index) {
            return value.substring(index);
        }
        else {
            return "";
        }
    }
}
