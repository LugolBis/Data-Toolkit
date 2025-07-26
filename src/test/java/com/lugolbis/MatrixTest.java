package com.lugolbis;

import com.lugolbis.mathematics.Matrix;
import com.lugolbis.mathematics.Vector;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    private Matrix createSampleMatrix2x2() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        data.add(new ArrayList<>(List.of(1.0, 2.0)));
        data.add(new ArrayList<>(List.of(3.0, 4.0)));
        return Matrix.createMatrix(data).get();
    }

    private Matrix createSampleMatrix2x3() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        data.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        data.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        return Matrix.createMatrix(data).get();
    }

    private Matrix createSampleMatrix3x2() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        data.add(new ArrayList<>(List.of(1.0, 2.0)));
        data.add(new ArrayList<>(List.of(3.0, 4.0)));
        data.add(new ArrayList<>(List.of(5.0, 6.0)));
        return Matrix.createMatrix(data).get();
    }

    @Test
    void testSum_Success() {
        Matrix matrixA = createSampleMatrix2x2();
        Matrix matrixB = createSampleMatrix2x2();

        Optional<Matrix> result = Matrix.sum(matrixA, matrixB);

        assertTrue(result.isPresent());
        Matrix sumMatrix = result.get();
        assertEquals(2, sumMatrix.getShape().rows);
        assertEquals(2, sumMatrix.getShape().columns);
        assertEquals(2.0, sumMatrix.getValue(0, 0).get());
        assertEquals(4.0, sumMatrix.getValue(0, 1).get());
        assertEquals(6.0, sumMatrix.getValue(1, 0).get());
        assertEquals(8.0, sumMatrix.getValue(1, 1).get());
    }

    @Test
    void testSum_Fail_DifferentDimensions() {
        Matrix matrixA = createSampleMatrix2x2();
        Matrix matrixB = createSampleMatrix2x3();

        Optional<Matrix> result = Matrix.sum(matrixA, matrixB);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSub_Success() {
        Matrix matrixA = createSampleMatrix2x2();
        Matrix matrixB = createSampleMatrix2x2();

        Optional<Matrix> result = Matrix.sub(matrixA, matrixB);

        assertTrue(result.isPresent());
        Matrix subMatrix = result.get();
        assertEquals(2, subMatrix.getShape().rows);
        assertEquals(2, subMatrix.getShape().columns);
        assertEquals(0.0, subMatrix.getValue(0, 0).get());
        assertEquals(0.0, subMatrix.getValue(0, 1).get());
        assertEquals(0.0, subMatrix.getValue(1, 0).get());
        assertEquals(0.0, subMatrix.getValue(1, 1).get());
    }

    @Test
    void testSub_Fail_DifferentDimensions() {
        Matrix matrixA = createSampleMatrix2x2();
        Matrix matrixB = createSampleMatrix2x3();

        Optional<Matrix> result = Matrix.sub(matrixA, matrixB);

        assertTrue(result.isEmpty());
    }

    @Test
    void testMultMatrixs_Success() {
        Matrix matrixA = createSampleMatrix2x3();
        Matrix matrixB = createSampleMatrix3x2();

        Optional<Matrix> result = Matrix.mult(matrixA, matrixB);

        assertTrue(result.isPresent());
        Matrix multMatrix = result.get();

        assertEquals(2, multMatrix.getShape().rows);
        assertEquals(2, multMatrix.getShape().columns);

        // Line 1: [1*1 + 2*3 + 3*5, 1*2 + 2*4 + 3*6] = [1+6+15, 2+8+18] = [22, 28]
        // Line 2: [4*1 + 5*3 + 6*5, 4*2 + 5*4 + 6*6] = [4+15+30, 8+20+36] = [49, 64]
        assertEquals(22.0, multMatrix.getValue(0, 0).get());
        assertEquals(28.0, multMatrix.getValue(0, 1).get());
        assertEquals(49.0, multMatrix.getValue(1, 0).get());
        assertEquals(64.0, multMatrix.getValue(1, 1).get());
    }

    @Test
    void testMultMatrixs_Fail_IncompatibleDimensions() {
        Matrix matrixA = createSampleMatrix2x2();
        Matrix matrixB = createSampleMatrix3x2();

        Optional<Matrix> result = Matrix.mult(matrixA, matrixB);
        assertTrue(result.isEmpty());
    }

    @Test
    void testScalarMult() {
        Matrix matrix = createSampleMatrix2x2();
        double k = 2.0;

        matrix.mult(k);

        assertEquals(2.0, matrix.getValue(0, 0).get());
        assertEquals(4.0, matrix.getValue(0, 1).get());
        assertEquals(6.0, matrix.getValue(1, 0).get());
        assertEquals(8.0, matrix.getValue(1, 1).get());
    }

    @Test
    void testLoadMatrix() {
        try {
            Path path = getAbsolutePath("matrix1.json");

            Matrix matrix = Matrix.loadFromJson(path.toString());

            assertEquals(matrix.getRows().get(0), 467.7829);;
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
    }

    @Test
    void testVectorMult() {
        Vector vector = Vector.newVector(new ArrayList<>(List.of(1.0, 8.0, 15.0)), Vector.Type.Column).get();
        Matrix matrix = Matrix.newMatrix(new ArrayList<>(List.of(1.0, 3.0, 5.0, 9.0, 10.0, 21.0)), 3).get();

        Optional<Vector> result = matrix.multVector(vector);
        assertTrue(result.isPresent());
        assertEquals(result.get().getValues(), new ArrayList<>(List.of(100.0, 404.0)));
    }

    public static Path getAbsolutePath(String resourceName) throws Exception {
        ClassLoader classLoader = MatrixTest.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);
        
        if (resourceUrl == null) {
            throw new IllegalArgumentException("File not found : " + resourceName);
        }
        
        return Paths.get(resourceUrl.toURI());
    }
}
