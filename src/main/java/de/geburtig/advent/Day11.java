package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

public class Day11 extends DayBase {

    @Override
    public String resolvePuzzle1() throws Exception {
        KeepAwayGame game = new KeepAwayGame(createInputFileScanner("/input_day11.txt"));
        game.worryOperator = old -> old / 3;
        game.rounds(20);
        int result = game.calculateResult();
        return String.valueOf(result);
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        KeepAwayGame game = new KeepAwayGame(createInputFileScanner("/input_day11.txt"));
        game.rounds(10_000);
        int result = game.calculateResult();
        return String.valueOf(result);
    }

    static class KeepAwayGame {
        private final List<Monkey> monkeys = new ArrayList<>();

        LongUnaryOperator worryOperator = old -> old;

        private static final LongBinaryOperator add = Math::addExact;
        private static final LongBinaryOperator multiply = Math::multiplyExact;


        KeepAwayGame(final Scanner scanner) {
            scanner.useDelimiter("\n\nMonkey ");
            while (scanner.hasNext()) {
                String monkeySection = scanner.next();
                String[] lines = monkeySection.split("\n");

                List<Item> items = Arrays.stream(lines[1].substring(18).split(", ")).map(i -> new Item(Integer.parseInt(i))).toList();

                String substring = lines[2].substring(23);
                String[] part = substring.split(" ");
                LongUnaryOperator operation = new LongUnaryOperator() {
                    @Override
                    public long applyAsLong(long operand) {
                        long value = part[1].equals("old") ? operand : Long.parseLong(part[1]);
                        return switch (part[0]) {
                            case "+" -> add.applyAsLong(operand, value);
                            case "*" -> multiply.applyAsLong(operand, value);
                            default  -> throw new IllegalStateException("Don't know what to do with \"" + part[0] + "\"");
                        };
                    }
                };

                substring = lines[3].substring(8);
                if (!substring.startsWith("divisible by ")) {
                    throw new IllegalStateException("Don't know what to do with \"" + lines[3] + "\"");
                }
                int testValue = Integer.parseInt(lines[3].substring(21));
                int ifTrue = Integer.parseInt(lines[4].replace("    If true: throw to monkey ", ""));
                int ifFalse = Integer.parseInt(lines[5].replace("    If false: throw to monkey ", ""));
                LongFunction<Monkey> test = new LongFunction<>() {
                    @Override
                    public Monkey apply(long value) {
                        return (value % testValue == 0) ? monkeys.get(ifTrue) : monkeys.get(ifFalse);
                    }
                };

                Monkey monkey = new Monkey(this, operation, test);
                monkey.items.addAll(items);
                monkeys.add(monkey);
            }
        }

        void rounds(final int rounds) {
            IntStream.range(0, rounds).forEach(i -> round());
        }

        void round() {
            monkeys.forEach(Monkey::turn);
        }

        int calculateResult() {
            int[] ints = monkeys.stream().mapToInt(Monkey::getInspections).sorted().toArray();
            int[] relevant = monkeys.stream().mapToInt(Monkey::getInspections).sorted().skip(6).toArray();
            return relevant[0] * relevant[1];
        }
    }

    @RequiredArgsConstructor
    static class Monkey {

        private final KeepAwayGame game;

        private final List<Item> items = new ArrayList<>();
        private final LongUnaryOperator operation;
        private final LongFunction<Monkey> test;
        private @Getter int inspections = 0;

        void turn() {
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                ++inspections;

                // Inspect item and update worry level
                item.setWorryLevel(operation.applyAsLong(item.worryLevel));
                item.setWorryLevel(game.worryOperator.applyAsLong(item.worryLevel));

                // Test and throw item to other monkey
                Monkey targetMonkey = test.apply(item.worryLevel);
                targetMonkey.items.add(item);
                iterator.remove();
            }
        }
    }

    @Data
    @AllArgsConstructor
    static class Item {
        private long worryLevel;
    }
}
