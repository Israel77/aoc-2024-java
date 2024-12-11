package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public enum Day10 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        HeightMap heightMap = parseInput(input);

        List<Pair<Integer, Integer>> trailheads = new ArrayList<>();
        for (int y = 0; y < heightMap.numOfRows; y++) {
            for (int x = 0; x < heightMap.numOfColumns; ++x) {
                var pos = new Pair<>(x, y);
                if (heightMap.getHeight(pos) == 0)
                    trailheads.add(pos);
            }
        }

        return trailheads.stream()
                .mapToInt(pos -> calculateScore(pos, heightMap, Optional.empty()))
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    int calculateScore(Pair<Integer, Integer> position, HeightMap heightMap,
            Optional<Set<Pair<Integer, Integer>>> previouslyVisited) {
        Set<Pair<Integer, Integer>> visited = previouslyVisited.orElse(new HashSet<>());

        if (visited.contains(position)) {
            return 0;
        }

        visited.add(position);
        int height = heightMap.getHeight(position);
        if (height == 9) {
            return 1;
        }

        int count = 0;
        for (Direction direction : Direction.values()) {
            var newPosition = new Pair<>(position.x() + direction.asPair().x(),
                    position.y() + direction.asPair().y());
            if (isInBounds(newPosition, heightMap)
                    && heightMap.getHeight(newPosition) == height + 1) {
                count += calculateScore(newPosition, heightMap, Optional.of(visited));
            }
        }
        return count;
    }

    private boolean isInBounds(Pair<Integer, Integer> position, HeightMap heightMap) {
        return isInBounds(position.x(), position.y(), heightMap);
    }

    private boolean isInBounds(int x, int y, HeightMap heightMap) {
        return 0 <= x
                && x < heightMap.numOfColumns()
                && 0 <= y
                && y < heightMap.numOfRows();
    }

    HeightMap parseInput(String input) {
        List<List<Integer>> heights = input.lines()
                .map(s -> s.chars().map(c -> c - 48).boxed().toList())
                .toList();
        int numOfRows = heights.size();
        int numOfColumns = heights.get(0).size();
        return new HeightMap(heights, numOfRows, numOfColumns);
    }

    private record HeightMap(List<List<Integer>> heights, int numOfRows, int numOfColumns) {
        public int getHeight(int x, int y) {
            return heights.get(y).get(x);
        }

        public int getHeight(Pair<Integer, Integer> coord) {
            return getHeight(coord.first(), coord.second());
        }
    }

}
