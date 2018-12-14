package com.github.anthogis.json_parser;

/**
 * An object that holds two values.
 *
 * @param <K> the first value.
 * @param <V> the second value.
 */
public class Pair<K, V> {
    private K key;
    private V value;

    /**
     * Constructor of a pair of two values.
     *
     * @param key the first value.
     * @param value the second value.
     */
    public Pair(K key, V value) {
        this.value = value;
        this.key = key;
    }

    /**
     * Returns the first value.
     *
     * @return the first value.
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the second value.
     *
     * @return the second value
     */
    public V getValue() {
        return value;
    }
}
