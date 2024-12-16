package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.adventofcode.graphs.implementations.SimpleGraph;
import com.adventofcode.graphs.interfaces.Graph;
import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public enum Day16 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {

        Maze maze = parseInput(input);
        List<Pair<Integer, Integer>> optimalPath = findOptimalPath(maze);

        return calculateCostFromPath(optimalPath);
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
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

    private int calculateCostFromPath(List<Pair<Integer, Integer>> path) {

        int steps = 0;
        int turns = 0;

        // Always start facing east/right
        Direction lastDirection = Direction.RIGHT;
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

    private Direction getDirection(Pair<Integer, Integer> lastPosition, Pair<Integer, Integer> position) {
        Direction direction = Direction.fromPair(new Pair<>(
                position.x() - lastPosition.x(),
                position.y() - lastPosition.y()));
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
    List<Pair<Integer, Integer>> findOptimalPath(Maze maze) {

        final int DEFAULT_COST = (maze.width() + maze.height()) * 1000;

        var graph = maze.graph();
        var start = maze.start();
        var end = maze.end();

        var currentDirection = Direction.RIGHT;

        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cameFrom = new HashMap<>();

        Map<Pair<Integer, Integer>, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<Pair<Integer, Integer>, Integer> fScore = new HashMap<>();
        fScore.put(start, costHeuristic(start, end));

        PriorityQueue<Pair<Integer, Integer>> openSet = new PriorityQueue<>(
                (a, b) -> Integer.compare(fScore.getOrDefault(a, DEFAULT_COST),
                        fScore.getOrDefault(b, DEFAULT_COST)));
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

                int tentativeGScore = gScore.getOrDefault(current, DEFAULT_COST) + costToNeighbor;

                if (tentativeGScore < gScore.getOrDefault(neighbor, DEFAULT_COST)) {
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
