package com.adventofcode.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.adventofcode.util.MutableIntRange;

public enum Day9 implements Solver<Long, Long> {
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
    public Long solvePart2(String input) {
        DiskRanges disk = parseInputAsRanges(input);
        List<File> files = disk.files();
        List<MutableIntRange> freeSpace = disk.freeSpace();

        List<File> compressedFiles = new ArrayList<>(files.size());
        // Iterate over the ids starting from the last
        for (int id = files.stream()
                .mapToInt(File::id)
                .max()
                .orElseGet(() -> 0); id >= 0; --id) {
            // Declare as final to allow lambda shenanigans.
            final int currentId = id;
            var file = files.stream()
                    .filter(f -> f.id() == currentId)
                    .findFirst()
                    .get();

            boolean canMoveFile = false;
            for (MutableIntRange freeRange : freeSpace) {
                if (freeRange.size() >= file.size() && freeRange.start() < file.startPos()) {
                    canMoveFile = true;
                    // Move file if there is space available
                    compressedFiles.add(file.withPos(freeRange.start()));
                    freeRange.removeFromStart(file.size());
                    break;
                }
            }

            if (!canMoveFile) {
                // If no space was available, copy the
                // file unchanged on the output.
                compressedFiles.add(file);
            }
        }

        return compressedFiles.parallelStream()
                .mapToLong(File::getFileChecksum)
                .sum();
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

    DiskRanges parseInputAsRanges(String input) {
        List<File> files = new ArrayList<>();
        List<MutableIntRange> freeSpace = new ArrayList<>();

        int fileCount = 0;
        int freeSpaceCount = 0;
        int totalCount = 0;
        int fileId = 0;
        for (int i = 0; i < input.length(); ++i) {
            var ch = input.charAt(i);
            var amount = ch - 48;

            if (i % 2 == 0) {
                files.add(new File(fileId, totalCount, amount));
                fileCount += amount;
                fileId++;
            } else {
                freeSpace.add(new MutableIntRange(totalCount, totalCount + amount));
                freeSpaceCount += amount;
            }

            totalCount = fileCount + freeSpaceCount;
        }

        return new DiskRanges(files, freeSpace, totalCount);
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

    private record DiskRanges(List<File> files, List<MutableIntRange> freeSpace, int size) {
    }

    private record File(int id, int startPos, int size) {
        public File withPos(int newStartPos) {
            return new File(id, newStartPos, size);
        }

        public long getFileChecksum() {
            return LongStream.range((long) startPos, (long) (startPos + size))
                    .map(pos -> pos * (long) id)
                    .sum();
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
