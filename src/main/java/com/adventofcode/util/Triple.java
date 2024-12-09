package com.adventofcode.util;

import java.util.function.Function;

public record Triple<F, S, T>(F first, S second, T third) {

    public <O> O mapFirst(Function<F, O> mapper) {
        return mapper.apply(first);
    }

    public <O> O mapSecond(Function<S, O> mapper) {
        return mapper.apply(second);
    }

    public <O> O mapThird(Function<T, O> mapper) {
        return mapper.apply(third);
    }

    public <O> O flatMap(Function<Triple<F, S, T>, O> mapper) {
        return mapper.apply(this);
    }
}
