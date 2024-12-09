package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day2Test {
    Solver<Integer, Integer> solver = Day2.INSTANCE;
    String input = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(2, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        assertEquals(4, solver.solvePart2(input));
    }
}
