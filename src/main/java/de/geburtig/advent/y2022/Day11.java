package de.geburtig.advent.y2022;

import de.geburtig.advent.base.DayBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

public class Day11 extends DayBase {

    public String resolveExample1() throws Exception {
        KeepAwayGame game = new KeepAwayGame(createInputFileScanner("/y2022/input_day11_example.txt"));
        game.worryOperator = old -> old / 3;
        game.rounds(20);
        return String.valueOf(game.calculateResult());
    }

    public String resolveExample2() throws Exception {
        KeepAwayGame game = new KeepAwayGame(createInputFileScanner("/y2022/input_day11_example.txt"));
        // Kleinstes gemeinsames Vielfaches der Divisoren ist 96577
        game.worryOperator = old -> old % 96577;
        game.rounds(10_000);
        return String.valueOf(game.calculateResult());
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        KeepAwayGame game = new KeepAwayGame(createInputFileScanner("/y2022/input_day11.txt"));
        game.worryOperator = old -> old / 3;
        game.rounds(20);
        return String.valueOf(game.calculateResult());
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        KeepAwayGame game = new KeepAwayGame(createInputFileScanner("/y2022/input_day11.txt"));
        // Kleinstes gemeinsames Vielfaches der Divisoren ist 9699690
        game.worryOperator = old -> old % 9699690;
        game.rounds(10_000);
        return String.valueOf(game.calculateResult());
    }

    static class KeepAwayGame {
        private final List<Monkey> monkeys = new ArrayList<>();

        // Effect on item worry level after monkey ends inspection
        LongUnaryOperator worryOperator = old -> old;


        KeepAwayGame(final Scanner scanner) {
            scanner.useDelimiter("\n\nMonkey ");
            while (scanner.hasNext()) {
                String monkeySection = scanner.next();
                String[] lines = monkeySection.split("\n");

                List<Item> items = Arrays.stream(lines[1].substring(18).split(", ")).map(i -> new Item(Long.parseLong(i))).toList();

                String substring = lines[2].substring(23);
                String[] part = substring.split(" ");
                LongUnaryOperator operation = operand -> {
                    long value = part[1].equals("old") ? operand : Long.parseLong(part[1]);
                    return switch (part[0]) {
                        case "+" -> operand + value;
                        case "*" -> operand * value;
                        default  -> throw new IllegalStateException("Don't know what to do with \"" + part[0] + "\"");
                    };
                };

                substring = lines[3].substring(8);
                if (!substring.startsWith("divisible by ")) {
                    throw new IllegalStateException("Don't know what to do with \"" + lines[3] + "\"");
                }
                long testValue = Long.parseLong(lines[3].substring(21));
                int ifTrue = Integer.parseInt(lines[4].replace("    If true: throw to monkey ", ""));
                int ifFalse = Integer.parseInt(lines[5].replace("    If false: throw to monkey ", ""));
                LongFunction<Monkey> test = value -> (value % testValue == 0) ? monkeys.get(ifTrue) : monkeys.get(ifFalse);

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

        long calculateResult() {
            return monkeys.stream().mapToLong(Monkey::getInspectionCount).sorted().skip(monkeys.size() - 2).reduce(1, Math::multiplyExact);
        }
    }

    @RequiredArgsConstructor
    static class Monkey {
        private final KeepAwayGame game;
        private final List<Item> items = new ArrayList<>();
        private final LongUnaryOperator operation;
        private final LongFunction<Monkey> test;
        private @Getter long inspectionCount = 0;

        void turn() {
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                ++inspectionCount;

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

    @Data @AllArgsConstructor
    static class Item {
        private long worryLevel;
    }
}
