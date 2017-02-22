package com.github.ac31007_group_8.quiz.util;

/**
 * A simple Pair class, without using javafx.
 *
 * @author Robert T.
 */
public class Pair<A, B> {

    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair<" + first + ", " + second + ">";
    }
}
