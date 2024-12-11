package com.adventofcode.util;

import java.util.Iterator;

public class MutableIntRange implements Iterable<Integer> {
    private int _start;
    private int _end;

    public MutableIntRange(int start, int end) {
        if (_start > _end)
            throw new IllegalArgumentException("Range end cannot be smaller than start");
        this._start = start;
        this._end = end;
    }

    public int size() {
        return _end - _start;
    }

    public int start() {
        return this._start;
    }

    public int end() {
        return this._end;
    }

    public void removeFromStart(int amount) {
        if (amount > size()) {
            throw new IllegalArgumentException(
                    String.format("Cannot remove %s from range with size %s", amount, size()));
        }
        this._start += amount;
    }

    public void removeFromEnd(int amount) {
        if (amount > size()) {
            throw new IllegalArgumentException(
                    String.format("Cannot remove %s from range with size %s", amount, size()));
        }
        this._end -= amount;

    }

    public boolean contains(int value) {
        return _start <= value && value < _end;
    }

    public boolean isEmpty() {
        return _start == _end;
    }

    /**
     * Iterate over the range of integers.
     * NOTE: The internal state of the iterator
     * operates on what is essentially a copy of
     * the original range, so mutating the range
     * should not change an ongoing iteration.
     */
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int current = start();
            final int stop = end();

            public boolean hasNext() {
                return current < stop;
            }

            public Integer next() {
                int value = current;
                current++;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        return "Range [start=" + _start + ", end=" + _end + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _start;
        result = prime * result + _end;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MutableIntRange other = (MutableIntRange) obj;
        if (_start != other._start)
            return false;
        if (_end != other._end)
            return false;
        return true;
    }
}