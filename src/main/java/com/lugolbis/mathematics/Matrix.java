package com.lugolbis.mathematics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Matrix {
    private ArrayList<Double> rows;
    private int columns;

    private Matrix(ArrayList<Double> rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public static Optional<Matrix> newMatrix(ArrayList<Double> rows, int columns) {
        for (Double number : rows) {
            if (number == null) {
                return Optional.empty();
            }
        }
        return Optional.of(new Matrix(rows, columns));
    }

    public class Shape {
        public int rows;
        public int columns;

        Shape(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
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

    public List<Double> unsafeGetRow(int index) {
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

    public List<Double> unsafeGetColumn(int index) {
        ArrayList<Double> array = new ArrayList<>();
        for (int indexValue=index; indexValue < rows.size(); indexValue += columns) {
            array.add(rows.get(indexValue));
        }
        return array;
    }

    public Shape getShape() {
        return new Shape(rows.size() / columns, columns);
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

    public static Optional<Matrix> createMatrix(ArrayList<ArrayList<Double>> array) {
        if (array.size() <= 0) {
            return Optional.empty();
        }

        ArrayList<Double> rows = array.get(0);
        int columns = rows.size();

        for (int index=1; index < array.size(); index++) {
            ArrayList<Double> row = array.get(index);
            if (row != null && row.size() == columns) {
                rows.addAll(row);
            }
            else {
                return Optional.empty();
            }
        }

        return Optional.of(new Matrix(rows, columns));
    }

    public static Optional<Matrix> sum(Matrix matriceA, Matrix matriceB) {
        return simpleCompute(matriceA, matriceB, ((x, y) -> x + y));
    }

    public static Optional<Matrix> sub(Matrix matriceA, Matrix matriceB) {
        return simpleCompute(matriceA, matriceB, ((x, y) -> x - y));
    }

    private static Optional<Matrix> simpleCompute(Matrix matriceA, Matrix matriceB, DoubleBinaryOperator op) {
        if (matriceA == null || matriceB == null) {
            return Optional.empty();
        }
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

            return Optional.of(new Matrix(rows, matriceA.columns));
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

    public static Optional<Matrix> mult(Matrix matriceA, Matrix matriceB) {
        if (matriceA == null || matriceB == null) {
            return Optional.empty();
        }
        Shape shapeA = matriceA.getShape();
        Shape shapeB = matriceB.getShape();

        if (shapeA.columns != shapeB.rows) {
            return Optional.empty();
        }

        ArrayList<Double> array = new ArrayList<>();
        for (int indexR=0; indexR < shapeA.rows; indexR++) {
            List<Double> rowA = matriceA.unsafeGetRow(indexR); 

            for (int indexC=0; indexC < shapeB.columns; indexC++) {
                List<Double> columnB = matriceB.unsafeGetColumn(indexC);
                double value = 0;

                for (int index=0; index < rowA.size(); index++) {
                    value += rowA.get(index) * columnB.get(index);
                }
                array.add(value);
            }
        }

        return Optional.of(new Matrix(array, shapeB.columns));
    }

    public Optional<Vector> multVector(Vector vector) {
        if (vector == null) {
            return Optional.empty();
        }
        int vector_size = vector.getSize();

        if (
            vector.getType() == Vector.Type.Column
            && columns == vector_size
        ) {
            ArrayList<Double> result = new ArrayList<>();
            ArrayList<Double> values = vector.getValues();

            for (int indexR=0; indexR < rows.size()/columns; indexR++) {
                List<Double> row = unsafeGetRow(indexR);

                double value = 0;
                for (int index=0; index < vector_size; index++) {
                    value += row.get(index) * values.get(index);
                }
                result.add(value);
            }
            return Optional.of(Vector.newVector(result, Vector.Type.Column).get());
        }
        else {
            return Optional.empty();
        }
    }

    public void transposate() {
        Shape shape = getShape();
        ArrayList<Double> array = new ArrayList<>();
        
        ArrayList<ArrayList<Double>> arrays = new ArrayList<>();
        int index = 0;
        while (index < shape.rows) {
            arrays.add(new ArrayList<>(rows.subList(index, index + shape.columns)));
            index += shape.columns + 1;
        }

        for (int indexC=0; indexC < shape.columns; indexC++) {
            for (int indexR=0; indexR < shape.rows; indexR++) {
                array.add(arrays.get(indexR).removeFirst());
            }
        }

        rows = array;
        columns = shape.rows;
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

    public static Matrix loadFromJson(String filePath) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        JsonObject obj = new Gson().fromJson(json, JsonObject.class);
        
        JsonArray dataArray = obj.get("rows").getAsJsonArray();
        int dataColumns = obj.get("columns").getAsInt();

        ArrayList<Double> rows = new ArrayList<>();
        for (JsonElement element : dataArray) {
            rows.add(element.getAsDouble());
        }

        return new Matrix(rows, dataColumns);
    }
}
