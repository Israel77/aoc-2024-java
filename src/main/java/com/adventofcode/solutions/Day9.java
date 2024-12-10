package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public enum Day9 implements Solver<Long, Integer> {
    INSTANCE;

    @Override
    public Long solvePart1(String input) {
        Disk disk = parseInput(input);
        List<File> files = disk.files();
        List<Integer> freeSpace = disk.freeSpace();

        List<File> defragmentedFiles = new ArrayList<>();
        // Reverse sort by position
        files.sort((a, b) -> Integer.compare(b.pos, a.pos));
        boolean addRemaining = false;
        for (var file : files) {
            addRemaining = addRemaining || freeSpace.isEmpty() || freeSpace.get(0) >= files.size();
            if (addRemaining) {
                defragmentedFiles.add(file);
            } else {
                int newPos = freeSpace.removeFirst();
                File newFile = file.withPos(newPos);
                defragmentedFiles.add(newFile);
            }
        }

        return defragmentedFiles.stream()
                .mapToLong(File::getFileChecksum)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    Disk parseInput(String input) {
        List<File> files = new ArrayList<>();
        List<Integer> freeSpace = new ArrayList<>();

        int fileCount = 0;
        int freeSpaceCount = 0;
        int totalCount = 0;
        int fileId = 0;
        for (int i = 0; i < input.length(); ++i) {
            var ch = input.charAt(i);
            var amount = ch - 48;

            if (i % 2 == 0) {
                List<File> fileRange = generateFileRange(fileId, totalCount, amount);
                files.addAll(fileRange);
                fileCount += amount;
                fileId++;
            } else {
                freeSpace.addAll(IntStream
                        .range(totalCount, totalCount + amount)
                        .boxed()
                        .toList());
                freeSpaceCount += amount;
            }

            totalCount = fileCount + freeSpaceCount;
        }

        return new Disk(files, freeSpace, totalCount);
    }

    private static List<File> generateFileRange(int id, int startPosition, int amount) {
        List<File> result = new ArrayList<>();

        for (int i = 0; i < amount; ++i) {
            int pos = startPosition + i;
            result.add(new File(id, pos));
        }

        return result;
    }

    private record Disk(List<File> files, List<Integer> freeSpace, int size) {
    }

    private static class File {
        public int id;
        public int pos;

        public File(int id, int pos) {
            this.id = id;
            this.pos = pos;
        }

        public File withPos(int pos) {
            return new File(this.id, pos);
        }

        public long getFileChecksum() {
            return (long) this.id * (long) this.pos;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + pos;
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
            File other = (File) obj;
            if (id != other.id)
                return false;
            if (pos != other.pos)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "File [id=" + id + ", pos=" + pos + "]";
        }

    }
}
