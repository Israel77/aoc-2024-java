package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.adventofcode.util.Pair;

public enum Day19 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        var parsedInput = parseInput(input);

        var availableTowels = parsedInput.first();
        var desiredSequences = parsedInput.second();

        return (int) desiredSequences.stream()
                .parallel()
                .filter(sequence -> findTowelSequence(sequence, new HashSet<String>(availableTowels)))
                .count();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    boolean findTowelSequence(String desiredSequence, Collection<String> availableTowels) {
        // Checking if the entire subSequence corresponds to a towel
        // can be found in O(1) time if availableTowels is a Set
        if (availableTowels.contains(desiredSequence))
            return true;

        for (var towel : availableTowels) {
            if (desiredSequence.startsWith(towel)) {
                var suffix = desiredSequence.substring(towel.length());
                if (findTowelSequence(suffix, availableTowels)) {
                    return true;
                }
            }
        }

        return false;
    }

    Pair<List<String>, List<String>> parseInput(String input) {
        var lines = input.lines().toList();

        List<String> availableTowels = new ArrayList<>();
        List<String> desiredSequences = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);

            if (line.isBlank())
                continue;

            if (i == 0) {
                availableTowels = Arrays.asList(line.split(", "));
            } else {
                desiredSequences.add(line);
            }
        }

        return new Pair<>(availableTowels, desiredSequences);
    }
}
