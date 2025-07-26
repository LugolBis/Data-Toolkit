package com.lugolbis.mathematics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class Similarity {
    public static Optional<Double> Jackard(HashSet<Double> setA, HashSet<Double> setB) {
        if (setA == null || setB == null) {
            return Optional.empty();
        }

        double intersection = 0.0;

        for (double valueA : setA) {
            if (setB.contains(valueA)) {
                intersection++;
            }
        }

        double union = (double)(setB.size()) + (double)(setA.size()) - intersection;

        if (union > 0.0) {
            return Optional.of(intersection / union);
        }
        else {
            return Optional.of(1.0);
        }
    }

    public static Optional<Double> minMax(ArrayList<Double> A, ArrayList<Double> B) {
        if (A != null && B != null && A.size() == B.size()) {
            double sum_min = 0;
            double sum_max = 0;

            for (int index=0; index < A.size(); index++) {
                double valueA = A.get(index);
                double valueB = B.get(index);

                if (Double.isInfinite(valueA) || Double.isInfinite(valueB)) {
                    return Optional.of(Double.NaN);
                }

                if (valueA > valueB) {
                    sum_max += valueA;
                    sum_min += valueB;
                }
                else {
                    sum_max += valueB;
                    sum_min += valueA;
                }
            }
            if (sum_max > 0) {
                return Optional.of(sum_min / sum_max);
            }
            else {
                return Optional.of(1.0);
            }
        }
        else {
            return Optional.empty();
        }
    }
}
