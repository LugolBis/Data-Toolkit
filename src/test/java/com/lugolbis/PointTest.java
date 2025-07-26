package com.lugolbis;

import com.lugolbis.mathematics.Point;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PointTest {

    @Test
    @DisplayName("Test constructor with an Array")
    void testConstructorWithArray() {
        ArrayList<Double> coords = new ArrayList<>(List.of(1.0, 2.0, 3.0));

        Point point = Point.newPoint(coords).get();
        
        assertEquals(3, point.getDimensions());
        assertIterableEquals(coords, point.getCoordinates());
    }

    @Test
    @DisplayName("Test constructor 1D")
    void test1DConstructor() {
        Point point = new Point(5.5);

        assertEquals(1, point.getDimensions());
        assertEquals(5.5, point.getCoordinates().get(0));
    }

    @Test
    @DisplayName("Test constructor 2D")
    void test2DConstructor() {
        Point point = new Point(2.0, 3.0);

        assertEquals(2, point.getDimensions());
        assertEquals(2.0, point.getCoordinates().get(0));
        assertEquals(3.0, point.getCoordinates().get(1));
    }

    @Test
    @DisplayName("Test setCoordinates")
    void testSetCoordinates() {
        Point point = new Point(1.0);
        ArrayList<Double> newCoords = new ArrayList<>(List.of(4.0, 5.0, 6.0));

        point.setCoordinates(newCoords);
        
        assertEquals(3, point.getDimensions());
    }

    @Test
    @DisplayName("Test getDimensions with empty Point")
    void testEmptyPoint() {
        Point point = Point.newPoint(new ArrayList<>()).get();

        assertEquals(0, point.getDimensions());
        assertTrue(point.getCoordinates().isEmpty());
    }

    @Test
    @DisplayName("Test immutability")
    void testImmutability() {
        Point point = new Point(1.0, 2.0);

        List<Double> coords = point.getCoordinates();
        coords.add(3.0);

        assertEquals(2, point.getDimensions());
    }

    @Test
    @DisplayName("Test equality between coordinates")
    void testCoordinatesEquality() {
        Point point1 = new Point(1.0, 2.0);
        Point point2 = new Point(1.0, 2.0);
        Point point3 = new Point(1.0, 3.0);
        
        assertIterableEquals(point1.getCoordinates(), point2.getCoordinates());
        assertNotEquals(point1.getCoordinates(), point3.getCoordinates());
    }
}
