package com.lugolbis.dsa;

import java.util.LinkedList;
import java.util.Optional;

public class Stack<T> {
    LinkedList<T> values;

    public Stack() {
        values = new LinkedList<>();
    }

    public Optional<T> getValue() {
        if (values.size() > 0) {
            return Optional.of(values.removeLast());
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
