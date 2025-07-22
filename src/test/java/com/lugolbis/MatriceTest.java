package com.lugolbis;

import com.lugolbis.mathematics.Matrice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatriceTest {

    // Crée une matrice 2x2 pour les tests
    private Matrice createSampleMatrice2x2() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        data.add(new ArrayList<>(List.of(1.0, 2.0)));
        data.add(new ArrayList<>(List.of(3.0, 4.0)));
        return Matrice.createMatrice(data).get();
    }

    // Crée une matrice 2x3 pour les tests
    private Matrice createSampleMatrice2x3() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        data.add(new ArrayList<>(List.of(1.0, 2.0, 3.0)));
        data.add(new ArrayList<>(List.of(4.0, 5.0, 6.0)));
        return Matrice.createMatrice(data).get();
    }

    // Crée une matrice 3x2 pour les tests
    private Matrice createSampleMatrice3x2() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        data.add(new ArrayList<>(List.of(1.0, 2.0)));
        data.add(new ArrayList<>(List.of(3.0, 4.0)));
        data.add(new ArrayList<>(List.of(5.0, 6.0)));
        return Matrice.createMatrice(data).get();
    }

    @Test
    void testSum_Success() {
        Matrice matriceA = createSampleMatrice2x2();
        Matrice matriceB = createSampleMatrice2x2();

        Optional<Matrice> result = Matrice.sum(matriceA, matriceB);

        assertTrue(result.isPresent());
        Matrice sumMatrice = result.get();
        assertEquals(2, sumMatrice.getShape().rows);
        assertEquals(2, sumMatrice.getShape().columns);
        assertEquals(2.0, sumMatrice.getValue(0, 0).get());
        assertEquals(4.0, sumMatrice.getValue(0, 1).get());
        assertEquals(6.0, sumMatrice.getValue(1, 0).get());
        assertEquals(8.0, sumMatrice.getValue(1, 1).get());
    }

    @Test
    void testSum_Fail_DifferentDimensions() {
        Matrice matriceA = createSampleMatrice2x2();
        Matrice matriceB = createSampleMatrice2x3();

        Optional<Matrice> result = Matrice.sum(matriceA, matriceB);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSub_Success() {
        Matrice matriceA = createSampleMatrice2x2();
        Matrice matriceB = createSampleMatrice2x2();

        Optional<Matrice> result = Matrice.sub(matriceA, matriceB);

        assertTrue(result.isPresent());
        Matrice subMatrice = result.get();
        assertEquals(2, subMatrice.getShape().rows);
        assertEquals(2, subMatrice.getShape().columns);
        assertEquals(0.0, subMatrice.getValue(0, 0).get());
        assertEquals(0.0, subMatrice.getValue(0, 1).get());
        assertEquals(0.0, subMatrice.getValue(1, 0).get());
        assertEquals(0.0, subMatrice.getValue(1, 1).get());
    }

    @Test
    void testSub_Fail_DifferentDimensions() {
        Matrice matriceA = createSampleMatrice2x2();
        Matrice matriceB = createSampleMatrice2x3();

        Optional<Matrice> result = Matrice.sub(matriceA, matriceB);

        assertTrue(result.isEmpty());
    }

    @Test
    void testMultMatrices_Success() {
        Matrice matriceA = createSampleMatrice2x3();
        Matrice matriceB = createSampleMatrice3x2();

        Optional<Matrice> result = Matrice.mult(matriceA, matriceB);

        assertTrue(result.isPresent());
        Matrice multMatrice = result.get();
        assertEquals(2, multMatrice.getShape().rows);
        assertEquals(2, multMatrice.getShape().columns);

        // Line 1: [1*1 + 2*3 + 3*5, 1*2 + 2*4 + 3*6] = [1+6+15, 2+8+18] = [22, 28]
        // Line 2: [4*1 + 5*3 + 6*5, 4*2 + 5*4 + 6*6] = [4+15+30, 8+20+36] = [49, 64]
        assertEquals(22.0, multMatrice.getValue(0, 0).get());
        assertEquals(28.0, multMatrice.getValue(0, 1).get());
        assertEquals(49.0, multMatrice.getValue(1, 0).get());
        assertEquals(64.0, multMatrice.getValue(1, 1).get());
    }

    @Test
    void testMultMatrices_Fail_IncompatibleDimensions() {
        Matrice matriceA = createSampleMatrice2x2();
        Matrice matriceB = createSampleMatrice3x2();

        Optional<Matrice> result = Matrice.mult(matriceA, matriceB);

        System.out.println(matriceA);
        System.out.println(matriceB);
        assertTrue(result.isEmpty());
    }

    @Test
    void testScalarMult() {
        Matrice matrice = createSampleMatrice2x2();
        double k = 2.0;

        matrice.mult(k);

        assertEquals(2.0, matrice.getValue(0, 0).get());
        assertEquals(4.0, matrice.getValue(0, 1).get());
        assertEquals(6.0, matrice.getValue(1, 0).get());
        assertEquals(8.0, matrice.getValue(1, 1).get());
    }
}
