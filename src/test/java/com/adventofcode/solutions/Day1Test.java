package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day1Test {
    Solver<Integer> solver = Day1.INSTANCE;

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        String input = """
                3   4
                4   3
                2   5
                1   3
                3   9
                3   3""";

        assertEquals(11, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        String input = """
                3   4
                4   3
                2   5
                1   3
                3   9
                3   3""";

        assertEquals(31, solver.solvePart2(input));
    }
}
