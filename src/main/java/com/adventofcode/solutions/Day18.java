package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public class Day18 implements Solver<Integer, String> {

    final int horizontalRange;
    final int verticalRange;
    final int numberOfBytesPart1;

    public Day18() {
        horizontalRange = 70;
        verticalRange = 70;
        numberOfBytesPart1 = 1024;
    }

    public Day18(int horizontalRange, int verticalRange, int numberOfBytesPart1) {
        this.horizontalRange = horizontalRange;
        this.verticalRange = verticalRange;
        this.numberOfBytesPart1 = numberOfBytesPart1;
    }

    @Override
    public Integer solvePart1(String input) {
        Set<Pair<Integer, Integer>> allPoints = new HashSet<>();
        for (int y = 0; y <= verticalRange; ++y) {
            for (int x = 0; x <= horizontalRange; x++) {
                allPoints.add(new Pair<>(x, y));
            }
        }

        parseInput(input).stream()
                .sequential()
                .limit(numberOfBytesPart1)
                .forEach(point -> allPoints.remove(point));

        var path = findOptimalPath(allPoints,
                new Pair<>(0, 0),
                new Pair<>(horizontalRange, verticalRange),
                Direction.RIGHT, 1000);

        return path.size() - 1;
    }

    @Override
    public String solvePart2(String input) {

        Set<Pair<Integer, Integer>> allPoints = new HashSet<>();
        for (int y = 0; y <= verticalRange; ++y) {
            for (int x = 0; x <= horizontalRange; x++) {
                allPoints.add(new Pair<>(x, y));
            }
        }

        var corruptedMemoryIterator = parseInput(input).iterator();
        int byteCount = 0;
        for (; byteCount < numberOfBytesPart1; ++byteCount) {
            // We know that a path is possible after these bytes
            // since it was already solved in part 1
            allPoints.remove(corruptedMemoryIterator.next());
        }

        Pair<Integer, Integer> currentCorruptedByte = null;
        var path = findOptimalPath(allPoints,
                new Pair<>(0, 0),
                new Pair<>(horizontalRange, verticalRange),
                Direction.RIGHT, 1000);
        do {
            currentCorruptedByte = corruptedMemoryIterator.next();
            allPoints.remove(currentCorruptedByte);
            path = findOptimalPath(allPoints,
                    new Pair<>(0, 0),
                    new Pair<>(horizontalRange, verticalRange),
                    Direction.RIGHT, 1000);

        } while (path.size() > 0);

        return currentCorruptedByte.map((x, y) -> x.toString() + "," + y.toString());

    }

    String printPath(Collection<Pair<Integer, Integer>> path, Collection<Pair<Integer, Integer>> points) {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y <= verticalRange; ++y) {
            StringBuilder line = new StringBuilder();

            for (int x = 0; x <= horizontalRange; x++) {
                var coordinate = new Pair<>(x, y);

                if (path.contains(coordinate)) {
                    line.append('O');
                } else if (points.contains(coordinate)) {
                    line.append('.');
                } else {
                    line.append('#');
                }
            }

            line.append('\n');
            sb.append(line);
        }

        return sb.toString();
    }

    /**
     * A* algorithm for finding the optimal path. Same as used on Day 16
     * this time using only a Set of points.
     * 
     */
    List<Pair<Integer, Integer>> findOptimalPath(Set<Pair<Integer, Integer>> points,
            Pair<Integer, Integer> start,
            Pair<Integer, Integer> end,
            Direction initialDirection,
            int defaultCost) {

        var currentDirection = initialDirection;

        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cameFrom = new HashMap<>();

        Map<Pair<Integer, Integer>, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<Pair<Integer, Integer>, Integer> fScore = new HashMap<>();
        fScore.put(start, costHeuristic(start, end));

        PriorityQueue<Pair<Integer, Integer>> openSet = new PriorityQueue<>(
                (a, b) -> Integer.compare(fScore.getOrDefault(a, defaultCost),
                        fScore.getOrDefault(b, defaultCost)));
        openSet.add(start);

        while (!openSet.isEmpty()) {
            var current = openSet.peek();

            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            if (!cameFrom.isEmpty())
                currentDirection = getDirection(cameFrom.get(current), current);

            openSet.remove(current);

            Direction[] possibleContinuations = { currentDirection,
                    currentDirection.rotateClockwise(),
                    currentDirection.rotateCounterClockwise() };

            for (Direction direction : possibleContinuations) {

                Pair<Integer, Integer> neighbor = Pair.sum(current, direction.asPair());

                if (!points.contains(neighbor))
                    continue;

                int costToNeighbor = 1;

                int tentativeGScore = gScore.getOrDefault(current, defaultCost) + costToNeighbor;

                if (tentativeGScore < gScore.getOrDefault(neighbor, defaultCost)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + costHeuristic(neighbor,
                            end));

                    if (!openSet.contains(neighbor))
                        openSet.add(neighbor);

                }
            }

        }

        return List.of();
    }

    private Direction getDirection(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        Direction direction = Direction.fromPair(new Pair<>(
                to.x() - from.x(),
                to.y() - from.y()));
        return direction;
    }

    List<Pair<Integer, Integer>> reconstructPath(Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cameFrom,
            Pair<Integer, Integer> current) {

        List<Pair<Integer, Integer>> totalPath = new ArrayList<>();

        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }

        return totalPath.reversed();
    }

    int costHeuristic(Pair<Integer, Integer> position, Pair<Integer, Integer> goal) {
        return Math.abs(position.x() - goal.x())
                + Math.abs(position.y() - goal.y());
    }

    List<Pair<Integer, Integer>> parseInput(String input) {
        return input.lines()
                .map(line -> {
                    var split = line.split(",");
                    return new Pair<>(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                })
                .toList();
    }
}
