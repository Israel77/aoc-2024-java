package com.adventofcode.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adventofcode.routes.RequestBean;
import com.adventofcode.solutions.Day1;
import com.adventofcode.solutions.Day10;
import com.adventofcode.solutions.Day11;
import com.adventofcode.solutions.Day12;
import com.adventofcode.solutions.Day13;
import com.adventofcode.solutions.Day14;
import com.adventofcode.solutions.Day15;
import com.adventofcode.solutions.Day2;
import com.adventofcode.solutions.Day3;
import com.adventofcode.solutions.Day4;
import com.adventofcode.solutions.Day5;
import com.adventofcode.solutions.Day6;
import com.adventofcode.solutions.Day7;
import com.adventofcode.solutions.Day8;
import com.adventofcode.solutions.Day9;
import com.adventofcode.solutions.Solver;
import com.adventofcode.util.Pair;
import com.adventofcode.util.Constants.Day;
import com.adventofcode.util.Constants.Part;

public class SolverService {
    Solver<?, ?> solverImpl;
    Day day;
    Part part;
    String input;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public SolverService(RequestBean request) throws FileNotFoundException {
        readInputFromInputStream(
                new FileInputStream(request.getInputPart().uploadedFile().toFile()));

        this.day = Day.fromInteger(request.getDayNumber());
        this.part = Part.fromInteger(request.getPartNumber());
        this.solverImpl = switch (day) {
            case DAY_1 -> Day1.INSTANCE;
            case DAY_2 -> Day2.INSTANCE;
            case DAY_3 -> Day3.INSTANCE;
            case DAY_4 -> Day4.INSTANCE;
            case DAY_5 -> Day5.INSTANCE;
            case DAY_6 -> new Day6();
            case DAY_7 -> Day7.INSTANCE;
            case DAY_8 -> new Day8();
            case DAY_9 -> Day9.INSTANCE;
            case DAY_10 -> Day10.INSTANCE;
            case DAY_11 -> Day11.INSTANCE;
            case DAY_12 -> Day12.INSTANCE;
            case DAY_13 -> Day13.INSTANCE;
            case DAY_14 -> new Day14(new Pair<>(101, 103),
                    request.getDay14IterationStart().orElse(0),
                    request.getDay14IterationEnd().orElse(1500),
                    false || request.getDay14Visualize());
            case DAY_15 -> Day15.INSTANCE;
            default -> throw new UnsupportedOperationException("Solution not implemented yet");
        };
    }

    public String solve() {
        long startTime = System.nanoTime();

        var value = switch (part) {
            case PART_1 -> solverImpl.solvePart1(input);
            case PART_2 -> solverImpl.solvePart2(input);
        };

        long endTime = System.nanoTime();

        logger.info("{}, {}: \n Took {} nanoseconds to run", day, part,
                NumberFormat.getInstance().format(endTime - startTime));

        return value.toString();
    }

    private void readInputFromInputStream(InputStream inputStream) {
        var stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Could not read the input file.\n" + e.getLocalizedMessage());
        }

        this.input = stringBuilder.toString();
    }
}
