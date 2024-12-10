package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.adventofcode.util.Pair;

public class Day8 implements Solver<Integer, Integer> {
    int numOfRows;
    int numOfColumns;

    @Override
    public Integer solvePart1(String input) {
        var antiNodes = getAntiNodes(parseInput(input));

        return antiNodes.size();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    void printWithAntiNodes(String input) {
        var antennas = parseInput(input);
        var antiNodes = getAntiNodes(antennas);

        for (int j = 0; j < numOfRows; ++j) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < numOfColumns; ++i) {
                final int x = i;
                final int y = j;
                var antiNode = antiNodes.stream()
                        .filter(coords -> x == coords.first() && coords.second() == y)
                        .findAny();
                var antenna = antennas.stream()
                        .filter(a -> a.posX() == x && a.posY() == y)
                        .findAny();

                if (antenna.isPresent()) {
                    line.append(antenna.get().frequency());
                } else if (antiNode.isPresent()) {
                    line.append('#');
                } else {
                    line.append('.');
                }
            }
            System.out.println(line.toString());
        }
    }

    List<Pair<Integer, Integer>> getAntiNodes(List<Antenna> antennas) {
        Set<Pair<Integer, Integer>> antiNodes = new HashSet<>();

        Map<Character, List<Antenna>> antennasPerFrequency = antennas.stream()
                .collect(Collectors.groupingBy(antenna -> antenna.frequency));

        antennasPerFrequency.forEach((f, a) -> {
            var localAntennas = new ArrayList<>(a);
            while (!localAntennas.isEmpty()) {
                var antenna = localAntennas.removeFirst();
                for (var other : localAntennas) {

                    var dx1 = other.posX - antenna.posX;
                    var dx2 = antenna.posX - other.posX;
                    var dy1 = other.posY - antenna.posY;
                    var dy2 = antenna.posY - other.posY;

                    var node1 = new Pair<>(antenna.posX - dx1, antenna.posY - dy1);
                    var node2 = new Pair<>(other.posX - dx2, other.posY - dy2);
                    antiNodes.add(node1);
                    antiNodes.add(node2);
                }
            }
        });

        return antiNodes.stream()
                .filter(coords -> 0 <= coords.first()
                        && coords.first() < numOfColumns
                        && 0 <= coords.second()
                        && coords.second() < numOfRows)
                .sorted((a, b) -> a.first().compareTo(b.first()))
                .toList();
    }

    List<Antenna> parseInput(String input) {
        List<Antenna> result = new ArrayList<>();

        List<String> lines = input.lines().toList();
        this.numOfRows = lines.size();
        this.numOfColumns = lines.get(0).length();

        for (int y = 0; y < numOfRows; ++y) {
            for (int x = 0; x < numOfColumns; ++x) {
                char frequency = lines.get(y).charAt(x);
                if (frequency != '.')
                    result.add(new Antenna(frequency, x, y));
            }
        }

        return result;
    }

    private record Antenna(char frequency, int posX, int posY) {
    }

}
