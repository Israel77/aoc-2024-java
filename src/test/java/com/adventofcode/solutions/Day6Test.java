package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day6Test {
    Day6 solver = new Day6();

    String input = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(41, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        assertEquals(123, solver.solvePart2(input));
    }
}
