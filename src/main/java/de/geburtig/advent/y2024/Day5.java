/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;

import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day5 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day5.txt", Day5.class);

        Map<Integer, Page> pagesMap = new HashMap<>();
        List<Update> updateList = new ArrayList<>();
        boolean fetchRules = true;
        for (String line : input) {
            if (line.isEmpty()) {
                fetchRules = false;
            } else if (fetchRules) {
                String[] split = line.split("\\|");
                int pageNr1 = parseInt(split[0]);
                int pageNr2 = parseInt(split[1]);
                pagesMap.computeIfAbsent(pageNr1, Page::new).after.add(pageNr2);
                pagesMap.computeIfAbsent(pageNr2, Page::new).before.add(pageNr1);
            } else {
                String[] split = line.split(",");
                Update update = new Update();
                for (String page : split) {
                    update.pages.add(pagesMap.get(parseInt(page)));
                }
                updateList.add(update);
            }
        }

        int result1 = updateList.stream().filter(Update::isCorrect).mapToInt(Update::middlePage).sum();
        // 5747
        System.out.println(result1);

        int result2 = updateList.stream().filter(u -> !u.isCorrect()).map(Update::reorder).mapToInt(Update::middlePage).sum();
        // 5502
        System.out.println(result2);

    }

    @Data
    static class Page implements Comparable<Page> {
        private final int number;
        private final Set<Integer> before = new HashSet<>();
        private final Set<Integer> after = new HashSet<>();

        @Override
        public int compareTo(final Page o) {
            if (before.contains(o.number) || o.after.contains(number)) {
                return -1;
            } else if (after.contains(o.number) || o.before.contains(number)) {
                return 1;
            }
            return 0;
        }
    }

    @Data
    static class Update {
        private final List<Page> pages = new ArrayList<>();

        int middlePage() {
            return pages.get(pages.size() / 2).number;
        }

        boolean isCorrect() {
            for (int i = 0; i < pages.size(); i++) {
                Page p1 = pages.get(i);
                for (int j = i + 1; j < pages.size(); j++) {
                    Page p2 = pages.get(j);
                    if (p2.after.contains(p1.number) || p1.before.contains(p2.number)) {
                        return false;
                    }
                }
            }
            return true;
        }

        Update reorder() {
            Update result = new Update();
            result.pages.addAll(pages.stream().sorted().toList());
            return result;
        }
    }
}
