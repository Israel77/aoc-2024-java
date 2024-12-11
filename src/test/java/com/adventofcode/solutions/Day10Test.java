package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day10Test {
    Day10 solver = Day10.INSTANCE;

    String input = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(36, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // assertEquals(2858L, solver.solvePart2(input));
    }
}