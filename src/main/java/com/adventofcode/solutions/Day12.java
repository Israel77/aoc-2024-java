package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public enum Day12 implements Solver<Long, Integer> {
    INSTANCE;

    @Override
    public Long solvePart1(String input) {
        return parseInput(input).regions().stream()
                .mapToLong(Region::price)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    Grid parseInput(String input) {

        List<Cell> cells = new ArrayList<>();

        var lines = input.lines().toList();
        var numOfRows = lines.size();
        var numOfColumns = lines.get(0).length();

        for (int y = 0; y < numOfRows; y++) {
            for (int x = 0; x < numOfColumns; x++) {
                Cell cell = new Cell(lines.get(y).charAt(x), new Pair<>(x, y));
                cells.add(cell);
            }
        }

        return new Grid(cells, numOfRows, numOfColumns);
    }

    record Cell(char id, Pair<Integer, Integer> position) {
    }

    static class Grid {
        final List<Cell> _cells;
        final int _numOfRows;
        final int _numOfColumns;

        // Same as cells, but implemented as a Map to make look-up
        // easier and more efficient.
        final Map<Pair<Integer, Integer>, Character> cellMap;
        List<Region> _regions;
        // Marks whether a cell already belongs to a region.
        Set<Cell> visited = new HashSet<>();

        Grid(List<Cell> cells, int numOfRows, int numOfColumns) {
            this._cells = cells;
            this._numOfRows = numOfRows;
            this._numOfColumns = numOfColumns;

            final Map<Pair<Integer, Integer>, Character> _cellMap = new HashMap<>();
            for (var cell : cells) {
                _cellMap.put(cell.position(), cell.id());
            }
            this.cellMap = _cellMap;

            initRegions();
        }

        private void initRegions() {
            List<Region> regions = new ArrayList<>();

            for (Cell cell : cells()) {
                if (visited.contains(cell)) {
                    continue;
                }

                List<Cell> path = traverseRegion(cell);
                regions.add(new Region(path));
            }

            this._regions = regions;
        }

        private List<Cell> traverseRegion(Cell initialCell) {

            final char id = initialCell.id();

            final List<Cell> path = new ArrayList<>();
            if (visited.contains(initialCell)) {
                return path;
            }

            path.add(initialCell);
            visited.add(initialCell);
            for (Direction direction : Direction.values()) {
                var newPosition = new Pair<>(initialCell.position().x() + direction.asPair().x(),
                        initialCell.position().y() + direction.asPair().y());

                if (isInBounds(newPosition) && cellMap.get(newPosition) == id) {
                    var nextCell = new Cell(id, newPosition);
                    path.addAll(traverseRegion(nextCell));
                }
            }
            return path;
        }

        public List<Cell> cells() {
            return this._cells;
        }

        public int numOfRows() {
            return this._numOfRows;
        }

        public int numOfColumns() {
            return this._numOfColumns;
        }

        public List<Region> regions() {
            return this._regions;
        }

        private boolean isInBounds(Pair<Integer, Integer> position) {
            return 0 <= position.x()
                    && position.x() < numOfColumns()
                    && 0 <= position.y()
                    && position.y() < numOfRows();
        }
    }

    record Region(List<Cell> cells) {
        public long price() {
            return (long) area() * (long) perimeter();
        }

        public int area() {
            return cells().size();
        }

        public int perimeter() {
            // An edge is the boundary between two cells.
            // Since the order of the cells does not matter
            // this can be represented as a set containing
            // the positions of the two cells.
            // Since we don't want to count duplicate edges,
            // they are put within another set.
            Set<Set<Pair<Integer, Integer>>> edges = new HashSet<>();

            var cellCoordinateSet = cells.stream()
                    .map(cell -> cell.position)
                    .collect(Collectors.toSet());
            for (var cell : cells) {
                for (var direction : Direction.values()) {
                    // Neighbouring coordinate (does not matter if it is valid cell or empty space)
                    var neighbour = new Pair<>(cell.position.x() + direction.asPair().x(),
                            cell.position.y() + direction.asPair().y());

                    // Don't count internal edges
                    if (!cellCoordinateSet.contains(neighbour)) {
                        var edge = Set.of(cell.position(), neighbour);
                        edges.add(edge);
                    }
                }
            }

            return edges.size();
        }
    }
}
