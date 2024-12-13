package com.adventofcode.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Ad-hoc implementation of 2x2 matrix to solve Day 13
 */
public class Matrix2x2 {
    private long[][] values;

    public Matrix2x2(long[][] values) {
        if (values.length != 2 || values[0].length != 2 || values[1].length != 2)
            throw new IllegalArgumentException("Only 2x2 matrix supported");
        this.values = new long[][] {
                { values[0][0], values[0][1] },
                { values[1][0], values[1][1] }
        };
    }

    public Matrix2x2(long a, long b, long c, long d) {
        long[] firstRow = { a, b };
        long[] secondRow = { c, d };

        this.values = new long[][] { firstRow, secondRow };
    }

    public static Matrix2x2 ofRows(Pair<Long, Long> firstRow, Pair<Long, Long> secondRow) {
        return new Matrix2x2(firstRow.x(), firstRow.y(), secondRow.x(), secondRow.y());
    }

    public static Matrix2x2 ofColumns(Pair<Long, Long> firstColumn, Pair<Long, Long> secondColumn) {
        return new Matrix2x2(firstColumn.x(), secondColumn.x(), firstColumn.y(), secondColumn.y());
    }

    public static Matrix2x2 identity() {
        return new Matrix2x2(1, 0, 0, 1);
    }

    public long get(int row, int column) {
        return values[row][column];
    }

    public long determinant() {
        var mainDiagonal = BigInteger.valueOf(this.values[0][0]).multiply(BigInteger.valueOf(this.values[1][1]));
        var antiDiagonal = BigInteger.valueOf(this.values[0][1]).multiply(BigInteger.valueOf(this.values[1][0]));

        var value = mainDiagonal.subtract(antiDiagonal);

        if (value.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            throw new Error("bruh");
        }

        return value.longValue();
    }

    public Matrix2x2 multiply(Matrix2x2 other) {
        return new Matrix2x2(
                values[0][0] * other.values[0][0] + values[0][1] * other.values[1][0],
                values[0][0] * other.values[0][1] + values[0][1] * other.values[1][1],
                values[1][0] * other.values[0][0] + values[1][1] * other.values[1][0],
                values[1][0] * other.values[0][1] + values[1][1] * other.values[1][1]);
    }

    public Pair<Long, Long> dot(Pair<Long, Long> vector) {
        return new Pair<>(
                values[0][0] * vector.x() + values[0][1] * vector.y(),
                values[1][0] * vector.x() + values[1][1] * vector.y());
    }

    public Optional<Pair<Long, Long>> solveLinearSystem(Pair<Long, Long> rightSide) {
        var determinant = determinant();

        if (determinant == 0)
            return Optional.empty();

        long detX = new Matrix2x2(
                rightSide.x(), values[0][1],
                rightSide.y(), values[1][1])
                .determinant();
        var x = detX / determinant;

        long detY = new Matrix2x2(
                values[0][0], rightSide.x(),
                values[1][0], rightSide.y())
                .determinant();
        var y = detY / determinant;

        return Optional.of(new Pair<>(x, y));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(values);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix2x2 other = (Matrix2x2) obj;
        if (!Arrays.deepEquals(values, other.values))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("""
                (%s, %s)
                (%s, %s)""",
                values[0][0], values[0][1],
                values[1][0], values[1][1]);
    }
}