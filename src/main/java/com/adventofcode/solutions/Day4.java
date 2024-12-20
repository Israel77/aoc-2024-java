package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public enum Day4 implements Solver<Integer, Integer> {
    INSTANCE;

    ExecutorService executor = Executors.newWorkStealingPool();

    @Override
    public Integer solvePart1(String input) {
        return splitLines(input).parallelStream()
                .mapToInt(this::countXmasOcurrences)
                .sum();

    }

    @Override
    public Integer solvePart2(String input) {
        var lines = input.lines().toList();
        int numOfRows = lines.size();
        int numOfColumns = lines.get(0).length();

        return IntStream.range(1, numOfRows - 1)
                .parallel()
                .map(y -> IntStream.range(1, numOfColumns - 1)
                        .map(x -> detectX_mas(lines, x, y) ? 1 : 0)
                        .sum())
                .sum();
    }

    boolean detectX_mas(List<String> lines, int x, int y) {
        if (lines.get(y).charAt(x) != 'A') {
            return false;
        }
        char upperLeft = lines.get(y - 1).charAt(x - 1);
        char upperRight = lines.get(y - 1).charAt(x + 1);
        char lowerLeft = lines.get(y + 1).charAt(x - 1);
        char lowerRight = lines.get(y + 1).charAt(x + 1);

        return (upperLeft == 'M' && upperRight == 'M' && lowerLeft == 'S' && lowerRight == 'S')
                || (upperLeft == 'M' && upperRight == 'S' && lowerLeft == 'M' && lowerRight == 'S')
                || (upperLeft == 'S' && upperRight == 'M' && lowerLeft == 'S' && lowerRight == 'M')
                || (upperLeft == 'S' && upperRight == 'S' && lowerLeft == 'M' && lowerRight == 'M');
    }

    int countXmasOcurrences(String line) {
        int count = 0;

        for (int pos = 0; pos <= line.length() - 4; ++pos) {
            String slice = line.substring(pos, pos + 4);
            if ("XMAS".equals(slice) || "SAMX".equals(slice)) {
                count++;
            }
        }

        return count;
    }

    List<String> splitLines(String input) {
        List<String> result = new ArrayList<>();

        var futures = List.of(
                CompletableFuture.runAsync(() -> {
                    result.addAll(splitHorizontalLines(input));
                }, executor),
                CompletableFuture.runAsync(() -> {
                    result.addAll(splitVerticalLines(input));
                }, executor),
                CompletableFuture.runAsync(() -> {
                    result.addAll(splitDiagonals(input));
                }, executor),
                CompletableFuture.runAsync(() -> {
                    result.addAll(splitAntiDiagonals(input));
                }, executor));

        for (var future : futures) {
            future.join();
        }

        return result;
    }

    private List<String> splitHorizontalLines(String input) {
        return input.lines().toList();
    }

    private List<String> splitVerticalLines(String input) {
        List<String> result = new ArrayList<>();

        // Assumes that all lines have the same number
        // of characters
        var horizontalLines = splitHorizontalLines(input);
        int numOfColumns = horizontalLines.get(0).length();

        for (int i = 0; i < numOfColumns; ++i) {
            StringBuilder sb = new StringBuilder();
            for (var horizontalLine : horizontalLines) {
                sb.append(horizontalLine.charAt(i));
            }
            result.add(sb.toString());
        }

        return result;
    }

    private List<String> splitDiagonals(String input) {
        List<String> result = new ArrayList<>();

        // Assumes that all lines have the same number
        // of characters
        var horizontalLines = splitHorizontalLines(input);
        int numOfColumns = horizontalLines.get(0).length();
        int numOfRows = horizontalLines.size();

        for (int offset = numOfRows - 1; offset > 0; --offset) {
            StringBuilder sb = new StringBuilder();

            for (int x = 0; x < numOfColumns; ++x) {
                var y = x + offset;
                if (y >= numOfRows)
                    break;
                sb.append(horizontalLines.get(y).charAt(x));
            }

            result.add(sb.toString());
        }

        for (int offset = 0; offset < numOfColumns; ++offset) {
            StringBuilder sb = new StringBuilder();

            for (int y = 0; y < numOfRows; ++y) {
                var x = y + offset;
                if (x >= numOfColumns)
                    break;
                sb.append(horizontalLines.get(y).charAt(x));
            }

            result.add(sb.toString());
        }

        return result;
    }

    private List<String> splitAntiDiagonals(String input) {
        List<String> result = new ArrayList<>();

        // Assumes that all lines have the same number
        // of characters
        var horizontalLines = splitHorizontalLines(input);
        int numOfColumns = horizontalLines.get(0).length();
        int numOfRows = horizontalLines.size();

        for (int startRow = numOfRows - 1; startRow > 0; --startRow) {
            StringBuilder sb = new StringBuilder();

            for (int y = startRow; y < numOfRows; ++y) {
                var x = numOfColumns - 1 - (y - startRow);
                if (x < 0)
                    break;
                sb.append(horizontalLines.get(y).charAt(x));
            }

            result.add(sb.toString());
        }

        for (int offset = numOfColumns - 1; offset >= 0; --offset) {
            StringBuilder sb = new StringBuilder();

            for (int y = 0; y < numOfRows; ++y) {
                var x = offset - y;
                if (x < 0)
                    break;
                sb.append(horizontalLines.get(y).charAt(x));
            }

            result.add(sb.toString());
        }

        return result;
    }
}
