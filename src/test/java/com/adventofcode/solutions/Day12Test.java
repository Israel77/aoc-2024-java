package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day12Test {
    Day12 solver = Day12.INSTANCE;

    String input1 = """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO""";

    String input2 = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(772, solver.solvePart1(input1));
        assertEquals(1930, solver.solvePart1(input2));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // assertEquals(65601038650482L, solver.solvePart2(input));
    }
}