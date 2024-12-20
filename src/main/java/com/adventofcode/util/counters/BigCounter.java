package com.adventofcode.util.counters;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BigCounter<T> implements Counter<T, BigInteger> {
    private Map<T, BigInteger> innerMap;
    private static BigInteger ZERO = BigInteger.valueOf(0);
    private static BigInteger ONE = BigInteger.valueOf(1);

    public BigCounter() {
        this.innerMap = new HashMap<>();
    }

    public BigCounter(Collection<T> collection) {
        this.innerMap = new HashMap<>();

        for (var value : collection) {
            this.increment(value);
        }
    }

    @Override
    public void increment(T object) {
        innerMap.put(object, innerMap.getOrDefault(object, ZERO)
                .add(ONE));
    }

    @Override
    public void decrement(T object) {
        if (get(object).compareTo(ZERO) > 0)
            innerMap.put(object, innerMap.get(object).subtract(ONE));
    }

    @Override
    public void incrementBy(T object, BigInteger amount) {
        innerMap.put(object, innerMap.getOrDefault(object, ZERO).add(amount));
    }

    @Override
    public void decrementBy(T object, BigInteger amount) {
        if (get(object).subtract(amount).compareTo(ZERO) >= 0)
            innerMap.put(object, get(object).subtract(amount));
    }

    @Override
    public BigInteger get(Object object) {
        return innerMap.getOrDefault(object, ZERO);
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
    public Set<T> keySet() {
        return this.innerMap.keySet();
    }

    @Override
    public BigInteger put(T object, BigInteger count) {
        if (count.compareTo(ZERO) < 0)
            throw new IllegalArgumentException("Count cannot be negative");
        BigInteger oldValue = get(object);
        this.innerMap.put(object, count);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends T, ? extends BigInteger> m) {
        for (var entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public BigInteger remove(Object key) {
        return this.innerMap.remove(key);
    }

    @Override
    public int size() {
        return this.innerMap.size();
    }

    @Override
    public Collection<BigInteger> values() {
        return this.innerMap.values();
    }

    @Override
    public Set<Entry<T, BigInteger>> entrySet() {
        return this.innerMap.entrySet();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Counter{}";
        }

        StringBuilder sb = new StringBuilder("Counter{");
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