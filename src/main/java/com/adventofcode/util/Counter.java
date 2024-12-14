package com.adventofcode.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter<T> implements Map<T, Long> {

    private Map<T, Long> innerMap;

    public Counter() {
        this.innerMap = new HashMap<>();
    }

    public Counter(Collection<T> collection) {
        this.innerMap = new HashMap<>();

        for (var value : collection) {
            this.increment(value);
        }
    }

    /**
     * Adds one to the current count of the object.
     * 
     * @param object The object being counted
     */
    public void increment(T object) {
        innerMap.put(object, innerMap.getOrDefault(object, 0l) + 1);
    }

    /**
     * Subtracts one from the current count of the object, if its count is greater
     * than zero, otherwise keep the same count.
     * 
     * Note: Setting a count to zero is not necessarily the same as removing
     * the object, as methods that allow iteration over the keys, such as keySet()
     * and entrySet() will still consider it. To properly remove an object from
     * the Counter, use the remove method.
     * 
     * @param object The object being counted
     */
    public void decrement(T object) {
        // Count cannot be negative
        if (get(object) > 0l)
            innerMap.put(object, innerMap.get(object) - 1);
    }

    /**
     * Returns the current count of a given object.
     * 
     * @param object The object being counted
     */
    @Override
    public Long get(Object object) {
        return innerMap.getOrDefault(object, 0l);
    }

    @Override
    public void clear() {
        this.innerMap.clear();
    }

    /**
     * Always returns true, as every key is valid.
     * We just assume that objects not previously
     * inserted have a count of 0.
     */
    @Override
    public boolean containsKey(Object key) {
        return true;
    }

    @Override
    public boolean containsValue(Object count) {
        return this.innerMap.containsValue(count);
    }

    @Override
    public boolean isEmpty() {
        return this.innerMap.isEmpty();
    }

    @Override
    public Set<T> keySet() {
        return this.innerMap.keySet();
    }

    /**
     * Updates the count of a given object. Does not accept negative counts.
     * 
     * @throws IllegalArgumentException If count is negative.
     */
    @Override
    public Long put(T object, Long count) {
        if (count < 0l)
            throw new IllegalArgumentException("Count cannot be negative");
        // Returning the same as the innerMap would not work
        // because the count of non-existing elements should
        // be 0 instead of null.
        long oldValue = get(object);

        this.innerMap.put(object, count);

        return oldValue;
    }

    /**
     * Updates all counts of the given objects. Does not accept negative counts.
     * 
     * @throws IllegalArgumentException If any count is negative.
     */
    @Override
    public void putAll(Map<? extends T, ? extends Long> m) {
        for (var entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Long remove(Object key) {
        return this.innerMap.remove(key);
    }

    @Override
    public int size() {
        return this.innerMap.size();
    }

    @Override
    public Collection<Long> values() {
        return this.innerMap.values();
    }

    @Override
    public Set<Entry<T, Long>> entrySet() {
        return this.innerMap.entrySet();
    }
}
