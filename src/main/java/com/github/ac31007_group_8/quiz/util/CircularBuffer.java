package com.github.ac31007_group_8.quiz.util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * Ring-style buffer, used in the Database class for handling connections.
 *
 * This provides automatic eviction of objects that have not been recently used.
 *
 * @author Robert T.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CircularBuffer<T> {

    private final Consumer<T> evictFunction;
    private final int capacity;
    private int currIndex = -1; // Increment-before-addition, normally -1 is impossible.
    private Object[] items; // Due to type erasure, we have to use this. Nasty.

    /**
     * Create a new circular buffer with an eviction function.
     *
     * @param capacity Number of items to retain.
     * @param evictFunction Function called on any item that's evicted from the buffer.
     */
    public CircularBuffer(int capacity, Consumer<T> evictFunction) {
        this.evictFunction = evictFunction;
        this.capacity = capacity;
        this.items = new Object[capacity];
    }

    /**
     * Add an item to this buffer.
     *
     * @param item The item to add.
     */
    @SuppressWarnings("unchecked")
    public void add(T item) {
        int targetIndex = currIndex + 1;
        if (targetIndex >= capacity) {
            targetIndex = 0;
        }
        currIndex = targetIndex;

        if (items[currIndex] != null) {
            evictFunction.accept((T)items[currIndex]);
        }
        items[currIndex] = item;
    }

    /**
     * Returns current amount of 'live' items. Always less than or equal to capacity.
     *
     * @return Live object count.
     */
    public int size() {
        int count = 0;
        for (Object o : items) {
            if (o != null) count += 1;
        }
        return count;
    }

    /**
     * Gets the total capacity of this buffer.
     *
     * @return The buffer capacity.
     */
    public int capacity() {
        return capacity;
    }

}
