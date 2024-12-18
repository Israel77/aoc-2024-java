package com.adventofcode.solutions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Day17 implements Solver<String, BigInteger> {
    INSTANCE;

    @Override
    public String solvePart1(String input) {
        return parseInput(input)
                .run()
                .output()
                .map(x -> x.toString())
                .collect(Collectors.joining(","));
    }

    @Override
    public BigInteger solvePart2(String input) {
        BigInteger quine = parseInput(input).findQuine();
        return quine;
    }

    CPU parseInput(String input) {

        int registerA = 0;
        int registerB = 0;
        int registerC = 0;

        List<Integer> program = new ArrayList<>();

        var numberPattern = Pattern.compile("(\\d+)");

        var inputLines = input.lines().toList();

        // So simple to parse that I will not even bother
        // setting up a loop for the registers(the temptation
        // was to just enter the number manually)

        // Parse register A
        var currentLine = inputLines.get(0);
        Matcher matcher = numberPattern.matcher(currentLine);
        matcher.find();

        registerA = Integer.parseInt(matcher.group());

        // Parse register B
        currentLine = inputLines.get(1);
        matcher = numberPattern.matcher(currentLine);
        matcher.find();

        registerB = Integer.parseInt(matcher.group());

        // Parse register C
        currentLine = inputLines.get(2);
        matcher = numberPattern.matcher(currentLine);
        matcher.find();

        registerC = Integer.parseInt(matcher.group());

        // Line 3 is blank
        currentLine = inputLines.get(4);
        matcher = numberPattern.matcher(currentLine);
        int index = 0;
        while (matcher.find(index)) {
            program.add(Integer.parseInt(matcher.group()));
            index = matcher.end();
        }

        return new CPU(registerA, registerB, registerC, program);
    }

    public static class CPU implements Cloneable {

        int registerA;
        int registerB;
        int registerC;

        int instructionPointer;

        List<Integer> outputList = new ArrayList<>();

        List<Integer> program;

        public CPU(int registerA, int registerB, int registerC, List<Integer> program) {

            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = program;

            this.instructionPointer = 0;
        }

        public CPU run() {

            final int programSize = program.size();

            while (instructionPointer < programSize - 1) {
                step();
            }

            return this;
        }

        public BigInteger findQuine() {

            int programSize = program.size();
            List<Integer> accumulator = new ArrayList<>();

            for (int i = 0; i < programSize; ++i) {

                var currentGoal = program.get(i);

                for (int candidateA = 0;; candidateA++) {
                    var cpu = new CPU(candidateA, 0, 0, new ArrayList<>(program));

                    cpu.run();

                    if (cpu.output().findFirst().get() % 8 == currentGoal) {
                        accumulator.add(candidateA);
                        break;
                    }
                }

            }

            BigInteger result = BigInteger.valueOf(0);

            final Map<Integer, BigInteger> powersOf8 = new HashMap<>();
            powersOf8.put(0, BigInteger.valueOf(1));

            for (int i = 0; i < accumulator.size(); ++i) {

                var power = powersOf8.computeIfAbsent(i, v -> {
                    return powersOf8.get(v - 1).multiply(BigInteger.valueOf(8));
                });

                result = result.add(
                        BigInteger.valueOf(accumulator.get(i)).multiply(
                                power));
            }

            return result;
        }

        private CPU step() {
            Instruction instruction = Instruction.valueOf(program.get(instructionPointer));
            int operand = program.get(instructionPointer + 1);

            if (parseInstruction(instruction, operand))
                instructionPointer += 2;

            return this;
        }

        public Stream<Integer> output() {
            return outputList.stream().sequential();
        }

        private boolean parseInstruction(Instruction instruction, int operand) {
            return switch (instruction) {
                case ADV -> {
                    registerA >>= comboValue(operand);
                    yield true;
                }
                case BXL -> {
                    registerB ^= operand;
                    yield true;
                }
                case BST -> {
                    registerB = comboValue(operand) & 7;
                    yield true;
                }
                case JNZ -> {
                    if (registerA == 0)
                        yield true;

                    instructionPointer = operand;
                    yield false;
                }
                case BXC -> {
                    registerB ^= registerC;
                    yield true;
                }
                case OUT -> {
                    outputList.add(comboValue(operand) & 7);
                    yield true;
                }
                case BDV -> {
                    registerB = registerA >> comboValue(operand);
                    yield true;
                }
                case CDV -> {
                    registerC = registerA >> comboValue(operand);
                    yield true;
                }
            };
        }

        private int comboValue(int comboOperand) {
            return switch (comboOperand) {
                case 0, 1, 2, 3 -> comboOperand;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> throw new IllegalArgumentException("Invalid combo operand");
            };
        }

        @Override
        public CPU clone() {
            return new CPU(registerA, registerB, registerC, new ArrayList<>(program));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("CPU\n");
            sb.append("registerA=").append(registerA).append("\n");
            sb.append("registerB=").append(registerB).append("\n");
            sb.append("registerC=").append(registerC).append("\n");
            sb.append("instructionPointer=").append(instructionPointer).append("\n");
            sb.append("program=").append(program).append("\n");
            sb.append("outputStream=").append(output()
                    .map(x -> x.toString())
                    .collect(Collectors.joining(",")));

            return sb.toString();
        }
    }

    enum Instruction {
        ADV,
        BXL,
        BST,
        JNZ,
        BXC,
        OUT,
        BDV,
        CDV;

        public static Instruction valueOf(int opCode) {
            return switch (opCode) {
                case 0 -> ADV;
                case 1 -> BXL;
                case 2 -> BST;
                case 3 -> JNZ;
                case 4 -> BXC;
                case 5 -> OUT;
                case 6 -> BDV;
                case 7 -> CDV;
                default -> throw new IllegalArgumentException("Invalid opcode");
            };
        }
    }

}