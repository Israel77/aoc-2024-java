package com.adventofcode.solutions;

import java.util.HashSet;
import java.util.Set;

import com.adventofcode.util.Pair;
import com.adventofcode.util.Triple;

public class Day6 implements Solver<Integer, Integer> {
    // Parsed from input
    Pair<Integer, Integer> initialPosition;
    Set<Pair<Integer, Integer>> obstacles;
    private int numOfRows;
    private int numOfColumns;

    // Current state
    Pair<Integer, Integer> currentPosition;
    Direction direction;

    @Override
    public Integer solvePart1(String input) {
        parseInput(input);

        this.currentPosition = this.initialPosition;
        Set<Pair<Integer, Integer>> visited = new HashSet<>();

        while (0 <= this.currentPosition.first()
                && this.currentPosition.first() < numOfColumns
                && 0 <= this.currentPosition.second()
                && this.currentPosition.second() < numOfRows) {
            visited.add(currentPosition);
            updatePosition();
        }

        return visited.size();
    }

    @Override
    public Integer solvePart2(String input) {
        parseInput(input);
        this.currentPosition = this.initialPosition;
        int count = 0;

        for (int x = 0; x < numOfColumns; x++) {
            for (int y = 0; y < numOfRows; y++) {
                var newObstacle = new Pair<>(x, y);
                count += checkIfLoops(newObstacle) ? 1 : 0;
                reset();
            }
        }

        return count;
    }

    boolean checkIfLoops(Pair<Integer, Integer> newObstacle) {
        // NOTE: ALWAYS RESET BEFORE ANY RETURN
        Set<Pair<Integer, Integer>> previousObstacles = new HashSet<>(obstacles);

        this.obstacles.add(newObstacle);
        Set<Triple<Integer, Integer, Direction>> visitedWithDirection = new HashSet<>();

        while (0 <= this.currentPosition.first()
                && this.currentPosition.first() < numOfColumns
                && 0 <= this.currentPosition.second()
                && this.currentPosition.second() < numOfRows) {
            var currentState = new Triple<>(currentPosition.first(), currentPosition.second(), this.direction);

            if (visitedWithDirection.contains(currentState)) {
                this.obstacles = previousObstacles;
                return true;
            }

            visitedWithDirection.add(currentState);
            updatePosition();
        }

        this.obstacles = previousObstacles;
        return false;
    }

    void reset() {
        this.currentPosition = this.initialPosition;
        this.direction = Direction.UP;
    };

    void updatePosition() {
        var directionAsPair = this.direction.asPair();
        var nextPosition = new Pair<>(this.currentPosition.first() + directionAsPair.first(),
                this.currentPosition.second() + directionAsPair.second());

        if (obstacles.contains(nextPosition)) {
            this.direction = this.direction.rotateClockwise();
            updatePosition();
        } else {
            this.currentPosition = nextPosition;
        }
    }

    void parseInput(String input) {
        this.obstacles = new HashSet<>();
        final var lines = input.lines().toList();
        this.numOfRows = lines.size();
        this.numOfColumns = lines.get(0).length();
        this.direction = Direction.UP;

        for (int y = 0; y < numOfRows; y++) {
            for (int x = 0; x < numOfColumns; x++) {
                switch (lines.get(y).charAt(x)) {
                    case '^':
                        this.initialPosition = new Pair<>(x, y);
                        break;
                    case '#':
                        this.obstacles.add(new Pair<>(x, y));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        Pair<Integer, Integer> asPair() {
            return switch (this) {
                case UP -> new Pair<>(0, -1);
                case RIGHT -> new Pair<>(1, 0);
                case DOWN -> new Pair<>(0, 1);
                case LEFT -> new Pair<>(-1, 0);
            };
        }

        Direction rotateClockwise() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };

        }
    }
}
