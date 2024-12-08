package com.adventofcode.util;

public final class Constants {
    private Constants() {

    }

    public static enum Day {
        DAY_1, DAY_2, DAY_3, DAY_4, DAY_5, //
        DAY_6, DAY_7, DAY_8, DAY_9, DAY_10, //
        DAY_11, DAY_12, DAY_13, DAY_14, DAY_15, //
        DAY_16, DAY_17, DAY_18, DAY_19, DAY_20, //
        DAY_21, DAY_22, DAY_23, DAY_24, DAY_25;

        public static Day fromInteger(int dayNumber) {
            return switch (dayNumber) {
                case 1 -> Day.DAY_1;
                case 2 -> Day.DAY_2;
                case 3 -> Day.DAY_3;
                case 4 -> Day.DAY_4;
                case 5 -> Day.DAY_5;
                case 6 -> Day.DAY_6;
                case 7 -> Day.DAY_7;
                case 8 -> Day.DAY_8;
                case 9 -> Day.DAY_9;
                case 10 -> Day.DAY_10;
                case 11 -> Day.DAY_11;
                case 12 -> Day.DAY_12;
                case 13 -> Day.DAY_13;
                case 14 -> Day.DAY_14;
                case 15 -> Day.DAY_15;
                case 16 -> Day.DAY_16;
                case 17 -> Day.DAY_17;
                case 18 -> Day.DAY_18;
                case 19 -> Day.DAY_19;
                case 20 -> Day.DAY_20;
                case 21 -> Day.DAY_21;
                case 22 -> Day.DAY_22;
                case 23 -> Day.DAY_23;
                case 24 -> Day.DAY_24;
                case 25 -> Day.DAY_25;
                default -> {
                    throw new IllegalArgumentException(
                            "Invalid day number, expected value in the range 1-25, got "
                                    + dayNumber);
                }
            };

        }
    }

    public static enum Part {
        PART_1, PART_2;

        public static Part fromInteger(int partNumber) {
            return switch (partNumber) {
                case 1 -> Part.PART_1;
                case 2 -> Part.PART_2;
                default -> {
                    throw new IllegalArgumentException(
                            "Invalid part number, expected values 1 or 2, got " + partNumber);
                }
            };

        }
    }
}
