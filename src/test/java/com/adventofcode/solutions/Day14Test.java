package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.adventofcode.util.Pair;

public class Day14Test {
    Day14 solver = new Day14(new Pair<>(11, 7), 0, 100, false);

    String input = """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
                        """;

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        assertEquals(12, solver.solvePart1(input));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // Code almost untestable, just test manually if you touch the solution
    }
}
