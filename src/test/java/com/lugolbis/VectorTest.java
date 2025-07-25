package com.lugolbis;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import com.lugolbis.mathematics.Vector;
import com.lugolbis.mathematics.Matrix;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    public static Vector getVectorColumn(List<Double> list) {
        return new Vector(new ArrayList<>(list), Vector.Type.Column);
    }

    public static Vector getVectorRow(List<Double> list) {
        return new Vector(new ArrayList<>(list), Vector.Type.Row);
    }

    @Test
    void testMultColumnRow() {
        Vector vectorA = getVectorColumn(List.of(2.0, 3.0, 5.0));
        Vector vectorB = getVectorRow(List.of(1.0, 0.5, 4.0));

        Optional<Matrix> matrix = vectorA.multColumnRow(vectorB);
        Matrix matrix_target = new Matrix(new ArrayList<>(List.of(2.0,3.0,5.0,1.0,1.5,2.5,8.0,12.0,20.0)), 3);

        assertTrue(matrix.isPresent()); 
        assertEquals(matrix.get().getRows(), matrix_target.getRows());
        assertEquals(matrix.get().getColumns(), matrix_target.getColumns());
    }

    @Test
    void testMultRowColumn() {
        Vector vectorA = getVectorColumn(List.of(2.0, 3.0, 5.0));
        Vector vectorB = getVectorRow(List.of(1.0, 0.5, 4.0));

        Optional<Double> opt = vectorB.multRowColumn(vectorA);

        assertTrue(opt.isPresent()); 
        assertEquals(opt.get(), 23.5);
    }

    @Test
    void testMultMatrix() {
        Vector vector = getVectorRow(List.of(2.0, 3.0, 5.0));
        Matrix matrix = new Matrix(new ArrayList<>(List.of(1.0, 2.0, 4.0, 5.0, 7.0, 5.0)), 2);

        Optional<Vector> vectorC = vector.multMatrix(matrix);

        assertTrue(vectorC.isPresent()); 
        assertEquals(vectorC.get().getValues(), new ArrayList<>(List.of(49.0, 44.0)));
        assertEquals(vectorC.get().getType(), Vector.Type.Row);
    }

    @Test
    void testMultHamrad() {
        Vector vectorA = getVectorRow(List.of(1.0, 0.5, 4.0));
        Vector vectorB = getVectorRow(List.of(1.0, 5.0, 4.0));

        Optional<Vector> vectorC = vectorA.multHadamard(vectorB);

        assertTrue(vectorC.isPresent());
        assertEquals(vectorC.get().getValues(), new ArrayList<>(List.of(1.0, 2.5, 16.0)));
    }
}
