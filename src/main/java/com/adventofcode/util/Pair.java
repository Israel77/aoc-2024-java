package com.adventofcode.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public record Pair<F, S>(F first, S second) {
    public <T> T mapFirst(Function<? super F, ? extends T> mapper) {
        return mapper.apply(first);
    }

    public <T> T mapSecond(Function<? super S, ? extends T> mapper) {
        return mapper.apply(second);
    }

    public <T> T map(Function<Pair<F, S>, T> mapper) {
        return mapper.apply(this);
    }

    public <T> T map(BiFunction<F, S, T> mapper) {
        return mapper.apply(first, second);
    }

    public <A, B> Pair<A, B> flatMap(Function<Pair<? super F, ? super S>, Pair<A, B>> mapper) {
        return mapper.apply(this);
    }

    public static Pair<Integer, Integer> sum(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return new Pair<>(a.first() + b.first(), a.second() + b.second());
    }

    /**
     * Alias for first, as the pair is often used to
     * represent 2d coordinates.
     * 
     * @return Same as first()
     */
    public F x() {
        return first;
    }

    /**
     * Alias for second, as the pair is often used to
     * represent 2d coordinates.
     * 
     * @return Same as second()
     */
    public S y() {
        return second;
    }
}
