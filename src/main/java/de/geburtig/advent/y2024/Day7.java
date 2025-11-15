package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.advent.util.Permutation;
import de.geburtig.utils.StringUtil;
import de.geburtig.utils.types.Zeichensystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {

    public static void main(final String[] args) throws Exception {
        List<Input> inputList = resolveInput("input_day7.txt");
//        List<Input> inputList = resolveInput("input_day7_example.txt");

//        long result = solve1(inputList);
        long result = solve2(inputList);
        System.out.println("Final result: " + result);
    }

    static long solve1(final List<Input> inputList) {
        long result = 0;
        for (Input input : inputList) {
            if (check1(input)) {
                result += input.value();
            }
        }
        return result;
    }

    static long solve2(final List<Input> inputList) {
        long result = 0;
        for (Input input : inputList) {
            if (check1(input)) {
                result += input.value();
            } else if (check2(input)) {
                // Extended for part two
                result += input.value();
//            } else {
//                System.out.println("Not fitting: " + input);
            }
        }
        return result;
    }

    private static boolean check1(final Input input) {
        for (int x = 0; x < (int) Math.pow(2, input.numbers().size() - 1); ++x) {
            char[] bits = StringUtil.fillLeft(Integer.toBinaryString(x), "0", input.numbers().size() - 1).toCharArray();
            char[] operators = new char[input.numbers().size() - 1];
            for (int i = 0; i < bits.length; i++) {
                operators[i] = bits[i] == '0' ? '+' : '*';
            }
            Equasion equasion = new Equasion(input.numbers(), operators);
            long result = equasion.resolve();

            if (result == input.value()) {
//                System.out.println(equasion + "=" + result);
                return true;
            } else {
//                System.out.println("Not fitting (" + input + "): " + equasion);
            }
        }
//        System.out.println("No solve for: " + input.value());
        return false;
    }

    private static boolean check2(final Input input) {
        char[] zeichensatz = {'+', '*', '|'};
        Zeichensystem system = new Zeichensystem(zeichensatz);

        int anzahlOperatoren = input.numbers().size() - 1;
        Zeichensystem.Wert wert = system.valueOf(0);
        do {
            char[] operators = wert.withTrailingZeros(anzahlOperatoren).toCharArray();

            Equasion equasion = new Equasion(input.numbers(), operators);
            long result = equasion.resolve();

            if (result == input.value()) {
//                System.out.println(equasion + "=" + result);
                return true;
            }
            wert = wert.inc();
        } while (wert.getString().length() <= anzahlOperatoren);
        return false;
    }

    record Input(long value, List<Long> numbers) {}

    record Equasion(List<Long> numbers, char[] operators) {
        long resolve() {
            long result = numbers.getFirst();
            for (int i = 0; i < operators.length; i++) {
                switch (operators[i]) {
                    case '+': result += numbers.get(i + 1); break;
                    case '*': result *= numbers.get(i + 1); break;
                    case '|': result = Long.parseLong(String.valueOf(result) + numbers.get(i + 1)); break;
                }
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < operators.length; i++) {
                b.append(numbers.get(i)).append(operators[i]);
            }
            b.append(numbers.getLast());
            return b.toString();
        }
    }

    private static List<Input> resolveInput(final String fileName) {
        List<String> inputLines = InputResolver.fetchLinesFromInputFile(fileName, Day7.class);
        List<Input> result = new ArrayList<>(inputLines.size());
        inputLines.forEach(line -> {
            String[] split = line.split(": ");
            long value = Long.parseLong(split[0]);
            List<Long> numbers = Arrays.stream(split[1].split(" ")).map(Long::parseLong).toList();
            result.add(new Input(value, numbers));
        });
        return result;
    }
}
