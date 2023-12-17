package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/9
 */
public class Day9 {
    public static void main(final String[] args) {
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day9_example.txt", Day9.class);
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day9.txt", Day9.class);

        // Puzzle 1
        long result = 0;
        for (String line : input) {
            Sequence sequence = Sequence.of(line);
            sequence.generateNextNumber();
//            System.out.println(sequence.toOutputString());
            result += sequence.numbers.getLast().value;
        }
        System.out.println("Result 1: " + result);

        // Puzzle 2
        result = 0;
        for (String line : input) {
            Sequence sequence = Sequence.of(line);
            sequence.generatePreviousNumber();
//            System.out.println(sequence.toOutputString());
            result += sequence.numbers.getFirst().value;
        }
        // 209 too low
        System.out.println("Result 2: " + result);
    }

    @Data
    static class Sequence {
        private final List<Position> numbers;
        private final Sequence superSequence;
        private final Sequence subSequence;
        private Boolean allZero = null;

        static Sequence of(final String line) {
            List<Position> list = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).mapToObj(Position::new).toList();
            return new Sequence(list, null);
        }

        private Sequence(final List<Position> numbers, final Sequence superSequence) {
            this.superSequence = superSequence;
            this.numbers = new ArrayList<>(numbers);
            this.subSequence = createSubSequenceIfNeeded(this);
        }

        private static Sequence createSubSequenceIfNeeded(final Sequence sequence) {
            if (!sequence.allZero()) {
                List<Position> newSequence = new ArrayList<>();
                for (int i = 1; i < sequence.getNumbers().size(); ++i) {
                    Position pos = sequence.getNumbers().get(i);
                    pos.setPre1(sequence.getNumbers().get(i - 1));
                    pos.setPre2(new Position(pos.value - pos.pre1.value));
                    newSequence.add(pos.getPre2());
                }
                return new Sequence(newSequence, sequence);
            }
            return null;
        }

        boolean allZero() {
            if (allZero == null) {
                allZero = numbers.stream().mapToLong(Position::getValue).allMatch(v -> v == 0);
            }
            return allZero;
        }

        public String toOutputString() {
            String indent = "";
            Sequence sequence = this;
            StringBuilder b = new StringBuilder();
            do {
                b.append(indent).append(sequence);
                sequence = sequence.getSubSequence();
                indent += "  ";
                b.append("\n");
            } while (sequence != null);
            return b.toString();
        }

        @Override
        public String toString() {
            return String.format("%-4s".repeat(numbers.size()), numbers.toArray());
        }

        public void generateNextNumber() {
            if (subSequence == null) {
                numbers.add(new Position(0));
            } else {
                subSequence.generateNextNumber();
                Position pre1 = numbers.getLast();
                Position pre2 = subSequence.numbers.getLast();
                numbers.add(new Position(pre1, pre2));
            }
        }

        public void generatePreviousNumber() {
            if (subSequence == null) {
                numbers.add(0, new Position(0));
            } else {
                subSequence.generatePreviousNumber();
                numbers.add(0, new Position(numbers.getFirst().value - subSequence.numbers.getFirst().value));
            }
        }
    }

    @Data
    @RequiredArgsConstructor
    static class Position {
        final long value;
        Position pre1, pre2;

        Position(final @NonNull Position pre1, final @NonNull Position pre2) {
            this.value = pre1.value + pre2.value;
            this.pre1 = pre1;
            this.pre2 = pre2;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
