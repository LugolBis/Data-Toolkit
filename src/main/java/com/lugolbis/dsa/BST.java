package com.lugolbis.dsa; 

import java.util.Optional;
import java.util.OptionalInt;

public class BST {
    private OptionalInt root;
    private Optional<BST> left;
    private Optional<BST> right;

    public BST(int x) {
        this.root = OptionalInt.of(x);
        this.left = Optional.empty();
        this.right = Optional.empty();
    }

    public OptionalInt getValue() {
        return this.root;
    }

    public Optional<BST> getLeft() {
        return this.left;
    }

    public Optional<BST> getRight() {
        return this.right;
    }

    public void setLeft(BST tree) {
        this.left = Optional.of(tree);
    }

    public void setRight(BST tree) {
        this.right = Optional.of(tree);
    }

    public void insert(int x) {
        if (!this.root.isPresent()) {
            this.root = OptionalInt.of(x);
        }
        else {
            if (this.root.getAsInt() > x) {
                if (!this.left.isPresent()) {
                    this.left = Optional.of(new BST(x));
                }
                else {
                    this.left.get().insert(x);
                }
            }
            else if (this.root.getAsInt() < x) {
                if (!this.right.isPresent()) {
                    this.right = Optional.of(new BST(x));
                }
                else {
                    this.right.get().insert(x);
                }
            }
        }
        return;
    }

    public boolean search(int x) {
        if (!this.root.isPresent()) {
            return false;
        }
        else {
            if (this.root.getAsInt() > x) {
                if (this.left.isPresent()) {
                    return this.left.get().search(x);
                }
                else {
                    return false;
                }
            }
            else if (this.root.getAsInt() < x) {
                if (this.right.isPresent()) {
                    return this.right.get().search(x);
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }
    }

    private BST search_min() {
        if (!this.left.isPresent()) {
            return this;
        }
        else {
            return this.left.get().search_min();
        }
    }

    public void delete(int x) {
        if (!this.root.isPresent()) {
            return;
        }

        if (this.root.getAsInt() == x) {
            int state = (this.left.isPresent() ? 1 : 0) << 1 | (this.right.isPresent() ? 1 : 0);

            switch (state) {
                case 0b00:
                    break;
                case 0b01:
                    this.root = this.right.get().root;
                    this.right = this.right.get().right;
                    break;
                case 0b10:
                    this.root = this.left.get().root;
                    this.left = this.left.get().left;
                    break;
                case 0b11:
                    BST tree_right = this.right.get();
                    BST tree_left = this.left.get();

                    BST min_right = tree_right.search_min();
                    min_right.left = Optional.of(tree_left);

                    this.root = tree_right.root;
                    this.right = tree_right.right;
                    this.left = tree_right.left;
                    break;
            }
        }
        else if (this.root.getAsInt() > x) {
            if (this.left.isPresent()) {
                this.left.get().delete(x);
            }
        }
        else {
            if (this.right.isPresent()) {
                this.right.get().delete(x);
            }
        }
        return;
    }

    public String display(int level) {
        String result = "\n" + "| ".repeat(level) + "Root of the tree : ";
        result +=  (this.root.isPresent()) ? this.root.getAsInt() : "EMPTY";
        
        result += "\n" + "| ".repeat(level);
        result += (this.left.isPresent()) ? "Left -> " + this.left.get().display(level + 1) : "Left -> NILL";
        result += "\n" + "| ".repeat(level);
        result += (this.right.isPresent()) ? "Right -> " + this.right.get().display(level + 1) : "Right -> NILL";

        return result;
    }
}
