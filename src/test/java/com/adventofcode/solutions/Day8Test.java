package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day8Test {
    Day8 solver = new Day8();

    String input = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        // solver.printWithAntiNodes(input);
        assertEquals(14, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // assertEquals(11387, solver.solvePart2(input));
    }
}
