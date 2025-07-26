package com.lugolbis;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import com.lugolbis.mathematics.Vector;
import com.lugolbis.mathematics.Matrix;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    public static Optional<Vector> getVectorColumn(List<Double> list) {
        return Vector.newVector(new ArrayList<>(list), Vector.Type.Column);
    }

    public static Optional<Vector> getVectorRow(List<Double> list) {
        return Vector.newVector(new ArrayList<>(list), Vector.Type.Row);
    }

    @Test
    void testMultColumnRow() {
        Optional<Vector> vectorA = getVectorColumn(List.of(2.0, 3.0, 5.0));
        Optional<Vector> vectorB = getVectorRow(List.of(1.0, 0.5, 4.0));

        assertTrue(vectorA.isPresent());
        assertTrue(vectorB.isPresent());

        Optional<Matrix> matrix = vectorA.get().multColumnRow(vectorB.get());
        Optional<Matrix> matrix_target = Matrix.newMatrix(new ArrayList<>(List.of(2.0,3.0,5.0,1.0,1.5,2.5,8.0,12.0,20.0)), 3);

        assertTrue(matrix.isPresent()); 
        assertTrue(matrix_target.isPresent()); 
        assertEquals(matrix.get().getRows(), matrix_target.get().getRows());
        assertEquals(matrix.get().getColumns(), matrix_target.get().getColumns());
    }

    @Test
    void testMultRowColumn() {
        Optional<Vector> vectorA = getVectorColumn(List.of(2.0, 3.0, 5.0));
        Optional<Vector> vectorB = getVectorRow(List.of(1.0, 0.5, 4.0));

        assertTrue(vectorA.isPresent());
        assertTrue(vectorB.isPresent());

        Optional<Double> result = vectorB.get().multRowColumn(vectorA.get());

        assertTrue(result.isPresent()); 
        assertEquals(result.get(), 23.5);
    }

    @Test
    void testMultMatrix() {
        Optional<Vector> vector = getVectorRow(List.of(2.0, 3.0, 5.0));
        Optional<Matrix> matrix = Matrix.newMatrix(new ArrayList<>(List.of(1.0, 2.0, 4.0, 5.0, 7.0, 5.0)), 2);

        assertTrue(vector.isPresent());
        assertTrue(matrix.isPresent());

        Optional<Vector> vectorC = vector.get().multMatrix(matrix.get());

        assertTrue(vectorC.isPresent()); 
        assertEquals(vectorC.get().getValues(), new ArrayList<>(List.of(49.0, 44.0)));
        assertEquals(vectorC.get().getType(), Vector.Type.Row);
    }

    @Test
    void testMultHamrad() {
        Optional<Vector> vectorA = getVectorRow(List.of(1.0, 0.5, 4.0));
        Optional<Vector> vectorB = getVectorRow(List.of(1.0, 5.0, 4.0));

        assertTrue(vectorA.isPresent());
        assertTrue(vectorB.isPresent());

        Optional<Vector> vectorC = vectorA.get().multHadamard(vectorB.get());

        assertTrue(vectorC.isPresent());
        assertEquals(vectorC.get().getValues(), new ArrayList<>(List.of(1.0, 2.5, 16.0)));
    }
}
