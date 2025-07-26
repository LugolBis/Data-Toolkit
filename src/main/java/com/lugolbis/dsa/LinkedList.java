package com.lugolbis.dsa;

import java.util.Optional;

public class LinkedList<T> {
    T value;
    Optional<LinkedList<T>> next;

    private LinkedList(T value) {
        this.value = value;
        next = Optional.empty();
    }

    public static <T> Optional<LinkedList<T>> newLinkedList(T value) {
        if (value != null) {
            return Optional.of(new LinkedList<>(value));
        }
        else {
            return Optional.empty();
        }
    }

    public T getValue() {
        return value;
    }

    public Optional<LinkedList<T>> getNext() {
        return next;
    }

    public void setValue(T value) {
        if (value != null) {
            this.value = value;
        }
    }

    public void setNext(LinkedList<T> next) {
        if (next != null) {
            this.next = Optional.of(next);
        }
    }

    public void insert(T value) {
        if (value == null) {
            return;
        }
        else if (this.next.isEmpty()) {
            this.setNext(new LinkedList<>(value));
        }
        else {
            this.next.get().insert(value);
        }
    }

    public boolean search(T value) {
        if (value == null) {
            return false;
        }
        else if (this.value.equals(value)) {
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
