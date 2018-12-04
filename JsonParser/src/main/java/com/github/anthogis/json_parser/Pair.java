package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

public class Pair<K, V> {
    private K key;
    private V value;


    public Pair(K key, V value) {
        this.value = value;
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }
}
