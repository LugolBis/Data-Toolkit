package com.lugolbis.mathematics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;
import java.util.concurrent.*;

public class Matrice {
    private ArrayList<Double> rows;
    private int columns;

    public Matrice(ArrayList<Double> rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public class Shape {
        public int rows;
        public int columns;

        Shape(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
        }
    }

    private static class ParallelRows implements Callable<ArrayList<Double>> {
        private int shapeB;
        private int indexR;
        private Matrice matriceA;
        private Matrice matriceB;

        private ParallelRows(int shapeB, int indexR, Matrice matriceA, Matrice matriceB) {
            this.shapeB = shapeB;
            this.indexR = indexR;
            this.matriceA = matriceA;
            this.matriceB = matriceB;
        }

        @Override
        public ArrayList<Double> call() throws InterruptedException, ExecutionException {
            ArrayList<Double> array = new ArrayList<>(); 
            List<Double> rowA = matriceA.unsafeGetRow(indexR);

            int nbThreads = shapeB;
            ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
            List<Future<Double>> futures = new ArrayList<>();

            for (int indexC=0; indexC < shapeB; indexC++) {
                futures.add(executor.submit(new ParallelColumns(indexC, rowA, matriceB)));
            }

            for (Future<Double> future : futures) {
                array.add(future.get());
            }

            executor.shutdown();
            return array;
        }
    }

    private static class ParallelColumns implements Callable<Double> {
        private int indexC;
        private List<Double> rowA;
        private Matrice matriceB;

        private ParallelColumns(int indexC, List<Double> rowA, Matrice matriceB) {
            this.indexC = indexC;
            this.rowA = rowA;
            this.matriceB = matriceB;
        }

        @Override
        public Double call() {
            List<Double> columnB = matriceB.unsafeGetColumn(indexC);
            return multRowColumn(rowA, columnB);
        }
    }

    public ArrayList<Double> getRows() {
        return rows;
    }

    public Optional<List<Double>> getRow(int index) {
        int start = columns * index;
        int end = start + columns;

        if (end < rows.size()) {
            return Optional.of(rows.subList(start, end));
        }
        else {
            return Optional.empty();
        }
    }

    private List<Double> unsafeGetRow(int index) {
        int start = columns * index;
        int end = start + columns;
        return rows.subList(start, end);
    }

    public int getColumns() {
        return columns;
    }

    public Optional<List<Double>> getColumn(int index) {
        if (index >= columns) {
            return Optional.empty();
        }

        ArrayList<Double> array = new ArrayList<>();
        for (int indexValue=index; indexValue < rows.size(); indexValue += columns) {
            array.add(rows.get(indexValue));
        }

        return Optional.of(array);
    }

    private List<Double> unsafeGetColumn(int index) {
        ArrayList<Double> array = new ArrayList<>();
        for (int indexValue=index; indexValue < rows.size(); indexValue += columns) {
            array.add(rows.get(indexValue));
        }
        return array;
    }

    public Shape getShape() {
        return new Shape(rows.size()/columns, columns);
    }

    public Optional<Double> getValue(int row, int column) {
        int index = row*columns + column;
        if (index < rows.size()) {
            return Optional.of(rows.get(index));
        }
        else {
            return Optional.empty();
        }
    }

    public static Optional<Matrice> createMatrice(ArrayList<ArrayList<Double>> array) {
        if (array.size() <= 0) {
            return Optional.empty();
        }

        ArrayList<Double> rows = array.get(0);
        int columns = rows.size();

        for (int index=1; index < array.size(); index++) {
            ArrayList<Double> row = array.get(index);
            if (row.size() == columns) {
                rows.addAll(row);
            }
            else {
                return Optional.empty();
            }
        }

        return Optional.of(new Matrice(rows, columns));
    }

    public static Optional<Matrice> sum(Matrice matriceA, Matrice matriceB) {
        return simpleCompute(matriceA, matriceB, ((x, y) -> x + y));
    }

    public static Optional<Matrice> sub(Matrice matriceA, Matrice matriceB) {
        return simpleCompute(matriceA, matriceB, ((x, y) -> x - y));
    }

    private static Optional<Matrice> simpleCompute(Matrice matriceA, Matrice matriceB, DoubleBinaryOperator op) {
        Shape shapeA = matriceA.getShape();
        Shape shapeB = matriceB.getShape();
        
        if (shapeA.rows == shapeB.rows && shapeA.columns == shapeB.columns) {
            ArrayList<Double> rowsA = matriceA.rows;
            ArrayList<Double> rowsB = matriceB.rows;
            int size = rowsA.size();

            ArrayList<Double> rows = new ArrayList<>();
            for (int index=0; index < size; index++) {
                rows.add(op.applyAsDouble(rowsA.get(index), rowsB.get(index)));
            }

            return Optional.of(new Matrice(rows, matriceA.columns));
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Multiply all the values by the input value 'k'
     */
    public void mult(double k) {
        for (int index=0; index < rows.size(); index++) {
            rows.set(index, rows.get(index)*k);
        }
    }

    public static Optional<Matrice> mult(Matrice matriceA, Matrice matriceB) throws InterruptedException, ExecutionException {
        Shape shapeA = matriceA.getShape();
        Shape shapeB = matriceB.getShape();

        if (shapeA.columns != shapeB.rows) {
            return Optional.empty();
        }

        return multParallel(matriceA, matriceB, shapeA, shapeB);
    }

    private static Optional<Matrice> multParallel(Matrice matriceA, Matrice matriceB, Shape shapeA, Shape shapeB) throws InterruptedException, ExecutionException {
        ArrayList<Double> array = new ArrayList<>();

        int nbThreads = shapeA.rows;
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
        List<Future<ArrayList<Double>>> futures = new ArrayList<>();
        
        for (int indexR=0; indexR < shapeA.rows; indexR++) {
            futures.add(executor.submit(new ParallelRows(shapeB.columns, indexR, matriceA, matriceB)));
        }

        for (Future<ArrayList<Double>> future : futures) {
            array.addAll(future.get());
        }

        executor.shutdown();
        return Optional.of(new Matrice(array, shapeB.columns));
    }

    public String toString() {
        Shape shape = getShape();
        String result = new String();

        for (int index=0; index < shape.rows; index++) {
            int start = index*shape.columns;
            int end = start+columns;
            List<Double> array = rows.subList(start, end);
            result += array.toString();
            result += "\n";
        }
        return result;
    }

    private static double multRowColumn(List<Double> row, List<Double> column) {
        double value = 0;
        for (int index=0; index < row.size(); index++) {
            value += row.get(index) * column.get(index);
        }
        return value;
    }
}
