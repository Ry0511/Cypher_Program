package cyphergram.transposition.utility.transposition;

import java.util.NoSuchElementException;

/**
 * Iterator implementation for usage of traversing a String char by char
 * with the back-end code implemented to allow ease of traversal. This allows
 * traversal to go forwards and backwards.
 *
 * @author -Ry
 * @version 1.0
 * Copyright: N/A
 */
public class StringIterator implements java.util.Iterator<String> {
    /**
     * This is the String to Iterate through, char by char.
     */
    private final String text;

    /**
     * This is the index counter which determines which character we should
     * return when {@link #next()} or {@link #previous} are called.
     */
    private int index;

    /**
     * Construct our String Iterator, initialising the variables for the Text
     * and Index.
     *
     * @param cypher Input text to be iterated through.
     */
    public StringIterator(final String cypher) {
        this.text = cypher;
        this.index = 0;
    }

    /**
     * @return The Full String provided at Iterator construction. {@link #text}
     */
    @Override
    public String toString() {
        return this.text;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return index < text.length();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public String next() {
        if (hasNext()) {
            final String out = String.valueOf(text.charAt(index));
            index++;
            return out;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns the previous element in the iteration. (This can be used to
     * index backwards)
     *
     * @return the previous element in the iteration
     * @throws NoSuchElementException if the iteration has no previous element
     */
    public String previous() {
        if (hasPrevious()) {
            final String out = String.valueOf(text.charAt(index - 1));
            index--;
            return out;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * @return {@code true} if the Iterator has a previous element (index can
     * be sent backwards) to find an element in the String. Else this will
     * return {@code false}.
     */
    public boolean hasPrevious() {
        return ((index - 1) >= 0);
    }
}
