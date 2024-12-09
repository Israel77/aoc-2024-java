package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.adventofcode.util.Pair;

public enum Day5 implements Solver<Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String inputString) {
        Input input = parseInput(inputString);
        var rules = input.rules;
        var updates = input.updates;

        int middlePageSum = 0;

        for (var update : updates) {
            if (validateUpdate(rules, update))
                middlePageSum += update.get(update.size() / 2);
        }

        return middlePageSum;
    }

    private boolean validateUpdate(List<Pair<Integer, Integer>> rules, List<Integer> update) {
        for (int i = 0; i < update.size(); ++i) {
            var value = update.get(i);
            var shouldComeBefore = rules.stream()
                    .filter(pair -> pair.second() == value)
                    .map(Pair::first)
                    .collect(Collectors.toSet());
            var shouldComeAfter = rules.stream()
                    .filter(pair -> pair.first() == value)
                    .map(Pair::second)
                    .collect(Collectors.toSet());

            var before = update.subList(0, i);
            for (int n : before) {
                if (shouldComeAfter.contains(n))
                    return false;
            }
            var after = update.subList(i, update.size());
            for (int n : after) {
                if (shouldComeBefore.contains(n))
                    return false;
            }
        }
        return true;
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    Input parseInput(String inputString) {
        List<Pair<Integer, Integer>> rules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        inputString.lines()
                .forEach(line -> {
                    if (line.isBlank())
                        return;

                    String[] tryRules = line.split("\\|");
                    if (tryRules.length == 2) {
                        var pair = new Pair<>(Integer.parseInt(tryRules[0]), Integer.parseInt(tryRules[1]));
                        rules.add(pair);
                    } else {

                        String[] tryPages = line.split(",");
                        var pageList = new ArrayList<Integer>();
                        for (String pageString : tryPages) {
                            pageList.add(Integer.parseInt(pageString));
                        }
                        if (!pageList.isEmpty()) {
                            updates.add(pageList);
                        }
                    }
                });

        return new Input(rules, updates);
    }

    private record Input(List<Pair<Integer, Integer>> rules, List<List<Integer>> updates) {
    }
}
