package com.adventofcode.solutions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.adventofcode.util.Matrix2x2;
import com.adventofcode.util.Pair;

// TODO: Refactor interfaces of the auxiliary classes to make them consistent
public enum Day13 implements Solver<Integer, BigInteger> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        return parseInput(input).stream()
                .parallel()
                .map(Machine::findSolution)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(pair -> 3 * pair.first() + pair.second())
                .sum();
    }

    @Override
    public BigInteger solvePart2(String input) {
        return parseInputWithOffset(input).stream()
                .parallel()
                .map(LongMachine::findSolution)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(pair -> pair.first().multiply(BigInteger.valueOf(3)).add(pair.second()))
                .reduce(BigInteger.ZERO, (a, b) -> a.add(b));
    }

    List<Machine> parseInput(String input) {
        List<Machine> machines = new ArrayList<>();

        Pair<Integer, Integer> buttonA = null;
        Pair<Integer, Integer> buttonB = null;
        Pair<Integer, Integer> prize = null;

        Pattern buttonAPattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
        Pattern buttonBPattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
        Pattern prizePattern = Pattern.compile("Prize: X\\=(\\d+), Y\\=(\\d+)");

        for (var line : input.lines().toList()) {
            var buttonAMatcher = buttonAPattern.matcher(line.trim());
            var buttonBMatcher = buttonBPattern.matcher(line.trim());
            var prizeMatcher = prizePattern.matcher(line.trim());

            if (buttonAMatcher.lookingAt()) {
                buttonA = new Pair<Integer, Integer>(Integer.parseInt(buttonAMatcher.group(1)),
                        Integer.parseInt(buttonAMatcher.group(2)));
            } else if (buttonBMatcher.lookingAt()) {
                buttonB = new Pair<Integer, Integer>(Integer.parseInt(buttonBMatcher.group(1)),
                        Integer.parseInt(buttonBMatcher.group(2)));
            } else if (prizeMatcher.lookingAt()) {
                prize = new Pair<Integer, Integer>(Integer.parseInt(prizeMatcher.group(1)),
                        Integer.parseInt(prizeMatcher.group(2)));
            }

            if (buttonA != null && buttonB != null && prize != null) {

                machines.add(new Machine(buttonA, buttonB, prize));

                buttonA = null;
                buttonB = null;
                prize = null;
            }
        }

        return machines;
    }

    List<LongMachine> parseInputWithOffset(String input) {
        List<LongMachine> machines = new ArrayList<>();
        Pair<Long, Long> buttonA = null;
        Pair<Long, Long> buttonB = null;
        Pair<Long, Long> prize = null;

        Pattern buttonAPattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
        Pattern buttonBPattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
        Pattern prizePattern = Pattern.compile("Prize: X\\=(\\d+), Y\\=(\\d+)");

        for (var line : input.lines().toList()) {
            var buttonAMatcher = buttonAPattern.matcher(line.trim());
            var buttonBMatcher = buttonBPattern.matcher(line.trim());
            var prizeMatcher = prizePattern.matcher(line.trim());

            if (buttonAMatcher.lookingAt()) {
                buttonA = new Pair<Long, Long>(Long.parseLong(buttonAMatcher.group(1)),
                        Long.parseLong(buttonAMatcher.group(2)));
            } else if (buttonBMatcher.lookingAt()) {
                buttonB = new Pair<Long, Long>(Long.parseLong(buttonBMatcher.group(1)),
                        Long.parseLong(buttonBMatcher.group(2)));
            } else if (prizeMatcher.lookingAt()) {
                prize = new Pair<Long, Long>(
                        Long.parseLong(prizeMatcher.group(1)) + 10000000000000l,
                        Long.parseLong(prizeMatcher.group(2)) + 10000000000000l);
            }

            if (buttonA != null && buttonB != null && prize != null) {

                machines.add(new LongMachine(buttonA, buttonB, prize));

                buttonA = null;
                buttonB = null;
                prize = null;
            }
        }
        return machines;
    }

    record LongMachine(Pair<Long, Long> buttonA,
            Pair<Long, Long> buttonB,
            Pair<Long, Long> prize) {
        public Optional<Pair<BigInteger, BigInteger>> findSolution() {
            var coefficients = Matrix2x2.ofColumns(buttonA, buttonB);
            var solution = coefficients.solveLinearSystem(prize);

            System.out.println(solution);
            if (solution.isEmpty() || !isExactSolution(solution.get()))
                return Optional.empty();

            return solution.map(pair -> {
                return pair.map((a, b) -> {
                    return new Pair<BigInteger, BigInteger>(
                            BigInteger.valueOf(a),
                            BigInteger.valueOf(b));
                });
            });
        }

        // It took me an unreasonable amount of time to realize this check
        // is necessary, since I'm only working with whole values
        boolean isExactSolution(Pair<Long, Long> candidateSolution) {
            var a = BigInteger.valueOf(candidateSolution.first());
            var b = BigInteger.valueOf(candidateSolution.second());
            var buttonAX = BigInteger.valueOf(buttonA.x());
            var buttonAY = BigInteger.valueOf(buttonA.y());
            var buttonBX = BigInteger.valueOf(buttonB.x());
            var buttonBY = BigInteger.valueOf(buttonB.y());
            var prizeX = BigInteger.valueOf(prize.x());
            var prizeY = BigInteger.valueOf(prize.y());

            var candidateX = a.multiply(buttonAX).add(b.multiply(buttonBX));
            var candidateY = a.multiply(buttonAY).add(b.multiply(buttonBY));

            return candidateX.equals(prizeX)
                    && candidateY.equals(prizeY);
        }
    }

    record Machine(Pair<Integer, Integer> buttonA,
            Pair<Integer, Integer> buttonB,
            Pair<Integer, Integer> prize) {

        private static final int MAX_PRESSES = 100;

        public Optional<Pair<Integer, Integer>> findSolution() {
            var coefficients = Matrix2x2.ofColumns(Pair.intToLong(buttonA), Pair.intToLong(buttonB));
            var maybeSolution = coefficients
                    .solveLinearSystem(Pair.intToLong(prize))
                    .map(Pair::longToInt)
                    .map(Optional::get);

            if (maybeSolution.isEmpty())
                return Optional.empty();

            var solution = maybeSolution.get();

            if (solution.x() < 0 || solution.x() > MAX_PRESSES || solution.y() < 0 || solution.y() > MAX_PRESSES
                    || !isExactSolution(solution))
                return Optional.empty();

            return Optional.of(solution);
        }

        boolean isExactSolution(Pair<Integer, Integer> candidateSolution) {
            var a = candidateSolution.first();
            var b = candidateSolution.second();

            // The numbers should be small enough to do the check
            // as integers.
            return (a * buttonA.x() + b * buttonB.x() == prize.x())
                    && (a * buttonA.y() + b * buttonB.y() == prize.y());
        }

    }
}
