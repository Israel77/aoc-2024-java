package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.adventofcode.util.Pair;

public class Day18Test {

    String input1 = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0""";

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        // Use 6x6 grid and 12 bytes for this test
        Day18 solver = new Day18(6, 6, 12);

        assertEquals(22, solver.solvePart1(input1));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
        // Use 6x6 grid and 12 bytes for this test
        Day18 solver = new Day18(6, 6, 12);

        assertEquals("6,1", solver.solvePart2(input1));
    }
}
