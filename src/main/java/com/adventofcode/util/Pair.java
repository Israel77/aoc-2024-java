package com.adventofcode.util;

import java.util.function.Function;

public record Pair<F, S>(F first, S second) {
    public <T> T mapFirst(Function<F, T> mapper) {
        return mapper.apply(first);
    }

    public <T> T mapSecond(Function<S, T> mapper) {
        return mapper.apply(second);
    }

    public <T> T flatMap(Function<Pair<F, S>, T> mapper) {
        return mapper.apply(this);
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
