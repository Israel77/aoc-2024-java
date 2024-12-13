package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import com.adventofcode.util.Functions;
import com.adventofcode.util.Pair;

public enum Day1 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        return parseLocations(input)
                .map(pair -> {
                    // Sorts the left and right columns
                    var left = pair.first().stream()
                            .sorted()
                            .toList();
                    var right = pair.second().stream()
                            .sorted()
                            .toList();

                    return Functions.zip(left, right);
                })
                .stream()
                .mapToInt(v -> Math.abs(v.first() - v.second()))
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        final var locations = parseLocations(input);
        return calculateSimilarities(locations)
                .sum();
    }

    IntStream calculateSimilarities(Pair<List<Integer>, List<Integer>> locations) {
        var left = locations.first();
        var right = locations.second();
        var counter = new HashMap<Integer, Integer>(left.size() + right.size());

        // Count how many times each value appears on the second list
        for (int value : right) {
            var currentCount = counter.getOrDefault(value, 0);
            counter.put(value, currentCount + 1);
        }

        return left.stream()
                .mapToInt(value -> value * counter.getOrDefault(value, 0));
    }

    Pair<List<Integer>, List<Integer>> parseLocations(String input) {
        var leftList = new ArrayList<Integer>();
        var rightList = new ArrayList<Integer>();
        int count = 0;

        for (String numberString : input.split("\\s+")) {
            var number = Integer.parseInt(numberString);
            if (count % 2 == 0) {
                leftList.add(number);
            } else {
                rightList.add(number);
            }
            count++;
        }

        return new Pair<>(leftList, rightList);
    }
}
