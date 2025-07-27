package com.lugolbis;

import com.lugolbis.algorithms.KNN;
import com.lugolbis.algorithms.KNNPoint;

import com.lugolbis.mathematics.Point;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class KNNTest {
    private final Point p1 = new Point(1.0, 1.0);
    private final Point p2 = new Point(1.0, 2.0);
    private final Point p3 = new Point(5.0, 5.0);
    private final Point p4 = new Point(5.0, 6.0);
    private final Point testPoint = new Point(1.0, 1.5);
    private final Point incompatiblePoint = new Point(1.0);

    private ArrayList<KNNPoint> createValidPoints() {
        ArrayList<KNNPoint> points = new ArrayList<>();
        points.add(KNNPoint.newKNNPoint("A", p1).get());
        points.add(KNNPoint.newKNNPoint("A", p2).get());
        points.add(KNNPoint.newKNNPoint("B", p3).get());
        points.add(KNNPoint.newKNNPoint("B", p4).get());
        return points;
    }

    @Test
    void validInput_shouldReturnKNNPoint() {
        ArrayList<KNNPoint> points = createValidPoints();
        Optional<KNNPoint> result = KNN.run(points, 3, testPoint);
        
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getClasse());
        assertEquals(testPoint, result.get().getPoint());
    }

    @Test
    void kEqualsOne_shouldReturnNearestNeighbor() {
        ArrayList<KNNPoint> points = createValidPoints();
        Optional<KNNPoint> result = KNN.run(points, 1, testPoint);
        
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getClasse());
    }

    @Test
    void kLargerThanPointsSize_shouldUseAllPoints() {
        ArrayList<KNNPoint> points = createValidPoints();
        Optional<KNNPoint> result = KNN.run(points, 10, testPoint);
        
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getClasse());
    }

    @Test
    void nullPoints_shouldReturnEmpty() {
        Optional<KNNPoint> result = KNN.run(null, 3, testPoint);
        assertTrue(result.isEmpty());
    }

    @Test
    void nullPoint_shouldReturnEmpty() {
        ArrayList<KNNPoint> points = createValidPoints();
        Optional<KNNPoint> result = KNN.run(points, 3, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void emptyPointsList_shouldReturnEmpty() {
        Optional<KNNPoint> result = KNN.run(new ArrayList<>(), 3, testPoint);
        assertTrue(result.isEmpty());
    }

    @Test
    void kEqualsZero_shouldReturnEmpty() {
        ArrayList<KNNPoint> points = createValidPoints();
        Optional<KNNPoint> result = KNN.run(points, 0, testPoint);
        assertTrue(result.isEmpty());
    }

    @Test
    void incompatibleDimensions_shouldIgnoreInvalidPoints() {
        ArrayList<KNNPoint> points = createValidPoints();
        points.add(KNNPoint.newKNNPoint("C", incompatiblePoint).get());
        
        Optional<KNNPoint> result = KNN.run(points, 3, testPoint);
        
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getClasse());
    }

    @Test
    void tieInVotes_shouldReturnFirstMajority() {
        ArrayList<KNNPoint> points = new ArrayList<>();
        points.add(KNNPoint.newKNNPoint("A", new Point(1.0, 1.0)).get());
        points.add(KNNPoint.newKNNPoint("B", new Point(2.0, 2.0)).get());
        Point testPoint = new Point(1.5, 1.5);
        
        Optional<KNNPoint> result = KNN.run(points, 2, testPoint);
        
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getClasse());
    }
}