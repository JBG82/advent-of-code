/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.advent.util.PeekableIterator;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day4 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day4.txt", Day4.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day4_example.txt", Day4.class);
        List<Card> cards = input.stream().map(Card::of).toList();

        long result = cards.stream().mapToLong(Card::resolveScore).sum();
        System.out.println("Result 1: " + result);

        PeekableIterator<Card> iterator = new PeekableIterator<>(cards);
        while (iterator.hasNext()) {
            Card card = iterator.next();
            long matchCount = card.resolveMatchCount();
            for (int i = 0; i < matchCount; ++i) {
                iterator.peekNextPlus(i).ifPresent(c -> c.setInstances(c.getInstances() + card.getInstances()));
            }
        }
        result = cards.stream().mapToLong(Card::getInstances).sum();
        System.out.println("Result 2: " + result);
    }

    @Data
    static class Card {
        private final int id;
        private final Set<Integer> winningNumbers;
        private final Set<Integer> numbers;
        private long instances = 1;

        private static Card of(final String line) {
            Pattern pattern = Pattern.compile("Card +(\\d+): (.*) \\| (.*)");
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) throw new IllegalArgumentException("Cannot parse pattern on: \"" + line + "\"");

            int id = Integer.parseInt(matcher.group(1));
            Set<Integer> winningNumbers = Arrays.stream(matcher.group(2).split(" ")).filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toSet());
            Set<Integer> numbers = Arrays.stream(matcher.group(3).split(" ")).filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toSet());
            return new Card(id, winningNumbers, numbers);
        }

        private long resolveMatchCount() {
            return winningNumbers.stream().filter(numbers::contains).count();
        }

        public long resolveScore() {
            return (long) Math.pow(2, resolveMatchCount() - 1);
        }

        @Override
        public String toString() {
            return id + " " + getWinningNumbers().size() + " " + getNumbers().size() + " " + resolveMatchCount() + " " + resolveScore();
        }
    }
}
