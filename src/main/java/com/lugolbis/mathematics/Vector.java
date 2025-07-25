package com.lugolbis.mathematics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Vector {
    public enum Type {
        Row,
        Column
    }

    private Type type;
    private ArrayList<Double> values;

    public Vector(ArrayList<Double> values, Type type) {
        this.values = values;
        this.type = type;
    }

    public Optional<Double> getValue(int index) {
        if (index < values.size()) {
            return Optional.of(values.get(index));
        }
        else {
            return Optional.empty();
        }
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public int getSize() {
        return values.size();
    }

    public Type getType() {
        return type;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

    /**
     * Apply the transposate operation to the vector
     */
    public void transposate() {
        if (type == Type.Column) {
            type = Type.Row;
        }
        else {
            type = Type.Column;
        }
    }

    public Optional<Double> multScalar(Vector vector) {
        if (values.size() == vector.values.size()) {
            double result = 0;

            for(int index=0; index < values.size(); index++) {
                result += values.get(index) * vector.values.get(index);
            }
            return Optional.of(result);
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<Vector> multHadamard(Vector vector) {
        if (values.size() == vector.values.size()) {
            ArrayList<Double> array = new ArrayList<>();

            for (int index=0; index < values.size(); index++) {
                array.add(values.get(index) * vector.values.get(index));
            }
            return Optional.of(new Vector(array, Type.Row));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<Double> multRowColumn(Vector vector) {
        if (
            type == Type.Row
            && vector.type == Type.Column
            && values.size() == vector.values.size()
        ) {
            ArrayList<Double> other_values = vector.values;
            double result = 0;
            for (int index=0; index < values.size(); index++) {
                result += values.get(index) * other_values.get(index);
            }
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public Optional<Matrix> multColumnRow(Vector vector) {
        if (
            type == Type.Column
            && vector.type == Type.Row
            && values.size() == vector.values.size()
        ) {
            ArrayList<Double> other_values = vector.values;
            ArrayList<Double> result = new ArrayList<>();

            for (int index=0; index < values.size(); index++) {
                double multiplicator = other_values.get(index);

                for (double value : values) {
                    result.add(value * multiplicator);
                }
            }
            return Optional.of(new Matrix(result, values.size()));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<Vector> multMatrix(Matrix matrix) {
        if (
            type == Type.Row
            && values.size() == matrix.getShape().rows
        ) {
            ArrayList<Double> result = new ArrayList<>();

            for (int indexC=0; indexC < matrix.getShape().columns; indexC++) {
                List<Double> column = matrix.unsafeGetColumn(indexC);

                double value = 0;
                for (int index=0; index < values.size(); index++) {
                    value += values.get(index) * column.get(index);
                }
                result.add(value);
            }
            return Optional.of(new Vector(result, Type.Row));
        }
        else {
            return Optional.empty();
        }
    }

    public String toString() {
        if (type == Type.Column) {
            String result = "[";

            for (double value : values) {
                result += value + "\n";
            }
            result += "]";
            return result;
        }
        else {
            return values.toString();
        }
    }
}
