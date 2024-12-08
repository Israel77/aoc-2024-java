package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Day3 implements Solver<Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        return parseInput(input).stream()
                .filter(i -> i instanceof Mul)
                .map(i -> (Mul) i)
                .mapToInt(pair -> pair.first() * pair.second())
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        int result = 0;
        boolean enable = true;
        for (Instruction instruction : parseInput(input)) {
            switch (instruction) {
                case Mul m:
                    result += enable ? m.first() * m.second() : 0;
                    break;
                case Do __:
                    enable = true;
                    break;
                case Dont __:
                    enable = false;
                    break;
            }
        }
        return result;
    }

    static List<Instruction> parseInput(String input) {
        String sanitizedInput = input.trim().lines()
                .map(line -> line.trim())
                .collect(Collectors.joining());
        List<Instruction> result = new ArrayList<>();

        Pattern mul = Pattern.compile("^(mul\\((\\d+),(\\d+)\\)).*?");
        Pattern _do = Pattern.compile("^do().*?");
        Pattern dont = Pattern.compile("^don\'t().*?");

        int currentIndex = 0;
        while (currentIndex < sanitizedInput.length()) {
            var currentSubstring = sanitizedInput.substring(currentIndex);

            Matcher mulMatcher = mul.matcher(currentSubstring);
            Matcher doMatcher = _do.matcher(currentSubstring);
            Matcher dontMatcher = dont.matcher(currentSubstring);

            if (dontMatcher.matches()) {
                result.add(new Dont());
                currentIndex += 6;
            } else if (doMatcher.matches()) {
                result.add(new Do());
                currentIndex += 4;
            } else if (mulMatcher.matches()) {
                int first = Integer.parseInt(mulMatcher.group(2));
                int second = Integer.parseInt(mulMatcher.group(3));

                result.add(new Mul(first, second));
                currentIndex += mulMatcher.group(1).length();
            } else {
                currentIndex++;
            }
        }

        System.out.println("Parsed " + result.size() + " expressions");
        System.out.println(result);
        return result;
    }

    private sealed interface Instruction permits Mul, Do, Dont {
    }

    private record Mul(Integer first, Integer second) implements Instruction {
    }

    private record Do() implements Instruction {
    }

    private record Dont() implements Instruction {
    }
}
