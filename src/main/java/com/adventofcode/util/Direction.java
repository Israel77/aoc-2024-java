package com.adventofcode.util;

/**
 * Represents a direction in 2D coordinates
 */
public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    /**
     * Returns a pair representing the unit 2d vector
     * associated with this direction.
     * Pair.first is the x coordinate.
     * Pair.second is the y coordinate.
     * 
     * x is positive towards right.
     * y is positive towards down.
     * 
     * @return Unit vector representing the direction
     */
    public Pair<Integer, Integer> asPair() {
        return switch (this) {
            case UP -> new Pair<>(0, -1);
            case RIGHT -> new Pair<>(1, 0);
            case DOWN -> new Pair<>(0, 1);
            case LEFT -> new Pair<>(-1, 0);
        };
    }

    public Direction rotateClockwise() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }

    public Direction rotateCounterClockwise() {
        return switch (this) {
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            case RIGHT -> UP;
        };
    }

    public Direction rotate180() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public boolean isPerpendicular(Direction other) {
        return switch (this) {
            case UP, DOWN -> other == LEFT || other == RIGHT;
            case LEFT, RIGHT -> other == UP || other == DOWN;
        };
    }
}
