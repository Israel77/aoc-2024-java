package com.adventofcode.solutions;

public interface Solver<P1, P2> {
    default P1 solvePart1(String input) {
        throw new UnsupportedOperationException("Solution for part 1 not implemented yet!");
    };

    default P2 solvePart2(String input) {
        throw new UnsupportedOperationException("Solution for part 2 not implemented yet!");
    }
}
