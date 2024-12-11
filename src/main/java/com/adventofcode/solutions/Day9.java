package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public enum Day9 implements Solver<Long, Integer> {
    INSTANCE;

    @Override
    public Long solvePart1(String input) {
        Disk disk = parseInput(input);
        List<FileBlock> fileBlocks = disk.fileBlocks();
        List<Integer> freeSpace = disk.freeSpace();

        List<FileBlock> compressedFileBlocks = new ArrayList<>();
        // Reverse sort by position
        fileBlocks.sort((a, b) -> Integer.compare(b.pos, a.pos));
        boolean addRemaining = false;
        for (var fileBlock : fileBlocks) {
            addRemaining = addRemaining || freeSpace.isEmpty() || freeSpace.get(0) >= fileBlocks.size();
            if (addRemaining) {
                compressedFileBlocks.add(fileBlock);
            } else {
                int newPos = freeSpace.removeFirst();
                FileBlock newFile = fileBlock.withPos(newPos);
                compressedFileBlocks.add(newFile);
            }
        }

        return compressedFileBlocks.stream()
                .mapToLong(FileBlock::getFileChecksum)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        // TODO Auto-generated method stub
        return Solver.super.solvePart2(input);
    }

    Disk parseInput(String input) {
        List<FileBlock> files = new ArrayList<>();
        List<Integer> freeSpace = new ArrayList<>();

        int fileCount = 0;
        int freeSpaceCount = 0;
        int totalCount = 0;
        int fileId = 0;
        for (int i = 0; i < input.length(); ++i) {
            var ch = input.charAt(i);
            var amount = ch - 48;

            if (i % 2 == 0) {
                List<FileBlock> fileRange = generateFileBlockRange(fileId, totalCount, amount);
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

    private static List<FileBlock> generateFileBlockRange(int id, int startPosition, int amount) {
        List<FileBlock> result = new ArrayList<>();

        for (int i = 0; i < amount; ++i) {
            int pos = startPosition + i;
            result.add(new FileBlock(id, pos));
        }

        return result;
    }

    private record Disk(List<FileBlock> fileBlocks, List<Integer> freeSpace, int size) {
    }

    private static class MutableRange {
        private int _start;
        private int _end;

        public MutableRange(int start, int end) {
            if (_start > _end)
                throw new IllegalArgumentException("Range end cannot be smaller than start");
            this._start = start;
            this._end = end;
        }

        public int size() {
            return _start - _end + 1;
        }

        public int start() {
            return this._start;
        }

        public int end() {
            return this._end;
        }

        public void removeFromStart(int amount) {
            if (amount > size()) {
                throw new IllegalArgumentException(
                        String.format("Cannot remove %s from range with size %s", amount, size()));
            }
            this._start += amount;
        }

        public void removeFromEnd(int amount) {
            if (amount > size()) {
                throw new IllegalArgumentException(
                        String.format("Cannot remove %s from range with size %s", amount, size()));
            }
            this._end -= amount;

        }

        @Override
        public String toString() {
            return "Range [start=" + _start + ", end=" + _end + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + _start;
            result = prime * result + _end;
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
            MutableRange other = (MutableRange) obj;
            if (_start != other._start)
                return false;
            if (_end != other._end)
                return false;
            return true;
        }
    }

    private static class FileBlock {
        public int id;
        public int pos;

        public FileBlock(int id, int pos) {
            this.id = id;
            this.pos = pos;
        }

        public FileBlock withPos(int pos) {
            return new FileBlock(this.id, pos);
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
            FileBlock other = (FileBlock) obj;
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
