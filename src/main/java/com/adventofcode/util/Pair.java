package com.adventofcode.util;

import java.math.BigInteger;
import java.util.Optional;
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

    public <A, B> Pair<A, B> flatMap(Function<Pair<F, S>, Pair<A, B>> mapper) {
        return mapper.apply(this);
    }

    public static Pair<Integer, Integer> sum(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return new Pair<>(a.first() + b.first(), a.second() + b.second());
    }

    public static Pair<Long, Long> intToLong(Pair<Integer, Integer> pair) {
        return new Pair<>(pair.first().longValue(), pair.second().longValue());
    }

    public static Optional<Pair<Integer, Integer>> longToInt(Pair<Long, Long> pair) {
        if (pair.first() > Integer.MAX_VALUE || pair.second() > Integer.MAX_VALUE) {
            return Optional.empty();
        }

        return Optional.of(new Pair<>(pair.first().intValue(), pair.second().intValue()));
    }

    public static Pair<BigInteger, BigInteger> longToBigInteger(Pair<Long, Long> pair) {
        return new Pair<>(BigInteger.valueOf(pair.first()), BigInteger.valueOf(pair.second()));
    }

    public static Optional<Pair<Long, Long>> bigIntegerToLong(Pair<BigInteger, BigInteger> pair) {
        if (pair.first().compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || pair.second().compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            return Optional.empty();
        }

        return Optional.of(new Pair<>(pair.first().longValue(), pair.second().longValue()));
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
