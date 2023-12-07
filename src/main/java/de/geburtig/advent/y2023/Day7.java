/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://adventofcode.com/2023/day/7
 */
public class Day7 {

    /** Jacks are used as jokers in Puzzle 2 */
    private static boolean withJoker = false;

    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day7.txt", Day7.class);

        // Puzzle 1
        List<Hand> hands = input.stream().map(Hand::new).sorted().toList();
        long result = 0;
        for (int i = 0; i < hands.size(); ++i) {
            result += (long)(i + 1) * hands.get(i).bid;
        }
        // 253603890
        System.out.println("Result 1: " + result);

        // Puzzle 2:
        withJoker = true;
        hands = input.stream().map(Hand::new).sorted().toList();
        result = 0;
        for (int i = 0; i < hands.size(); ++i) {
            result += (long)(i + 1) * hands.get(i).bid;
        }
        // 253630098
        System.out.println("Result 2: " + result);
    }

    @Data
    @ToString(of = {"line", "typeStrength", "secondaryStrength"})
    static class Hand implements Comparable<Hand> {
        private final String line;
        private final List<Card> cards;
        private int bid;

        private final int typeStrength;
        private final int secondaryStrength;

        public Hand(final String line) {
            this.line = line;

            String[] split = line.split(" ");
            cards = split[0].chars().mapToObj(c -> new Card((char)c)).toList();
            bid = Integer.parseInt(split[1]);

            Map<Card, AtomicInteger> values = new HashMap<>();
            int power = 5;
            int secondary = 0;
            for (Card card : cards) {
                secondary += (int)Math.pow(13, --power) * card.value;
                values.computeIfAbsent(card, c -> new AtomicInteger()).incrementAndGet();
            }
            secondaryStrength = secondary;

            int maxOfOneType;
            if (withJoker) {
                AtomicInteger aij = values.remove(new Card('J'));
                int jokers = aij != null ? aij.get() : 0;
                maxOfOneType = values.values().stream().mapToInt(AtomicInteger::get).max().orElse(0) + jokers;
            } else {
                maxOfOneType = values.values().stream().mapToInt(AtomicInteger::get).max().orElseThrow();
            }
            typeStrength = switch (values.size()) {
                case 5  -> 0; // High card
                case 4  -> 1; // One pair
                case 3  -> {
                    if (maxOfOneType == 2) {
                        yield 2; // Two pairs
                    } else {
                        yield 3; // Three of a kind
                    }
                }
                case 2  -> {
                    if (maxOfOneType == 3) {
                        yield 4; // Full house
                    } else {
                        yield 5; // Four of a kind
                    }
                }
                case 1  -> 6; // Five of a kind
                default -> {
                    if (withJoker && values.size() == 0) {
                        yield 6; // Five Jokers, so Five of a kind!
                    } else {
                        throw new RuntimeException("WTF!?");
                    }
                }
            };
        }

        @Override
        public int compareTo(final Hand o) {
            int compare = Integer.compare(typeStrength, o.typeStrength);
            return compare == 0 ? Integer.compare(secondaryStrength, o.secondaryStrength) : compare;
        }
    }

    @Data
    static class Card implements Comparable<Card> {
        private final char type;
        private final int value;

        Card(final char type) {
            this.type = type;
            this.value = switch (type) {
                case 'A' -> 12;
                case 'K' -> 11;
                case 'Q' -> 10;
                case 'J' -> withJoker ? 0 : 9;
                case 'T' -> withJoker ? 9 : 8;
                default  -> (int) type - '2' + (withJoker ? 1 : 0);
            };
        }

        @Override
        public int compareTo(final Card o) {
            return Integer.compare(value, o.value);
        }
    }
}
