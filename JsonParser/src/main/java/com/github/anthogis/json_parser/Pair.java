package com.github.anthogis.json_parser;

/**
 * An object that holds two values.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 * @param <F> the first value.
 * @param <S> the second value.
 */
public class Pair<F, S> {
    private F first;
    private S second;

    /**
     * Constructor of a pair of two values.
     *
     * @param first the first value.
     * @param second the second value.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first value.
     *
     * @return the first value.
     */
    public F getFirst() {
        return first;
    }

    /**
     * Returns the second value.
     *
     * @return the second value
     */
    public S getSecond() {
        return second;
    }
}
