package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day3Test {
    Solver<Integer> solver = Day3.INSTANCE;
    String input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(161, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // assertEquals(4, solver.solvePart2(input));
    }

}
