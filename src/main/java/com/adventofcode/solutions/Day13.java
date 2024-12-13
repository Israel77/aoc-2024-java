package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.regex.Pattern;

import com.adventofcode.util.Pair;

public enum Day13 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        return parseInput(input).stream()
                .parallel()
                .map(Machine::minTokens)
                .filter(OptionalInt::isPresent)
                .mapToInt(OptionalInt::getAsInt)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
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

    record Machine(Pair<Integer, Integer> buttonA,
            Pair<Integer, Integer> buttonB,
            Pair<Integer, Integer> prize) {

        private static final int MAX_PRESSES = 100;

        public OptionalInt minTokens() {
            final var initialState = new IterationState(0, 0, new Pair<>(0, 0));

            Set<Optional<IterationState>> states = new HashSet<>();
            states.add(Optional.of(initialState));

            Map<Pair<Integer, Integer>, Pair<Integer, Integer>> memoPressA = new HashMap<>();
            Map<Pair<Integer, Integer>, Pair<Integer, Integer>> memoPressB = new HashMap<>();

            boolean advance = true;
            do {
                int previousNumberOfStates = states.size();
                states = iterate(states, memoPressA, memoPressB);
                // Keep iterating until no new states are generated
                advance = previousNumberOfStates != states.size();
            } while (advance);

            return states.stream()
                    .parallel()
                    .filter(state -> state.isPresent() && state.get().position().equals(prize))
                    .map(Optional::get)
                    .mapToInt(IterationState::cost)
                    .min();
        }

        public Set<Optional<IterationState>> iterate(Collection<Optional<IterationState>> states,
                Map<Pair<Integer, Integer>, Pair<Integer, Integer>> memoPressA,
                Map<Pair<Integer, Integer>, Pair<Integer, Integer>> memoPressB) {
            // Represents the number of A presses, B presses and the associated cost
            Set<Optional<IterationState>> outcomes = new HashSet<>(states);

            for (var possibleState : states) {
                possibleState.ifPresent(state -> {
                    outcomes.add(pressA(state, prize, memoPressA));
                    outcomes.add(pressB(state, prize, memoPressB));
                });
            }

            return outcomes;
        }

        Optional<IterationState> pressA(IterationState previousState,
                Pair<Integer, Integer> target,
                Map<Pair<Integer, Integer>, Pair<Integer, Integer>> memoPressA) {
            if (previousState.numberOfAPresses() >= MAX_PRESSES
                    || previousState.position().x() > target.x()
                    || previousState.position().y() > target.y())
                return Optional.empty();

            Pair<Integer, Integer> nextPosition = memoPressA.computeIfAbsent(previousState.position(),
                    position -> Pair.sum(position, buttonA));

            return Optional.of(new IterationState(previousState.numberOfAPresses() + 1,
                    previousState.numberOfBPresses(),
                    nextPosition));
        }

        Optional<IterationState> pressB(IterationState previousState,
                Pair<Integer, Integer> target,
                Map<Pair<Integer, Integer>, Pair<Integer, Integer>> memoPressB) {
            if (previousState.numberOfBPresses() >= MAX_PRESSES
                    || previousState.position().x() > target.x()
                    || previousState.position().y() > target.y())
                return Optional.empty();

            Pair<Integer, Integer> nextPosition = memoPressB.computeIfAbsent(previousState.position(),
                    position -> Pair.sum(position, buttonB));

            return Optional.of(new IterationState(previousState.numberOfAPresses(),
                    previousState.numberOfBPresses() + 1,
                    nextPosition));
        }

    }

    record IterationState(int numberOfAPresses, int numberOfBPresses, Pair<Integer, Integer> position) {
        public int cost() {
            return 3 * numberOfAPresses() + numberOfBPresses();
        }
    }

}
