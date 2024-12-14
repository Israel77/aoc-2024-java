package com.adventofcode.solutions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.adventofcode.util.Pair;

public class Day14 implements Solver<Integer, Object> {

    private Pair<Integer, Integer> availableSpace;

    public Day14(Pair<Integer, Integer> availableSpace) {
        this.availableSpace = availableSpace;
    }

    @Override
    public Integer solvePart1(String input) {
        return parseInput(input).stream()
                .parallel()
                .map(robot -> {
                    var iterator = new RobotIterator(robot, availableSpace);
                    var position = robot.position;

                    for (int i = 0; i < 100; ++i)
                        position = iterator.next();

                    return position;
                })
                .sorted((a, b) -> Integer.compare(a.second(), b.second()))
                .collect(Collectors.groupingBy(position -> getQuadrant(position,
                        availableSpace)))
                .entrySet().stream()
                .parallel()
                .filter(entry -> entry.getKey() != 0)
                .mapToInt(entry -> (int) entry.getValue().stream().count())
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public Object solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    public void configureSpace(Pair<Integer, Integer> availableSpace) {
        this.availableSpace = availableSpace;
    }

    private void printRobots(List<Pair<Integer, Integer>> positions, Pair<Integer, Integer> availableSpace) {
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
        System.out.println(result.toString());
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

    class RobotIterator implements Iterator<Pair<Integer, Integer>> {
        int currentX;
        int currentY;
        int velocityX;
        int velocityY;
        int maxX;
        int maxY;

        RobotIterator(Robot robot, Pair<Integer, Integer> availableSpace) {
            this.currentX = robot.position.x();
            this.currentY = robot.position.y();
            this.velocityX = robot.velocity.x();
            this.velocityY = robot.velocity.y();
            this.maxX = availableSpace.x();
            this.maxY = availableSpace.y();
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Pair<Integer, Integer> next() {
            currentX = (currentX + velocityX + maxX) % maxX;
            currentY = (currentY + +velocityY + maxY) % maxY;

            return new Pair<>(currentX, currentY);
        }
    }
}
