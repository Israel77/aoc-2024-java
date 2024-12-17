package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import com.adventofcode.graphs.implementations.SimpleGraph;
import com.adventofcode.graphs.interfaces.Graph;
import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public enum Day16 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {

        Maze maze = parseInput(input);
        final int DEFAULT_COST = (maze.width() + maze.height()) * 1000;

        List<Pair<Integer, Integer>> optimalPath = findOptimalPath(
                maze.graph(), maze.start(), maze.end(), Direction.RIGHT,
                DEFAULT_COST);

        return calculateCostFromPath(optimalPath, Direction.RIGHT);
    }

    @Override
    public Integer solvePart2(String input) {

        Maze maze = parseInput(input);

        return findVerticesInMinimalPaths(maze)
                .size();
    }

    Collection<Pair<Integer, Integer>> findVerticesInMinimalPaths(Maze maze) {

        var graph = maze.graph();
        var start = maze.start();
        var end = maze.end();

        final int DEFAULT_COST = (maze.width() + maze.height()) * 1000;

        Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> pathsFromStart = new ConcurrentHashMap<>();
        Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> pathsToEnd = new ConcurrentHashMap<>();
        Map<Integer, List<Pair<Integer, Integer>>> totalCosts = new HashMap<>();

        int knownOptimalCost = calculateCostFromPath(findOptimalPath(graph, start,
                end, Direction.RIGHT, DEFAULT_COST), Direction.RIGHT);

        Set<Pair<Integer, Integer>> minimalPathsVertices = new HashSet<>();

        for (var vertex : graph.vertices()) {
            boolean isPathPossible = true;

            var pathFromStart = pathsFromStart.computeIfAbsent(vertex,
                    v -> findOptimalPath(graph, start, v, Direction.RIGHT, DEFAULT_COST));
            int pathLength = pathFromStart.size();

            IntStream.range(0, pathLength)
                    .parallel()
                    .forEach(i -> {
                        // for (int i = 0; i < pathLength; ++i) {
                        final int viewEnd = i + 1;
                        var intermediateVertex = pathFromStart.get(i);
                        pathsFromStart.computeIfAbsent(intermediateVertex,
                                v -> pathFromStart.subList(0, viewEnd));
                    });

            isPathPossible &= !pathFromStart.isEmpty();

            int costFromStart;

            // Kinda ugly, but using computeIfAbsent would throw a
            // ConcurrentModificationException
            costFromStart = calculateCostFromPath(pathFromStart, Direction.RIGHT);

            var directionFromStart = vertex.equals(start)
                    ? Direction.RIGHT
                    : getDirection(pathFromStart.get(pathLength - 2),
                            pathFromStart.get(pathLength - 1));

            int costToEnd;

            var pathToEnd = pathsToEnd.computeIfAbsent(vertex,
                    v -> findOptimalPath(graph, v, end, directionFromStart, DEFAULT_COST));
            costToEnd = calculateCostFromPath(pathToEnd, directionFromStart);

            isPathPossible &= !pathToEnd.isEmpty();

            IntStream.range(0, pathToEnd.size())
                    .parallel()
                    .forEach(i -> {
                        // for (int i = 0; i < pathToEnd.size(); ++i) {
                        final int viewStart = i;
                        var intermediateVertex = pathToEnd.get(i);
                        pathsToEnd.computeIfAbsent(intermediateVertex, v -> pathToEnd.subList(viewStart,
                                pathToEnd.size()));
                    });

            int totalCost = costFromStart + costToEnd;
            if (isPathPossible && totalCost == knownOptimalCost) {

                // List<Pair<Integer, Integer>> sameCost = totalCosts.getOrDefault(totalCost,
                // new ArrayList<>());
                // sameCost.add(vertex);

                // totalCosts.put(totalCost, sameCost);
                minimalPathsVertices.add(vertex);
            }
        }

        // int minCost = totalCosts.keySet().stream()
        // .mapToInt(Integer::valueOf)
        // .min()
        // .getAsInt();

        return minimalPathsVertices;
    }

    private int calculateCostFromPath(List<Pair<Integer, Integer>> path,
            Direction initialDirection) {

        int steps = 0;
        int turns = 0;

        Direction lastDirection = initialDirection;
        Pair<Integer, Integer> lastPosition = null;

        for (var position : path) {
            if (lastPosition == null) {
                lastPosition = position;
                continue;
            }

            Direction direction = getDirection(lastPosition, position);

            if (direction != lastDirection)
                turns++;

            steps++;

            lastDirection = direction;
            lastPosition = position;
        }

        return steps + turns * 1000;
    }

    private Direction getDirection(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        Direction direction = Direction.fromPair(new Pair<>(
                to.x() - from.x(),
                to.y() - from.y()));
        return direction;
    }

    /**
     * A* algorithm for finding the optimal path. Adapted from Wikipedia
     * pseudocode.
     * 
     * I realized I don't need to setup the edges manually
     * since I can check if they're valid based on the coordinates,
     * so I could have used just a Set of points instead of a graph.
     * But anyway, I wanted to test my graph implementations and if
     * it works, I'm not complaining.
     * 
     * @param maze
     * @return
     */
    List<Pair<Integer, Integer>> findOptimalPath(Graph<Pair<Integer, Integer>> graph,
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

                if (!graph.containsVertex(neighbor))
                    continue;

                int costToNeighbor = currentDirection == direction ? 1 : 1000;

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

    public Maze parseInput(String input) {
        Graph<Pair<Integer, Integer>> graph = new SimpleGraph<>();
        Pair<Integer, Integer> start = null;
        Pair<Integer, Integer> end = null;

        var chars = input
                .lines()
                .map(line -> line.chars()
                        .mapToObj(x -> (char) x)
                        .toList())
                .toList();

        int y = 0;
        for (var line : chars) {
            for (int x = 0; x < line.size(); ++x) {
                char currentChar = chars.get(y).get(x);

                if (currentChar == '#')
                    continue;

                var position = new Pair<>(x, y);

                if (currentChar == 'S')
                    start = position;
                else if (currentChar == 'E')
                    end = position;

                graph.addVertex(position);

                for (var direction : Direction.values()) {

                    var neighbor = Pair.sum(position, direction.asPair());

                    if (0 <= neighbor.x() && neighbor.x() < line.size() &&
                            0 <= neighbor.y() && neighbor.y() < chars.size() &&
                            chars.get(neighbor.y()).get(neighbor.x()) != '#') {
                        graph.addEdge(position, neighbor);
                    }
                }
            }
            ++y;
        }

        return new Maze(graph, start, end, chars.get(0).size(), chars.size());
    }

    String printPath(List<Pair<Integer, Integer>> path, Maze maze) {

        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < maze.height(); ++y) {
            StringBuilder line = new StringBuilder();

            for (int x = 0; x < maze.width(); x++) {
                var position = new Pair<>(x, y);

                if (position.equals(maze.start())) {
                    line.append('S');
                } else if (position.equals(maze.end())) {
                    line.append('E');
                } else if (path.contains(position)) {
                    line.append('O');
                } else if (maze.graph().containsVertex(position)) {
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

    record Maze(Graph<Pair<Integer, Integer>> graph, Pair<Integer, Integer> start,
            Pair<Integer, Integer> end, int width, int height) {

        @Override
        public String toString() {
            return String.format("[start=%s, end=%s, width=%s, height=%s]\n",
                    start, end, width, height) + reconstructInput();
        }

        private String reconstructInput() {
            StringBuilder sb = new StringBuilder();

            for (int y = 0; y < height; ++y) {
                StringBuilder line = new StringBuilder();

                for (int x = 0; x < width; x++) {
                    var position = new Pair<>(x, y);

                    if (position.equals(start())) {
                        line.append('S');
                    } else if (position.equals(end())) {
                        line.append('E');
                    } else if (graph.containsVertex(position)) {
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

    }
}
