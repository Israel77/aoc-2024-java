package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day9Test {
    Day9 solver = Day9.INSTANCE;

    String input = "2333133121414131402";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        // input = "12345";
        // assertEquals(60, solver.solvePart1(input));
        assertEquals(1928L, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // assertEquals(34, solver.solvePart2(input));
    }
}