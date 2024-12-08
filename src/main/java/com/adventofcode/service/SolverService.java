package com.adventofcode.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.adventofcode.solutions.Day1;
import com.adventofcode.solutions.Solver;
import com.adventofcode.util.Constants.Day;
import com.adventofcode.util.Constants.Part;

public class SolverService {
    Solver solverImpl;
    Part part;
    String input;

    public SolverService(Day day, Part part, InputStream inputStream) {
        readInputFromInputStream(inputStream);

        this.part = part;
        this.solverImpl = switch (day) {
            case DAY_1 -> Day1.INSTANCE;
            default -> throw new UnsupportedOperationException("Solution not implemented yet");
        };
    }

    public String solve() {
        return switch (part) {
            case PART_1 -> solverImpl.solvePart1(input);
            case PART_2 -> solverImpl.solvePart2(input);
        };
    }

    private void readInputFromInputStream(InputStream inputStream) {
        var stringBuilder = new StringBuilder();

        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
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
