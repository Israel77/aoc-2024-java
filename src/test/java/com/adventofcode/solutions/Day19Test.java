package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day19Test {
    Day19 solver = Day19.INSTANCE;

    String input1 = """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(6, solver.solvePart1(input1));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
    }
}
