package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Day2 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        return parseReports(input).stream()
                .filter(this::isReportSafe)
                .mapToInt(r -> 1)
                .sum();
    }

    public Integer solvePart2(String input) {
        return parseReports(input).stream()
                .filter(this::isDampenedReportSafe)
                .mapToInt(r -> 1)
                .sum();
    }

    boolean isReportSafe(List<Integer> report) {
        Integer previousValue = null;
        // Stores whether or not the values increased
        // at any point in the sequence
        Boolean increasingSequence = null;

        for (int value : report) {
            if (previousValue == null) {
                previousValue = value;
                continue;
            }

            int diff = value - previousValue;
            int absDiff = Math.abs(diff);

            if (increasingSequence == null) {
                increasingSequence = diff > 0;
            }

            if (absDiff < 1 || absDiff > 3)
                return false;

            if ((increasingSequence && diff < 0) || (!increasingSequence && diff > 0))
                return false;

            // Setup next iteration
            previousValue = value;
        }
        return true;
    }

    private boolean isDampenedReportSafe(List<Integer> report) {
        if (isReportSafe(report)) {
            return true;
        }

        // O(n²)
        for (int i = 0; i < report.size(); i++) {
            var reportWithItemRemoved = new ArrayList<>(report);
            reportWithItemRemoved.remove(i);

            // O(n)
            if (isReportSafe(reportWithItemRemoved))
                return true;
        }

        return false;
    }

    private List<List<Integer>> parseReports(String input) {
        return input.lines()
                .map(line -> Arrays.stream(line.split("\\s+"))
                        .map(Integer::parseInt).toList())
                .toList();
    }
}
