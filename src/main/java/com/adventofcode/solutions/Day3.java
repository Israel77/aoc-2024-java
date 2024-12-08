package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Day3 implements Solver<Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        return parseInput(input).stream()
                .mapToInt(pair -> pair.first() * pair.second())
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    List<Mul> parseInput(String input) {
        List<Mul> result = new ArrayList<>();

        Pattern mul = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher mulMatcher = mul.matcher(input);

        while (mulMatcher.find()) {
            int first = Integer.parseInt(mulMatcher.group(1));
            int second = Integer.parseInt(mulMatcher.group(2));

            result.add(new Mul(first, second));
        }

        return result;
    }

    private record Mul(Integer first, Integer second) {
    }
}
