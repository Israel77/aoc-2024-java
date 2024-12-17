package com.adventofcode.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.adventofcode.solutions.Day17.CPU;

public class Day17Test {

    Day17 solver = Day17.INSTANCE;

    String input1 = """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
                        """;

    String input2 = """
            Register A: 123456
            Register B: 0
            Register C: 0

            Program: 2,4,1,5,7,5,0,3,4,0,1,6,5,5,3,0
                        """;

    @Test
    @DisplayName("Should solve the example given in part 1")
    void testSolverPart1() {
        System.out.println(solver.parseInput(input2));
        assertEquals("4,6,3,5,6,3,5,2,1,0", solver.solvePart1(input1));
        assertEquals("1,1,2,5,6,0", solver.solvePart1(input2));
    }

    @Test
    @DisplayName("Should solve the example given in part 2")
    void testSolverPart2() {
    }

    @Test
    @DisplayName("Test CPU instructions")
    void testCPU() {
        CPU cpu = new CPU(0, 0, 9, List.of(2, 6));
        cpu.run();
        assertEquals(1, cpu.registerB);

        cpu = new CPU(10, 0, 0, List.of(5, 0, 5, 1, 5, 4));
        cpu.run();
        assertEquals("0,1,2", cpu.output().map(x -> x.toString()).collect(Collectors.joining(",")));

        cpu = new CPU(2024, 0, 0, List.of(0, 1, 5, 4, 3, 0));
        cpu.run();
        assertEquals("4,2,5,6,7,7,7,7,3,1,0", cpu.output().map(x -> x.toString()).collect(Collectors.joining(",")));
        assertEquals(0, cpu.registerA);

        cpu = new CPU(0, 29, 0, List.of(1, 7));
        cpu.run();
        assertEquals(26, cpu.registerB);

        cpu = new CPU(0, 2024, 43690, List.of(4, 0));
        cpu.run();
        assertEquals(44354, cpu.registerB);
    }
}