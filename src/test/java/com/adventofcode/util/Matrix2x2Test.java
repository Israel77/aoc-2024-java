package com.adventofcode.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Matrix2x2Test {
    @Test
    void testSolveLinearSystem() {
        var coefficients = Matrix2x2.ofColumns(new Pair<>(94l, 34l), new Pair<>(22l,
                67l));
        var constants = new Pair<>(8400l, 5400l);
        Pair<Long, Long> solution = coefficients.solveLinearSystem(constants).get()
                .map((x, y) -> new Pair<>(x.longValue(), y.longValue()));

        assertEquals(80, solution.first());
        assertEquals(40, solution.second());
    }

    @Test
    void testSolveLinearSystem_invalid() {
        // Zero solutions
        var coefficients = Matrix2x2.ofColumns(new Pair<>(2l, 4l), new Pair<>(3l, 6l));
        var constants = new Pair<>(16l, 3l);
        var solution = coefficients.solveLinearSystem(constants);

        assertTrue(solution.isEmpty());

        // Infinite solutions
        coefficients = Matrix2x2.ofColumns(new Pair<>(2l, 4l), new Pair<>(3l, 6l));
        constants = new Pair<>(2l, 3l);
        solution = coefficients.solveLinearSystem(constants);

        assertTrue(solution.isEmpty());
    }
}
