package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;

import java.util.*;
import java.util.function.BiFunction;

public class Day8 {

    private static int maxX, maxY;

    public static void main(final String[] args) throws Exception {
        List<String> inputLines = InputResolver.fetchLinesFromInputFile("input_day8.txt", Day8.class);
        maxY = inputLines.size() - 1;
        maxX = inputLines.getFirst().length() - 1;

        Map<Character, List<Antenna>> antennaMap = resolveAntennasFromInput(inputLines);

//        Set<Pos> antinodes = createAntinodesFrom(antennaMap, antinodeFunctionForPart1());
        Set<Pos> antinodes = createAntinodesFrom(antennaMap, antinodeFunctionForPart2());
        System.out.println("Result=" + antinodes.size());
    }

    private static Set<Pos> createAntinodesFrom(final Map<Character, List<Antenna>> antennaMap, final BiFunction<Antenna, Antenna, Set<Pos>> f) {
        Set<Pos> antinodes = new HashSet<>();
        antennaMap.forEach((frequency, antennaList) -> {
            antinodes.addAll(createAntinodesFrom(antennaList, f));
        });
        return antinodes;
    }

    private static Set<Pos> createAntinodesFrom(List<Antenna> antennaList, final BiFunction<Antenna, Antenna, Set<Pos>> f) {
        Set<Pos> antinodes = new HashSet<>();
        for (int p1 = 0; p1 < antennaList.size() - 1; ++p1) {
            for (int p2 = p1 + 1; p2 < antennaList.size(); ++p2) {
                antinodes.addAll(f.apply(antennaList.get(p1), antennaList.get(p2)));
            }
        }
        return antinodes;
    }

    private static BiFunction<Antenna, Antenna, Set<Pos>> antinodeFunctionForPart1() {
        return (a1, a2) -> {
            Vector v = Vector.between(a2, a1);
            Set<Pos> antinodes = new HashSet<>();
            Pos p1 = a2.pos.plus(v);
            Pos p2 = a1.pos.minus(v);
            if (p1.isInBounds()) antinodes.add(p1);
            if (p2.isInBounds()) antinodes.add(p2);
            return antinodes;
        };
    }

    private static BiFunction<Antenna, Antenna, Set<Pos>> antinodeFunctionForPart2() {
        return (a1, a2) -> {
            Vector v = Vector.between(a2, a1);
            Set<Pos> antinodes = new HashSet<>();

            Pos p = a2.pos;
            do {
                antinodes.add(p);
                p = p.plus(v);
            } while (p.isInBounds());

            p = a1.pos;
            do {
                antinodes.add(p);
                p = p.minus(v);
            } while (p.isInBounds());

            return antinodes;
        };
    }

    private static Map<Character, List<Antenna>> resolveAntennasFromInput(final List<String> inputLines) {
        Map<Character, List<Antenna>> result = new HashMap<>();
        for (int y = 0; y < inputLines.size(); y++) {
            String line = inputLines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c != '.') {
                    List<Antenna> antennas = result.computeIfAbsent(c, ArrayList::new);
                    antennas.add(new Antenna(new Pos(x, y), c));
                }
            }
        }
        return result;
    }

    record Vector(int x, int y) {
        static Vector between(final Antenna a1, final Antenna a2) {
            return new Vector(a1.pos.x - a2.pos.x, a1.pos.y - a2.pos.y);
        }
    }

    record Antenna(Pos pos, char frequency) {}

    record Pos(int x, int y) {
        Pos plus(final Vector v) {
            return new Pos(x + v.x, y + v.y);
        }

        Pos minus(final Vector v) {
            return new Pos(x - v.x, y - v.y);
        }

        boolean isInBounds() {
            return x >= 0 && y >= 0 && x <= maxX && y <= maxY;
        }
    }
}
