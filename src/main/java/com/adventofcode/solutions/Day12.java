package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public enum Day12 implements Solver<Long, Integer> {
    INSTANCE;

    @Override
    public Long solvePart1(String input) {
        return parseInput(input).regions()
                .parallel()
                .mapToLong(Region::price)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        return parseInput(input).regions()
                .parallel()
                .mapToInt(Region::discountedPrice)
                .sum();

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
        Stream<Region> _regions;
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
            Stream.Builder<Region> regionBuilder = Stream.builder();

            for (Cell cell : cells()) {
                if (visited.contains(cell)) {
                    continue;
                }

                List<Cell> path = traverseRegion(cell);
                regionBuilder.add(new Region(path));
            }

            this._regions = regionBuilder.build();
        }

        private List<Cell> traverseRegion(Cell initialCell) {

            final char id = initialCell.id();

            final var path = new ArrayList<Cell>();
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
                    traverseRegion(nextCell).forEach(cell -> path.add(cell));
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

        public Stream<Region> regions() {
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

        public int discountedPrice() {
            return area() * sides();
        }

        public int area() {
            return cells().size();
        }

        public int perimeter() {
            return edges().size();
        }

        public int sides() {

            int turns = 0;

            var corners = corners();
            Map<Corner, Integer> remainingVisits = numberOfPasses(corners);

            Corner startingCorner = pickTrueCorner(corners);

            turns += countTurns(startingCorner, corners, remainingVisits);

            List<Corner> unvisitedCorners = new ArrayList<>(corners.stream()
                    .filter(corner -> remainingVisits.getOrDefault(corner, 0) > 0)
                    .toList());

            while (!unvisitedCorners.isEmpty()) {
                startingCorner = unvisitedCorners.removeFirst();

                if (remainingVisits.getOrDefault(startingCorner, 0) > 0
                        && isValidStart(startingCorner, corners))
                    turns += countTurns(startingCorner, corners, remainingVisits);
            }

            return turns;
        }

        private Corner pickTrueCorner(Set<Corner> corners) {
            // Only start from true corners, i. e. there are
            // two valid possible continuations.
            var cornerIterator = corners.iterator();
            Corner startingCorner;
            boolean validStart = false;
            do {
                startingCorner = cornerIterator.next();
                validStart = isValidStart(startingCorner, corners);
            } while (!validStart);

            return startingCorner;
        }

        private Map<Corner, Integer> numberOfPasses(Collection<Corner> corners) {

            Map<Corner, Integer> countPasses = new HashMap<>();

            for (Corner corner : corners) {

                List<Direction> validContinuations = new ArrayList<>();

                for (Direction direction : Direction.values()) {

                    var nextCorner = Corner.move(corner, direction);

                    boolean isValidCorner = corners.contains(nextCorner);

                    if (isValidCorner && isValidEdge(corner, direction)) {
                        validContinuations.add(direction);
                    }
                }

                // Should pass once for every two possible continuations
                countPasses.put(corner, validContinuations.size() / 2);
            }
            return countPasses;

        }

        private boolean isValidStart(Corner startingCorner, Set<Corner> corners) {
            List<Direction> validContinuations = new ArrayList<>();

            for (Direction direction : Direction.values()) {

                var nextCorner = Corner.move(startingCorner, direction);

                boolean isValidCorner = corners.contains(nextCorner);

                if (isValidCorner && isValidEdge(startingCorner, direction)) {
                    validContinuations.add(direction);
                }
            }
            return validContinuations.size() == 2
                    && validContinuations.get(0).isPerpendicular(validContinuations.get(1));
        }

        private int countTurns(Corner startingCorner, Set<Corner> corners,
                Map<Corner, Integer> remainingVisits) {
            int turns = 0;

            Corner currentCorner = startingCorner;
            Direction previousDirection = null;
            boolean canAdvance;
            do {
                canAdvance = false;
                int directionsTried = 0;

                Direction direction = previousDirection != null
                        // Prioritize clockwise rotation.
                        ? previousDirection.rotateClockwise()
                        // Arbitrary initial direction
                        : Direction.UP;
                while (directionsTried < Direction.values().length) {
                    var nextCorner = Corner.move(currentCorner, direction);

                    boolean isValidCorner = corners.contains(nextCorner);
                    boolean canVisit = remainingVisits.getOrDefault(nextCorner, 0) > 0;
                    boolean isNewDirection = previousDirection != direction;
                    boolean isPerpendicular = previousDirection == null || previousDirection.isPerpendicular(direction);

                    if ((!isNewDirection || isPerpendicular)
                            && isValidCorner
                            && isValidEdge(currentCorner, direction)
                            && canVisit) {
                        canAdvance = true;
                        if (isNewDirection)
                            turns++;

                        previousDirection = direction;
                        currentCorner = nextCorner;
                        remainingVisits.put(currentCorner,
                                remainingVisits.get(currentCorner) - 1);
                        break;
                    }
                    direction = direction.rotateClockwise();
                    directionsTried++;
                }

            } while (canAdvance);

            return turns;
        }

        // Check if there is an edge linked to the corner in the
        // given direction
        boolean isValidEdge(Corner from, Direction direction) {
            return switch (direction) {
                case UP -> edges().contains(Set.of(
                        from.upperLeft(),
                        from.upperRight()));
                case RIGHT -> edges().contains(Set.of(
                        from.lowerRight(),
                        from.upperRight()));
                case DOWN -> edges().contains(Set.of(
                        from.lowerRight(),
                        from.lowerLeft()));
                case LEFT -> edges().contains(Set.of(
                        from.lowerLeft(),
                        from.upperLeft()));
            };
        }

        Set<Corner> corners() {
            return cells().stream()
                    .map(Cell::position)
                    .map(Corner::getCorners)
                    .reduce((Set<Corner>) new HashSet<Corner>(), (x, y) -> {
                        x.addAll(y);
                        return x;
                    }, (x, y) -> {
                        Set<Corner> combined = new HashSet<>(x);
                        combined.addAll(y);
                        return combined;
                    });
        }

        /**
         * 
         * An edge is the boundary between two cells.
         * Since the order of the cells does not matter
         * this can be represented as a set containing
         * the positions of the two cells.
         * Since we don't want to count duplicate edges,
         * they are put within another set.
         * 
         * @return
         */
        Set<Set<Pair<Integer, Integer>>> edges() {
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

            return edges;
        }
    }

    /**
     * Assumes that the corner represents the top-left corner
     * of the cell with the same coordinates
     */
    record Corner(
            Pair<Integer, Integer> coordinates) {
        /**
         * A corner is a set of 4 cell-coordinates (the ones touching the corner)
         * 
         * @param cellCoords
         * @return
         */
        public static Set<Corner> getCorners(Pair<Integer, Integer> cellCoords) {
            var diagonal = Pair.sum(Direction.DOWN.asPair(), Direction.RIGHT.asPair());

            Corner upperLeftCorner = new Corner(
                    cellCoords);
            Corner upperRightCorner = new Corner(
                    Pair.sum(cellCoords, Direction.RIGHT.asPair()));
            Corner lowerLeftCorner = new Corner(
                    Pair.sum(cellCoords, Direction.DOWN.asPair()));
            Corner lowerRightCorner = new Corner(
                    Pair.sum(cellCoords, diagonal));

            return Set.of(upperLeftCorner, upperRightCorner, lowerLeftCorner, lowerRightCorner);
        }

        public static Corner move(Corner corner, Direction direction) {
            return new Corner(Pair.sum(corner.coordinates(), direction.asPair()));
        }

        /**
         * Returns the coordinates of the cell that is on the upper-left
         * diagonal, relative to this corner.
         * Note that the return is in Cell coordinates, not
         * Corner coordinates. (But in this case they coincide)
         * 
         * @return
         */
        public Pair<Integer, Integer> upperLeft() {
            var offset = new Pair<>(-1, -1);
            return Pair.sum(coordinates, offset);
        }

        /**
         * Returns the oordinates of the cell that is on the upper-right
         * diagonal, relative to this corner.
         * Note that the return is in Cell coordinates, not
         * Corner coordinates.
         * The upper-right Cell coordinates have the same value as
         * the Corner immediately upwards, since we take the
         * top-left corner of a cell as a reference.
         * 
         * @return
         */
        public Pair<Integer, Integer> upperRight() {
            return Pair.sum(coordinates, Direction.UP.asPair());
        }

        /**
         * Returns the coordinates of the cell that is on the lower-left
         * diagonal, relative to this corner.
         * Note that the return is in Cell coordinates, not
         * Corner coordinates.
         * The lower-left Cell coordinates have the same value as
         * the Corner immediately left to this, since we take the
         * top-left corner of a cell as a reference.
         * 
         * @return
         */
        public Pair<Integer, Integer> lowerLeft() {
            return Pair.sum(coordinates, Direction.LEFT.asPair());
        }

        /**
         * Returns the coordinates of the cell that is on the lower-right
         * diagonal, relative to this corner.
         * Note that the return is in Cell coordinates, not
         * Corner coordinates.
         * The lower-right Cell coordinates have the same value as
         * this Corner coordinates, since we take the top-left
         * corner of a cell as a reference.
         * 
         * @return
         */
        public Pair<Integer, Integer> lowerRight() {
            return this.coordinates();
        }
    }
}
