package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day12Test {
    Day12 solver = Day12.INSTANCE;

    String input1 = """
            AAAA
            BBCD
            BBCC
            EEEC""";

    String input2 = """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO""";

    String input3 = """
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

    String input4 = """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE""";

    String input5 = """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(140, solver.solvePart1(input1));
        assertEquals(772, solver.solvePart1(input2));
        assertEquals(1930, solver.solvePart1(input3));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        assertEquals(80, solver.solvePart2(input1));
        assertEquals(436, solver.solvePart2(input2));
        assertEquals(1206, solver.solvePart2(input3));
        assertEquals(236, solver.solvePart2(input4));
        assertEquals(368, solver.solvePart2(input5));
    }
}