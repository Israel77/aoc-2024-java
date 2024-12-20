package com.adventofcode.util.counters;

import java.util.Map;

public class ConcurrentCounter<T> implements Counter<T, Long> {

    private final Map<T, Long> innerMap;

    public ConcurrentCounter() {
        this.innerMap = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public ConcurrentCounter(java.util.Collection<T> collection) {
        this.innerMap = new java.util.concurrent.ConcurrentHashMap<>();

        for (var value : collection) {
            this.increment(value);
        }
    }

    @Override
    public synchronized void increment(T object) {
        innerMap.put(object, innerMap.getOrDefault(object, 0L) + 1);
    }

    @Override
    public synchronized void decrement(T object) {
        if (get(object) > 0L)
            innerMap.put(object, innerMap.get(object) - 1);
    }

    @Override
    public synchronized void incrementBy(T object, Long amount) {
        innerMap.put(object, innerMap.getOrDefault(object, 0L) + amount);
    }

    @Override
    public synchronized void decrementBy(T object, Long amount) {
        if (get(object) - amount > 0L)
            innerMap.put(object, innerMap.get(object) - 1);
    }

    @Override
    public Long get(Object object) {
        return innerMap.getOrDefault(object, 0L);
    }

    @Override
    public void clear() {
        this.innerMap.clear();
    }

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
    public java.util.Set<T> keySet() {
        return this.innerMap.keySet();
    }

    @Override
    public synchronized Long put(T object, Long count) {
        if (count < 0L)
            throw new IllegalArgumentException("Count cannot be negative");
        long oldValue = get(object);
        this.innerMap.put(object, count);
        return oldValue;
    }

    @Override
    public synchronized void putAll(Map<? extends T, ? extends Long> m) {
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
    public java.util.Collection<Long> values() {
        return this.innerMap.values();
    }

    @Override
    public java.util.Set<Entry<T, Long>> entrySet() {
        return this.innerMap.entrySet();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "ConcurrentCounter{}";
        }

        StringBuilder sb = new StringBuilder("ConcurrentCounter{");
        for (var entry : innerMap.entrySet()) {
            sb.append(entry.getKey())
                    .append('=')
                    .append(entry.getValue())
                    .append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append('}');
        return sb.toString();
    }

}
