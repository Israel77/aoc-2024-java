package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.adventofcode.util.Functions;

public enum Day11 implements Solver<Integer, Long> {
    INSTANCE;

    @Override
    public Integer solvePart1(String input) {
        var stones = parseInput(input);
        Blinker blinker = new Blinker(stones, true);

        return blinker.toStream()
                // Get to the 25th iteration
                .skip(24)
                .findFirst()
                .map(List::size)
                .orElse(0);
    }

    @Override
    public Long solvePart2(String input) {
        var stones = parseInput(input);
        SmartBlinker blinker = new SmartBlinker(stones);

        blinker.blink(75);

        return blinker.countStones();
    }

    List<Integer> parseInput(String input) {
        return Arrays.stream(input.split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }

    private static class SmartBlinker {
        Map<Long, List<Long>> memo = new HashMap<>();
        Map<Long, Long> stoneCounter = new HashMap<>();

        SmartBlinker(List<Integer> stones) {
            for (long stone : stones) {
                addToCounter(stoneCounter, stone, 1);
            }
        }

        public long countStones() {
            return stoneCounter.values().stream().reduce(0l, Long::sum);
        }

        public void blink(int iterations) {
            for (int i = 0; i < iterations; ++i) {
                blink();
            }
        }

        public void blink() {
            Map<Long, Long> newCounter = new HashMap<>();

            for (var entry : stoneCounter.entrySet()) {
                long stone = entry.getKey();
                long currentCount = entry.getValue();

                for (var newStone : generateNewStones(stone)) {
                    addToCounter(newCounter, newStone, currentCount);
                }
            }
            this.stoneCounter = newCounter;
        }

        List<Long> generateNewStones(Long stone) {
            return memo.computeIfAbsent(stone, value -> {
                if (value == 0) {
                    return List.of(1L);
                } else if (value.toString().length() % 2 == 0) {
                    var valueString = value.toString();
                    var length = valueString.length();

                    var firstHalf = Long.valueOf(valueString.substring(0, length / 2));
                    var secondHalf = Long.valueOf(valueString.substring(length / 2));

                    return List.of(firstHalf, secondHalf);
                } else {
                    return List.of(value * 2024L);
                }
            });
        }

        private static void addToCounter(Map<Long, Long> counter,
                long stone,
                long amount) {
            counter.put(stone, counter.getOrDefault(stone, 0l) + amount);
        }

    }

    private static class Blinker implements Spliterator<List<Long>> {
        List<Long> stones;
        final boolean guaranteeOrder;
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        public Blinker(List<Integer> stones, boolean guaranteeOrder) {
            if (stones == null) {
                this.stones = List.of();
            } else {
                this.stones = stones.stream().map(v -> (long) v).toList();
            }
            this.guaranteeOrder = guaranteeOrder;

        }

        public Stream<List<Long>> toStream() {
            return StreamSupport.stream(this, false);
        }

        private void blink() {
            final List<Long> newStones = new ArrayList<>(2 * this.stones.size());
            // Although the problem description states that the
            // order should never change, part 1 only requires
            // the total count, so using a parallel stream
            // should not alter the final result.
            Consumer<Long> task = value -> {
                if (value == 0) {
                    newStones.add(1L);
                } else if (value.toString().length() % 2 == 0) {
                    var valueString = value.toString();
                    var length = valueString.length();

                    var firstHalf = Long.valueOf(valueString.substring(0, length / 2));
                    var secondHalf = Long.valueOf(valueString.substring(length / 2));

                    newStones.add(firstHalf);
                    newStones.add(secondHalf);
                } else {
                    newStones.add(value * 2024L);
                }
            };

            if (guaranteeOrder) {
                stones.stream().forEach(task);
            } else {
                // FIXME: There is some bug when trying to run in parallel, though
                // both using parallelStream() or implementing using completable
                // futures does not work, and return random results each time.
                // Must investigate further later.
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                for (long value : stones) {
                    futures.add(CompletableFuture.runAsync(() -> task.accept(value), executor));
                }
                Functions.joinAllAndClear(futures);
            }
            this.stones = List.copyOf(newStones);
        }

        @Override
        public boolean tryAdvance(Consumer<? super List<Long>> action) {
            blink();
            action.accept(stones);
            // Infinite iterator
            return true;
        }

        @Override
        public Spliterator<List<Long>> trySplit() {
            // Sequential iterator
            return null;
        }

        @Override
        public int characteristics() {
            return NONNULL;
        }

        @Override
        public long estimateSize() {

            return Long.MAX_VALUE;
        }
    }
}
