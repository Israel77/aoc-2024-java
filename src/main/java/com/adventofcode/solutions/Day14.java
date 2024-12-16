package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.adventofcode.util.Pair;

public class Day14 implements Solver<Integer, String> {

    private final Pair<Integer, Integer> availableSpace;
    private final int iterationStart;
    private final int iterationEnd;
    private final boolean visualize;

    /**
     * Solution for Day 14 depends on available space, which is a pair
     * representing the number of columns and rows of the space where
     * the robots can move.
     * 
     * iterationStart and iterationEnd control the pagination for part 2
     * where the number of iterations can get very high, and recomputing
     * again would be inefficient, so the last iteration in a page is cached.
     * 
     * For example, you can start with iterationStart=0 and iterationEnd=2000,
     * this will try to find the answer within the first 2000 iterations. If
     * no result is found, you can do iterationStart=2000 (cached from last try)
     * and iterationEnd=4000 to search within the next 2000 iterations and so on.
     * 
     * You can run as many iterations per request as you want (and your hardware
     * supports), but keep in mind that only the last one is cached. So if you try
     * iterationStart=2000 and iterationEnd=4000, followed by iterationStart=3500
     * and iterationEnd=5000, this would be as inefficient as running with
     * iterationStart=0/iterationEnd=5000, since the 3500th iteration was not
     * cached. This means you should prefer using iterationEnd of each request
     * as iterationStart for the next.
     * 
     * You can also try to find the easter egg manually by passing visualize=true.
     * This will print the robots position on each iteration.
     * 
     * @param availableSpace
     * @param iterationStart
     * @param iterationEnd
     * @param visualize
     */
    public Day14(Pair<Integer, Integer> availableSpace, int iterationStart, int iterationEnd, boolean visualize) {
        this.availableSpace = availableSpace;
        this.iterationStart = iterationStart;
        this.iterationEnd = iterationEnd;
        this.visualize = visualize;
    }

    @Override
    public Integer solvePart1(String input) {
        return parseInput(input).stream()
                .parallel()
                .map(robot -> {
                    var iterator = new RobotIterator(robot, availableSpace);
                    var position = robot.position;

                    for (int i = 0; i < 100; ++i)
                        position = iterator.next().position;

                    return position;
                })
                .collect(Collectors.groupingBy(position -> getQuadrant(position,
                        availableSpace)))
                .entrySet().stream()
                .parallel()
                .filter(entry -> entry.getKey() != 0)
                .mapToInt(entry -> (int) entry.getValue().stream().count())
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public String solvePart2(String input) {

        var initialRobots = Day14Cache.INSTANCE.get().computeIfAbsent(iterationStart, key -> {
            List<Robot> result = parseInput(input);
            var iterators = result.stream()
                    .map(robot -> new RobotIterator(robot, availableSpace))
                    .toList();
            for (int i = 0; i < iterationStart; ++i) {
                result = iterators.stream().map(it -> it.next()).toList();
            }
            return result;
        });
        var iterators = initialRobots.stream()
                .map(robot -> new RobotIterator(robot, availableSpace))
                .toList();

        StringBuilder sb = new StringBuilder();

        List<Robot> robots = new ArrayList<>();
        for (int i = iterationStart; i < iterationEnd; ++i) {
            robots = iterators.stream()
                    .map(it -> it.next())
                    .toList();
            var positions = robots.stream()
                    .map(robot -> robot.position())
                    .toList();

            if (visualize) {
                sb.append("Iteration: " + i + 1);
                sb.append('\n');
                sb.append(printRobotsNoCount(positions, availableSpace));
                sb.append('\n');
            } else if (findStreak(positions)) {
                sb.append(i + 1);
                break;
            }
            ;
        }

        Day14Cache.INSTANCE.get().put(iterationEnd, robots);

        return sb.toString();
    }

    boolean findStreak(List<Pair<Integer, Integer>> positions) {
        Map<Integer, Set<Integer>> ys = new HashMap<>();

        for (var position : positions) {
            var xs = ys.getOrDefault(position.y(), new HashSet<>());
            xs.add(position.x());
            ys.put(position.y(), xs);
        }

        for (var xs : ys.values()) {
            List<Integer> xsList = new ArrayList<>();
            xs.forEach(xsList::add);
            xsList.sort(Integer::compare);

            int lastValue = 0;
            int streakCount = 0;
            for (int value : xsList) {
                if (value - lastValue == 1)
                    streakCount++;
                lastValue = value;

                if (streakCount >= 30)
                    return true;
            }
        }
        return false;
    }

    String printRobots(List<Pair<Integer, Integer>> positions, Pair<Integer, Integer> availableSpace) {
        Map<Pair<Integer, Integer>, Integer> counter = new HashMap<>();

        for (var position : positions) {
            counter.put(position, counter.getOrDefault(position, 0) + 1);
        }

        StringBuilder result = new StringBuilder();
        for (int y = 0; y < availableSpace.y(); ++y) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < availableSpace.x(); ++x) {
                var position = new Pair<>(x, y);
                var count = counter.getOrDefault(position, 0);
                line.append(count == 0 ? '.' : count.toString());
            }
            line.append('\n');
            result.append(line);
        }
        return result.toString();
    }

    private String printRobotsNoCount(List<Pair<Integer, Integer>> positions, Pair<Integer, Integer> availableSpace) {
        Set<Pair<Integer, Integer>> positionSet = positions.stream().collect(Collectors.toSet());

        StringBuilder result = new StringBuilder();
        for (int y = 0; y < availableSpace.y(); ++y) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < availableSpace.x(); ++x) {
                var position = new Pair<>(x, y);
                line.append(positionSet.contains(position) ? '#' : '.');
            }
            line.append('\n');
            result.append(line);
        }
        return result.toString();
    }

    private int getQuadrant(Pair<Integer, Integer> position, Pair<Integer, Integer> availableSpace) {
        int middleX = (availableSpace.x() - 1) / 2;
        int middleY = (availableSpace.y() - 1) / 2;

        if (position.x() > middleX && position.y() < middleY)
            return 1;
        if (position.x() < middleX && position.y() < middleY)
            return 2;
        if (position.x() < middleX && position.y() > middleY)
            return 3;
        if (position.x() > middleX && position.y() > middleY)
            return 4;

        return 0;
    }

    List<Robot> parseInput(String input) {
        Pattern robotPattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");

        return input.lines()
                .map(line -> {
                    Matcher robotMatcher = robotPattern.matcher(line);
                    if (robotMatcher.lookingAt()) {
                        var pX = Integer.parseInt(robotMatcher.group(1));
                        var pY = Integer.parseInt(robotMatcher.group(2));
                        var vX = Integer.parseInt(robotMatcher.group(3));
                        var vY = Integer.parseInt(robotMatcher.group(4));

                        return new Robot(new Pair<>(pX, pY), new Pair<>(vX, vY));
                    }
                    return null;
                })
                .filter(r -> r != null)
                .toList();
    }

    record Robot(Pair<Integer, Integer> position, Pair<Integer, Integer> velocity) {
    }

    class RobotIterator implements Iterator<Robot> {
        int currentX;
        int currentY;
        int velocityX;
        int velocityY;
        final Pair<Integer, Integer> velocity;
        int maxX;
        int maxY;

        RobotIterator(Robot robot, Pair<Integer, Integer> availableSpace) {
            this.currentX = robot.position.x();
            this.currentY = robot.position.y();
            this.velocityX = robot.velocity.x();
            this.velocityY = robot.velocity.y();
            this.velocity = robot.velocity;
            this.maxX = availableSpace.x();
            this.maxY = availableSpace.y();
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Robot next() {
            currentX = (currentX + velocityX + maxX) % maxX;
            currentY = (currentY + +velocityY + maxY) % maxY;

            return new Robot(new Pair<>(currentX, currentY), velocity);
        }

        public Robot current() {
            return new Robot(new Pair<>(currentX, currentY), velocity);
        }
    }
}
