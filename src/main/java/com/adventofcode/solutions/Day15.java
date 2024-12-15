package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
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
                        || nextTile instanceof Box && tryMoving(nextPosition, direction, warehouse)) {
                    warehouse.set(currentPosition, new EmptyTile());
                    warehouse.set(nextPosition, currentTile);
                    yield true;
                }
                yield false;
            }
            case Box box -> {
                if (nextTile instanceof EmptyTile
                        || nextTile instanceof Box && tryMoving(nextPosition, direction, warehouse)) {
                    warehouse.set(currentPosition, new EmptyTile());
                    warehouse.set(nextPosition, currentTile);
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
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
                    if (get(candidatePosition) instanceof Box)
                        sum += 100 * y + x;
                }
            }

            return sum;
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

    public record Box() implements Tile {
        public String type() {
            return "O";
        }
    }

    public record Robot() implements Tile {
        public String type() {
            return "@";
        }
    }

    public record Wall() implements Tile {
        public String type() {
            return "#";
        }
    }

    public record EmptyTile() implements Tile {
        public String type() {
            return ".";
        }
    }

    sealed interface Tile permits Box, Robot, Wall, EmptyTile {
        public String type();
    }
}
