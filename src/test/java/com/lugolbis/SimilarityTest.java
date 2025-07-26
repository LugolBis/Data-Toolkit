package com.lugolbis;

import com.lugolbis.mathematics.Similarity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;

class SimilarityTest {

    @Test
    void testJackard_IdenticalSets() {
        HashSet<Double> setA = new HashSet<>(List.of(1.0, 2.0, 3.0));
        HashSet<Double> setB = new HashSet<>(List.of(1.0, 2.0, 3.0));
        
        Optional<Double> result = Similarity.Jackard(setA, setB);
        assertTrue(result.isPresent());
        assertEquals(1.0, result.get(), 0.001);
    }

    @Test
    void testJackard_NoIntersection() {
        HashSet<Double> setA = new HashSet<>(List.of(1.0, 2.0));
        HashSet<Double> setB = new HashSet<>(List.of(3.0, 4.0));
        
        Optional<Double> result = Similarity.Jackard(setA, setB);
        assertTrue(result.isPresent());
        assertEquals(0.0, result.get(), 0.001);
    }

    @Test
    void testJackard_PartialIntersection() {
        HashSet<Double> setA = new HashSet<>(List.of(1.0, 2.0, 3.0));
        HashSet<Double> setB = new HashSet<>(List.of(2.0, 3.0, 4.0));
        
        Optional<Double> result = Similarity.Jackard(setA, setB);
        assertTrue(result.isPresent());
        assertEquals(2.0 / 4.0, result.get(), 0.001);
    }

    @Test
    void testJackard_EmptySets() {
        HashSet<Double> setA = new HashSet<>();
        HashSet<Double> setB = new HashSet<>();
        
        Optional<Double> result = Similarity.Jackard(setA, setB);
        assertTrue(result.isPresent());
        assertEquals(1.0, result.get(), 0.001);
    }

    @Test
    void testJackard_OneEmptySet() {
        HashSet<Double> setA = new HashSet<>(List.of(1.0, 2.0));
        HashSet<Double> setB = new HashSet<>();
        
        Optional<Double> result = Similarity.Jackard(setA, setB);
        assertTrue(result.isPresent());
        assertEquals(0.0, result.get(), 0.001);
    }

    // Tests pour la méthode minMax
    @Test
    void testMinMax_SameSize() {
        ArrayList<Double> A = new ArrayList<>(List.of(3.0, 0.0, 1.0));
        ArrayList<Double> B = new ArrayList<>(List.of(1.0, 2.0, 0.0));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertEquals(1.0/6.0, result.get(), 0.001);
    }

    @Test
    void testMinMax_DifferentSizes() {
        ArrayList<Double> A = new ArrayList<>(List.of(1.0, 2.0));
        ArrayList<Double> B = new ArrayList<>(List.of(1.0));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMinMax_AllZero() {
        ArrayList<Double> A = new ArrayList<>(List.of(0.0, 0.0, 0.0));
        ArrayList<Double> B = new ArrayList<>(List.of(0.0, 0.0, 0.0));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertEquals(1.0, result.get(), 0.001);
    }

    @Test
    void testMinMax_NegativeValues() {
        ArrayList<Double> A = new ArrayList<>(List.of(-2.0, 3.0));
        ArrayList<Double> B = new ArrayList<>(List.of(1.0, -4.0));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertEquals(-6.0/4.0, result.get(), 0.001);
    }

    @Test
    void testMinMax_OneZeroVector() {
        ArrayList<Double> A = new ArrayList<>(List.of(0.0, 0.0));
        ArrayList<Double> B = new ArrayList<>(List.of(1.0, 2.0));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertEquals(0.0, result.get(), 0.001);
    }

    @Test
    void testMinMax_EmptyVectors() {
        ArrayList<Double> A = new ArrayList<>();
        ArrayList<Double> B = new ArrayList<>();
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertEquals(1.0, result.get(), 0.001);
    }

    @Test
    void testMinMax_LargeVectors() {
        ArrayList<Double> A = new ArrayList<>();
        ArrayList<Double> B = new ArrayList<>();
        
        for (int i = 0; i < 1000; i++) {
            A.add(1.0);
            B.add(2.0);
        }
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertEquals(1000.0 / 2000.0, result.get(), 0.001);
    }

    @Test
    void testMinMax_InfinityValues() {
        ArrayList<Double> A = new ArrayList<>(List.of(Double.POSITIVE_INFINITY, 1.0));
        ArrayList<Double> B = new ArrayList<>(List.of(2.0, Double.POSITIVE_INFINITY));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        // min = [2.0, 1.0] = 3.0, max = [∞, ∞] = ∞
        System.out.println(result.get());
        assertTrue(Double.isNaN(result.get()));
    }

    @Test
    void testMinMax_NaNValues() {
        ArrayList<Double> A = new ArrayList<>(List.of(1.0, Double.NaN));
        ArrayList<Double> B = new ArrayList<>(List.of(2.0, 3.0));
        
        Optional<Double> result = Similarity.minMax(A, B);
        assertTrue(result.isPresent());
        assertTrue(Double.isNaN(result.get()));
    }
}
