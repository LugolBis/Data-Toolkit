package com.lugolbis.dsa;

import java.util.Optional;

public class LinkedList<T> {
    T value;
    Optional<LinkedList<T>> next;

    public LinkedList(T value) {
        this.value = value;
        next = Optional.empty();
    }

    public T getValue() {
        return value;
    }

    public Optional<LinkedList<T>> getNext() {
        return next;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setNext(LinkedList<T> next) {
        this.next = Optional.of(next);
    }

    public void insert(T value) {
        if (this.next.isEmpty()) {
            this.setNext(new LinkedList<>(value));
        }
        else {
            this.next.get().insert(value);
        }
    }

    public boolean search(T value) {
        if (this.value.equals(value)) {
            return true;
        }
        else if (this.next.isPresent()) {
            return this.next.get().search(value);
        }
        else {
            return false;
        }
    }

    public String toString() {
        String result = String.format("[ %s ]", value);
        if (next.isPresent()) {
            return String.format("%s->%s", result, next.get().toString());
        }
        else {
            return result;
        }
    }
}
