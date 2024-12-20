package com.adventofcode.solutions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

import com.adventofcode.util.Pair;
import com.adventofcode.util.counters.BigCounter;
import com.adventofcode.util.counters.Counter;
import com.adventofcode.util.counters.SimpleCounter;

public enum Day19 implements Solver<Integer, BigInteger> {
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
    public BigInteger solvePart2(String input) {
        var parsedInput = parseInput(input);

        var availableTowels = parsedInput.first();
        var desiredSequences = parsedInput.second();

        BinaryOperator<BigInteger> bigIntegerSum = (a, b) -> a.add(b);

        return desiredSequences.stream()
                .parallel()
                .map(sequence -> countPossibleSequences(sequence, availableTowels))
                .reduce(BigInteger.valueOf(0), bigIntegerSum, bigIntegerSum);
    }

    boolean findTowelSequence(String desiredSequence, Collection<String> availableTowels) {
        // Checking if the entire subSequence corresponds to a towel
        // can be done in O(1) time if availableTowels is a Set
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

    BigInteger countPossibleSequences(String desiredSequence, Collection<String> availableTowels) {
        // Changed to a bottom-up approach to generate the strings
        // We count how many substrings we can fit after a given index
        Counter<Integer, BigInteger> sequencesUpToIndex = new BigCounter<>();
        sequencesUpToIndex.put(0, BigInteger.valueOf(1));

        IntStream.range(1, desiredSequence.length() + 1)
                .forEach(i -> {
                    for (var towel : availableTowels) {
                        int begin = i - towel.length();
                        if (begin < 0)
                            continue;
                        if (desiredSequence.substring(begin, i).equals(towel)) {
                            sequencesUpToIndex.incrementBy(i, sequencesUpToIndex.get(begin));
                        }
                    }
                });

        return sequencesUpToIndex.get(desiredSequence.length());
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
