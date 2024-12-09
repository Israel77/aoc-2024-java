package com.adventofcode.solutions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.adventofcode.util.Pair;

public enum Day7 implements Solver<Long, Long> {
    INSTANCE;

    @Override
    public Long solvePart1(String input) {
        return parseInput(input).parallelStream()
                .filter(pair -> calculatePossibleResults(pair.second(), pair.first())
                        .contains(pair.first()))
                .mapToLong(Pair::first)
                .sum();
    }

    @Override
    public Long solvePart2(String input) {
        return parseInput(input).parallelStream()
                .filter(pair -> calculatePossibleResults(
                        // Convert all values to BigInteger
                        pair.second().stream().map(BigInteger::valueOf).toList(),
                        BigInteger.valueOf(pair.first()))
                        .contains(BigInteger.valueOf(pair.first())))
                .mapToLong(Pair::first)
                .sum();
    }

    List<Long> calculatePossibleResults(List<Long> values, long threshold) {
        return calculatePossibleResults(values.get(0), values.subList(1, values.size()), threshold)
                .stream()
                .filter(Pair::second)
                .map(Pair::first)
                .toList();
    }

    List<Pair<Long, Boolean>> calculatePossibleResults(long currentValue, List<Long> values, long threshold) {
        // The boolean marks whether or not this is a true result
        // (using all numbers)
        List<Pair<Long, Boolean>> results = new ArrayList<>();

        if (values.isEmpty() || currentValue > threshold)
            return results;

        long add = currentValue + values.get(0);
        long multiply = currentValue * values.get(0);
        if (add <= threshold) {
            results.add(new Pair<>(add, values.size() == 1));
            results.addAll(calculatePossibleResults(add, values.subList(1, values.size()), threshold));
        }
        if (multiply <= threshold) {
            results.add(new Pair<>(multiply, values.size() == 1));
            results.addAll(calculatePossibleResults(multiply, values.subList(1, values.size()), threshold));
        }

        return results;
    }

    List<BigInteger> calculatePossibleResults(List<BigInteger> values, BigInteger threshold) {
        return calculatePossibleResults(values.get(0), values.subList(1, values.size()), threshold)
                .stream()
                .filter(Pair::second)
                .map(Pair::first)
                .toList();
    }

    List<Pair<BigInteger, Boolean>> calculatePossibleResults(BigInteger currentValue, List<BigInteger> values,
            BigInteger threshold) {
        // The boolean marks whether or not this is a true result
        // (using all numbers)
        List<Pair<BigInteger, Boolean>> results = new ArrayList<>();

        if (values.isEmpty() || currentValue.compareTo(threshold) > 1)
            return results;

        BigInteger add = currentValue.add(values.get(0));
        BigInteger multiply = currentValue.multiply(values.get(0));
        BigInteger concat = new BigInteger(currentValue.toString() + values.get(0).toString());
        if (add.compareTo(threshold) <= 0) {
            results.add(new Pair<>(add, values.size() == 1));
            results.addAll(calculatePossibleResults(add, values.subList(1, values.size()), threshold));
        }
        if (multiply.compareTo(threshold) <= 0) {
            results.add(new Pair<>(multiply, values.size() == 1));
            results.addAll(calculatePossibleResults(multiply, values.subList(1, values.size()), threshold));
        }
        if (concat.compareTo(threshold) <= 0) {
            results.add(new Pair<>(concat, values.size() == 1));
            results.addAll(calculatePossibleResults(concat, values.subList(1, values.size()), threshold));
        }

        return results;
    }

    List<Pair<Long, List<Long>>> parseInput(String input) {
        return input.lines()
                .filter(line -> !line.isBlank())
                .map(line -> {
                    var split = line.split(":");
                    long id = Long.parseLong(split[0]);

                    List<Long> values = new ArrayList<>();
                    for (String valueString : split[1].split("\\s+")) {
                        if (!valueString.isBlank())
                            values.add(Long.parseLong(valueString));
                    }
                    return new Pair<>(id, values);
                })
                .toList();
    }
}
