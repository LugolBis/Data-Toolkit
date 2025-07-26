package com.lugolbis.dsa;

import java.util.LinkedList;
import java.util.Optional;

public class Tail<T> {
    LinkedList<T> values;

    public Tail() {
        values = new LinkedList<>();
    }

    public Optional<T> getValue() {
        if (values.size() > 0) {
            return Optional.of(values.removeFirst());
        }
        else {
            return Optional.empty();
        }
    }

    public void add(T value) {
        if (value != null) {
            values.addLast(value);
        }
    }

    public int size() {
        return values.size();
    } 
}
