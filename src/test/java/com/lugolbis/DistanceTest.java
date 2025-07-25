package com.lugolbis;

import com.lugolbis.mathematics.Distance;
import com.lugolbis.mathematics.Point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class DistanceTest {

    private final Point point2D_1 = new Point(1.0, 2.0);
    private final Point point2D_2 = new Point(4.0, 6.0);
    private final Point point3D_1 = new Point(1.0, 2.0, 3.0);
    private final Point point3D_2 = new Point(4.0, 6.0, 8.0);
    private final Point pointDiffDim = new Point(1.0, 2.0);

    @Test
    void testManhattan_SameDimension() {
        Optional<Double> result = Distance.Manhattan(point2D_1, point2D_2);
        assertTrue(result.isPresent());
        assertEquals(7.0, result.get(), 0.001);

        result = Distance.Manhattan(point3D_1, point3D_2);
        assertTrue(result.isPresent());
        assertEquals(12.0, result.get(), 0.001);
    }

    @Test
    void testManhattan_DifferentDimensions() {
        Optional<Double> result = Distance.Manhattan(point2D_1, point3D_1);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEuclidian_SameDimension() {
        Optional<Double> result = Distance.Euclidian(point2D_1, point2D_2);
        assertTrue(result.isPresent());
        assertEquals(5.0, result.get(), 0.001);

        result = Distance.Euclidian(point3D_1, point3D_2);
        assertTrue(result.isPresent());
        assertEquals(Math.sqrt(50), result.get(), 0.001);
    }

    @Test
    void testEuclidian_DifferentDimensions() {
        Optional<Double> result = Distance.Euclidian(point3D_1, pointDiffDim);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLevenshtein() {
        assertEquals(0.0, Distance.Levenshtein("", ""));
        assertEquals(3.0, Distance.Levenshtein("", "abc"));
        assertEquals(3.0, Distance.Levenshtein("abc", ""));
        
        assertEquals(0.0, Distance.Levenshtein("kitten", "kitten"));
        
        assertEquals(1.0, Distance.Levenshtein("chat", "chats"));
        assertEquals(1.0, Distance.Levenshtein("chat", "cha"));
        assertEquals(1.0, Distance.Levenshtein("chat", "chit"));
        
        assertEquals(3.0, Distance.Levenshtein("kitten", "sitting"));
        assertEquals(2.0, Distance.Levenshtein("book", "back"));
        assertEquals(3.0, Distance.Levenshtein("saturday", "sunday"));
        
        assertEquals(1.0, Distance.Levenshtein("café", "cafe"));
    }
}