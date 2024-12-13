package com.adventofcode.util;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

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
