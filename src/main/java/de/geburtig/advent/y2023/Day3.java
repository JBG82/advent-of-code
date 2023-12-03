package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;

public class Day3 {
    public static void main(final String[] args) {
//        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day3_example.txt", Day3.class);
        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day3.txt", Day3.class);

        char[][] schema = new char[lines.size()][lines.getFirst().length()];
        for (int i = 0; i < lines.size(); ++i) {
            schema[i] = lines.get(i).toCharArray();
        }

        String current = "";
        List<Part> parts = new ArrayList<>();
        List<Gear> gears = new ArrayList<>();
        for (int y = 0; y < schema.length; ++y) {
            for (int x = 0; x < schema[y].length; ++x) {
                char c = schema[y][x];
                if (c == '*') {
                    gears.add(new Gear(y, x));
                }
                if (isDigit(c)) {
                    current += c;
                } else if (!current.isEmpty()) {
                    parts.add(new Part(y, x - current.length(), x - 1, Integer.parseInt(current)));
                    current = "";
                }
                System.out.print(c);
            }
            if (!current.isEmpty()) {
                parts.add(new Part(y, schema[y].length - current.length(), schema[y].length - 1, Integer.parseInt(current)));
                current = "";
            }
            System.out.println();
        }

        int result = 0;
        for (Part part : parts) {
            if (part.isAdjacentToSymbol(schema)) {
                result += part.getValue();
            }
        }
        System.out.println("Result 1: " + result);

        result = 0;
        for (Gear gear : gears) {
            List<Part> adjacent = gear.resolveAdjacentPartsFrom(parts);
            if (adjacent.size() == 2) {
                result += adjacent.getFirst().getValue() * adjacent.getLast().getValue();
            }
        }
        System.out.println("Result 2: " + result);
    }

    @Data
    static class Part {
        final int line;
        final int startX;
        final int endX;
        final int value;

        public boolean isAdjacentToSymbol(final char[][] schema) {
            for (int y = Math.max(0, line - 1); y < Math.min(schema.length, line + 2); ++y) {
                for (int x = Math.max(0, startX - 1); x < Math.min(schema[y].length, endX + 2); ++x) {
                    char c = schema[y][x];
                    if ( c != '.' && !isDigit(c)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    record Gear(int y, int x) {
        public List<Part> resolveAdjacentPartsFrom(final List<Part> parts) {
            return parts.stream().filter(p -> p.getLine() >= y - 1 && p.getLine() <= y + 1 && p.startX <= x + 1 && p.endX >= x - 1).toList();
        }
    }
}
