package com.adventofcode.util.counters;

public interface Counter<T, C extends Number> extends java.util.Map<T, C> {

    void increment(T object);

    void decrement(T object);

    void incrementBy(T object, C amount);

    void decrementBy(T object, C amount);

}
