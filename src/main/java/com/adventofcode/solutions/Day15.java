package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.adventofcode.util.Direction;
import com.adventofcode.util.Pair;

public enum Day15 implements Solver<Integer, Integer> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {

        var parsedInput = parseInput(input);
        var warehouse = parsedInput.first();
        var directions = parsedInput.second();

        var currentPosition = warehouse.findRobotPosition().get();
        for (var direction : directions) {
            var nextPosition = Pair.sum(currentPosition, direction.asPair());
            if (tryMoving(currentPosition, direction, warehouse))
                currentPosition = nextPosition;
        }

        return warehouse.sumOfBoxGPS();
    }

    @Override
    public Integer solvePart2(String input) {

        var parsedInput = parseInputWide(input);
        var warehouse = parsedInput.first();
        var directions = parsedInput.second();

        var currentPosition = warehouse.findRobotPosition().get();

        for (var direction : directions) {

            var nextPosition = Pair.sum(currentPosition, direction.asPair());
            if (tryMoving(currentPosition, direction, warehouse))
                currentPosition = nextPosition;

        }

        return warehouse.sumOfBoxGPS();
    }

    boolean tryMoving(Pair<Integer, Integer> currentPosition, Direction direction, Warehouse warehouse) {

        var currentTile = warehouse.get(currentPosition);

        var nextPosition = Pair.sum(currentPosition, direction.asPair());
        var nextTile = warehouse.get(nextPosition);

        if (nextTile instanceof Wall)
            return false;

        return switch (currentTile) {
            case Wall wall -> false;
            case EmptyTile empty -> false;
            case Robot bot -> {
                if (nextTile instanceof EmptyTile
                        || ((nextTile instanceof Box
                                || nextTile instanceof WideBoxLeft
                                || nextTile instanceof WideBoxRight)
                                && tryMoving(nextPosition, direction, warehouse))) {
                    warehouse.set(currentPosition, new EmptyTile());
                    warehouse.set(nextPosition, currentTile);
                    yield true;
                }
                yield false;
            }
            case Box box -> {
                if (nextTile instanceof EmptyTile
                        || (nextTile instanceof Box && tryMoving(nextPosition, direction, warehouse))) {
                    warehouse.set(currentPosition, new EmptyTile());
                    warehouse.set(nextPosition, currentTile);
                    yield true;
                }
                yield false;
            }
            case WideBoxLeft box ->
                tryMovingWideBox(currentPosition, direction, warehouse, new HashSet<>(), new HashMap<>());
            case WideBoxRight box ->
                tryMovingWideBox(currentPosition, direction, warehouse, new HashSet<>(), new HashMap<>());
            default -> false;
        };
    }

    boolean tryMovingWideBox(Pair<Integer, Integer> currentPosition, Direction direction,
            Warehouse warehouse, Set<Pair<Integer, Integer>> waiting,
            Map<Pair<Integer, Integer>, Boolean> resolved) {

        var currentTile = warehouse.get(currentPosition);

        var nextPosition = Pair.sum(currentPosition, direction.asPair());
        var nextTile = warehouse.get(nextPosition);

        if (nextTile instanceof Wall)
            return false;

        return switch (direction) {
            case LEFT, RIGHT -> {
                // For horizontal movement, the logic is the same as the one for small boxes
                if (nextTile instanceof EmptyTile
                        || ((nextTile instanceof WideBoxLeft || nextTile instanceof WideBoxRight)
                                && tryMovingWideBox(nextPosition, direction, warehouse, waiting, resolved))) {
                    warehouse.set(currentPosition, new EmptyTile());
                    warehouse.set(nextPosition, currentTile);
                    resolved.put(currentPosition, true);
                    yield true;
                }
                resolved.put(currentPosition, false);
                yield false;
            }
            case UP, DOWN -> {
                // For vertical movement it is necessary to check if the other side of the box
                // can move.
                var otherSide = switch (currentTile) {
                    case WideBoxLeft box -> Pair.sum(currentPosition, Direction.RIGHT.asPair());
                    case WideBoxRight box -> Pair.sum(currentPosition, Direction.LEFT.asPair());
                    default -> throw new IllegalStateException();
                };

                var warehouseCopy = warehouse.copy();

                // If the other side of the box is already awaiting for the result, just apply
                // the normal recursive check.
                if (waiting.contains(otherSide)) {
                    if (nextTile instanceof EmptyTile
                            || ((nextTile instanceof WideBoxLeft || nextTile instanceof WideBoxRight)
                                    && tryMovingWideBox(nextPosition, direction, warehouse, waiting, resolved))) {

                        warehouse.set(currentPosition, new EmptyTile());
                        warehouse.set(nextPosition, currentTile);

                        resolved.put(currentPosition, true);
                        yield true;
                    }

                    resolved.put(currentPosition, false);
                    yield false;

                } else if (resolved.values().contains(false)) {
                    // If any path failed, revert any modification
                    warehouse.set(warehouseCopy);
                    yield false;
                } else {
                    boolean result;

                    // Recursively check the both sides of the box.
                    waiting.add(currentPosition);
                    result = tryMovingWideBox(otherSide, direction, warehouse, waiting, resolved);
                    waiting.remove(currentPosition);

                    waiting.add(otherSide);
                    result &= tryMovingWideBox(currentPosition, direction, warehouse, waiting, resolved);
                    waiting.remove(otherSide);

                    if (!result)
                        warehouse.set(warehouseCopy);

                    yield result;
                }
            }
            default -> false;
        };
    }

    boolean canTileBeMoved(Pair<Integer, Integer> position, Direction direction, Warehouse warehouse) {
        Pair<Integer, Integer> currentPosition = position;
        while (true) {
            currentPosition = Pair.sum(currentPosition, direction.asPair());
            var currentTile = warehouse.get(currentPosition);

            if (currentTile instanceof EmptyTile)
                return true;
            if (currentTile instanceof Wall)
                return false;
        }
    }

    Pair<Warehouse, List<Direction>> parseInput(String input) {
        List<List<Tile>> tiles = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();

        input.lines()
                .filter(line -> !line.isBlank())
                .forEach(line -> {
                    char firstChar = line.charAt(0);

                    if (firstChar == '#')
                        tiles.add(parseMapLine(line));
                    else if (firstChar == '^'
                            || firstChar == '>'
                            || firstChar == 'v'
                            || firstChar == '<')
                        directions.addAll(parseDirectionLine(line));
                });

        return new Pair<>(new Warehouse(tiles), directions);
    }

    List<Tile> parseMapLine(String line) {
        return line.chars()
                .sequential()
                .mapToObj(ch -> (Tile) (switch (ch) {
                    case '#' -> new Wall();
                    case 'O' -> new Box();
                    case '@' -> new Robot();
                    case '.' -> new EmptyTile();
                    default -> throw new IllegalStateException();
                }))
                .collect(() -> new ArrayList<>(),
                        (a, b) -> a.add(b),
                        (a, b) -> a.addAll(b));
    }

    Pair<Warehouse, List<Direction>> parseInputWide(String input) {
        List<List<Tile>> tiles = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();

        input.lines()
                .filter(line -> !line.isBlank())
                .forEach(line -> {
                    char firstChar = line.charAt(0);

                    if (firstChar == '#')
                        tiles.add(parseMapLineWide(line));
                    else if (firstChar == '^'
                            || firstChar == '>'
                            || firstChar == 'v'
                            || firstChar == '<')
                        directions.addAll(parseDirectionLine(line));
                });

        return new Pair<>(new Warehouse(tiles), directions);
    }

    List<Tile> parseMapLineWide(String line) {
        ArrayList<Tile> result = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            result.addAll(switch (ch) {
                case '#' -> List.of(new Wall(), new Wall());
                case 'O' -> List.of(new WideBoxLeft(), new WideBoxRight());
                case '@' -> List.of(new Robot(), new EmptyTile());
                case '.' -> List.of(new EmptyTile(), new EmptyTile());
                default -> throw new IllegalStateException();
            });
        }

        return result;
    }

    List<Direction> parseDirectionLine(String line) {
        return line.chars()
                .sequential()
                .mapToObj(ch -> switch (ch) {
                    case '^' -> Direction.UP;
                    case '>' -> Direction.RIGHT;
                    case 'v' -> Direction.DOWN;
                    case '<' -> Direction.LEFT;
                    default -> throw new IllegalStateException();
                })
                .toList();
    }

    record Warehouse(List<List<Tile>> tiles) {
        Warehouse copy() {
            return new Warehouse(this.tiles().stream()
                    .map(list -> (List<Tile>) list.stream()
                            .map(Tile::copy)
                            .collect(() -> new ArrayList<Tile>(),
                                    (a, b) -> a.add(b),
                                    (a, b) -> a.addAll(b)))
                    .collect(() -> new ArrayList<List<Tile>>(),
                            (a, b) -> a.add(b),
                            (a, b) -> a.addAll(b)));
        }

        Tile get(Pair<Integer, Integer> position) {
            final int x = position.x();
            final int y = position.y();

            return this.tiles().get(y).get(x);
        }

        void set(Pair<Integer, Integer> position, Tile tile) {
            final int x = position.x();
            final int y = position.y();

            this.tiles().get(y).set(x, tile);
        }

        Optional<Pair<Integer, Integer>> findRobotPosition() {
            var rows = this.tiles().size();
            var columns = this.tiles().get(0).size();

            for (int y = 0; y < rows; ++y) {
                for (int x = 0; x < columns; ++x) {
                    var candidatePosition = new Pair<>(x, y);
                    if (get(candidatePosition) instanceof Robot)
                        return Optional.of(candidatePosition);
                }
            }

            return Optional.empty();
        }

        int sumOfBoxGPS() {

            int sum = 0;

            var rows = this.tiles().size();
            var columns = this.tiles().get(0).size();

            for (int y = 0; y < rows; ++y) {
                for (int x = 0; x < columns; ++x) {
                    var candidatePosition = new Pair<>(x, y);
                    var candidateTile = get(candidatePosition);
                    if (candidateTile instanceof Box || candidateTile instanceof WideBoxLeft)
                        sum += 100 * y + x;
                }
            }

            return sum;
        }

        void set(Warehouse other) {
            this.tiles.clear();
            this.tiles.addAll(other.tiles);
        }

        @Override
        public final String toString() {
            StringBuilder sb = new StringBuilder();

            var rows = this.tiles().size();
            var columns = this.tiles().get(0).size();

            for (int y = 0; y < rows; ++y) {
                StringBuilder lineBuilder = new StringBuilder();

                for (int x = 0; x < columns; ++x) {
                    var position = new Pair<>(x, y);
                    lineBuilder.append(get(position).type());
                }

                lineBuilder.append('\n');
                sb.append(lineBuilder);
            }

            return sb.toString();
        }
    }

    public record WideBoxLeft() implements Tile {
        public String type() {
            return "[";
        }

        public Tile copy() {
            return new WideBoxLeft();
        }
    }

    public record WideBoxRight() implements Tile {
        public String type() {
            return "]";
        }

        public Tile copy() {
            return new WideBoxRight();
        }
    }

    public record Box() implements Tile {
        public String type() {
            return "O";
        }

        public Box copy() {
            return new Box();
        }
    }

    public record Robot() implements Tile {
        public String type() {
            return "@";
        }

        public Tile copy() {
            return new Robot();
        }
    }

    public record Wall() implements Tile {
        public String type() {
            return "#";
        }

        public Tile copy() {
            return new Wall();
        }
    }

    public record EmptyTile() implements Tile {
        public String type() {
            return ".";
        }

        public Tile copy() {
            return new EmptyTile();
        }
    }

    sealed interface Tile permits Box, WideBoxLeft, WideBoxRight, Robot, Wall, EmptyTile {
        public String type();

        public Tile copy();
    }
}
