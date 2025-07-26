package com.lugolbis;

import com.lugolbis.algorithms.CAH;

import com.lugolbis.mathematics.Point;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CAHTest {

    private final Point p1 = new Point(0.0, 0.0); // (0,0)
    private final Point p2 = new Point(1.0, 0.0); // (1,0)
    private final Point p3 = new Point(5.0, 0.0); // (5,0)
    private final Point p4 = new Point(10.0, 0.0); // (10,0)

    @Test
    void testCompleteLinkage() {
        List<Point> points = List.of(p1, p2, p3, p4);
        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.CompleteLinkage);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getStage());
        assertEquals(4, result.get().getPoints().size());
        assertTrue(result.get().getPoint().isEmpty());
    }

    @Test
    void testSingleLinkage() {
        List<Point> points = List.of(p1, p2, p3, p4);
        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.SingleLinkage);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getStage());
        assertEquals(4, result.get().getPoints().size());
    }

    @Test
    void testAverageLinkage() {
        List<Point> points = List.of(p1, p2, p3, p4);
        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.AverageLinkage);
        
        assertTrue(result.isPresent());
        assertEquals(3, result.get().getStage());
        assertEquals(4, result.get().getPoints().size());
    }

    @Test
    void testWithTwoPoints() {
        List<Point> points = List.of(p1, p2);
        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.CompleteLinkage);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getStage());
        assertEquals(2, result.get().getPoints().size());
        
        assertFalse(result.get().getPoint().isPresent());
        assertNotNull(result.get().getGroupA());
        assertNotNull(result.get().getGroupB());
    }

    @Test
    void testWithOnePoint() {
        List<Point> points = List.of(p1);
        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.CompleteLinkage);
        
        assertTrue(result.isPresent());
        assertEquals(0, result.get().getStage());
        assertEquals(1, result.get().getPoints().size());
        assertEquals(p1, result.get().getPoints().get(0));
        assertTrue(result.get().getPoint().isPresent());
    }

    @Test
    void testWithEmptyList() {
        List<Point> points = List.of();
        assertEquals(Optional.empty(), CAH.run(points, CAH.Strategy.CompleteLinkage));
    }

    @Test
    void testDifferentDimensions() {
        Point p1 = new Point(1.0, 2.0);
        Point p2 = new Point(3.0);
        
        List<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);

        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.SingleLinkage);
        
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testClusterOrder() {
        Point close1 = new Point(0.0, 0.0);
        Point close2 = new Point(0.1, 0.1);
        Point far = new Point(100.0, 100.0);
        
        List<Point> points = List.of(close1, close2, far);
        
        Optional<CAH.Group> result = CAH.run(points, CAH.Strategy.SingleLinkage);
        
        CAH.Group root = result.get();
        CAH.Group firstCluster = root.getGroupA().get() instanceof CAH.Group ? root.getGroupA().get() : root.getGroupB().get();
        
        assertNotNull(firstCluster);
        assertEquals(2, firstCluster.getPoints().size());
        assertTrue(firstCluster.getPoints().contains(close1));
        assertTrue(firstCluster.getPoints().contains(close2));
    }
}
