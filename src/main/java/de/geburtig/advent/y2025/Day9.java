/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * https://adventofcode.com/2025/day/9
 */
public class Day9 {
    public static void main(String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day9.txt");
        List<Tile> tiles = input.stream().map(line -> {
            String[] split = line.split(",");
            return new Tile(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }).toList();
        System.out.println("Got " + tiles.size() + " tiles");

        // Part 1: 4735268538
        System.out.println("Part 1: " + solvePart1(tiles));
        // Part 2: 1537458069
        System.out.println("Part 2: " + solvePart2(tiles));
    }

    private static long solvePart1(List<Tile> tiles) {
        long max = 0;
        for (int i = 0; i < tiles.size() - 1; ++i) {
            for (int j = i + 1; j < tiles.size(); ++j) {
                Tile t1 = tiles.get(i);
                Tile t2 = tiles.get(j);
                long val = (long) (Math.abs(t1.x - t2.x) + 1) * (Math.abs(t1.y - t2.y) + 1);
                max = max(max, val);
            }
        }
        return max;
    }

    private static long solvePart2(List<Tile> tiles) {
        // Kanten konstruieren und mit den Punkten in Beziehung bringen
        List<Edge> edges = new ArrayList<>();
        Tile lastTile = tiles.getFirst();
        for (int i = 1; i < tiles.size(); i++) {
            Tile newTile = tiles.get(i);
            Edge e = new Edge(lastTile, newTile);
            lastTile.getEdges().add(e);
            newTile.getEdges().add(e);
            edges.add(e);
            lastTile = newTile;
        }
        Edge missingLink = new Edge(lastTile, tiles.getFirst());
        tiles.getFirst().getEdges().add(missingLink);
        tiles.getLast().getEdges().add(missingLink);
        edges.add(missingLink);

        // Mögliche Rechtecke konstruieren und nach Fläche sortieren (größte nach vorne)
        List<Rect> possibleMatches = new ArrayList<>();
        for (int i = 0; i < tiles.size() - 1; i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile t1 = tiles.get(i);
                Tile t2 = tiles.get(j);
                possibleMatches.add(new Rect(t1, t2));
            }
        }
        possibleMatches.sort(Comparator.comparingLong(Rect::getArea).reversed());

        // Jetzt alle Kombinationen durchprobieren. Ausschlusskriterien sind:
        //  1.) Innerhalb des Rechtecks liegt ein Punkt
        //  2.) Die Fläche des Rechtecks wird durch eine zwischen zwei Punkten liegende Kante "durchschnitten"
        for (Rect rect : possibleMatches) {
            boolean fits = true;
            inner:
            for (Tile tile : tiles) {
                if (rect.contains(tile)) continue;
                if (tile.isIn(rect)) {
                    fits = false;
                    break;
                }
                for (Edge edge : tile.getEdges()) {
                    if (edge.crosses(rect)) {
                        fits = false;
                        break inner;
                    }
                }
            }
            if (fits) {
                // Das wars!
                return rect.getArea();
            }
        }
        throw new RuntimeException("No result found");
    }

    @Data
    static class Rect {
        private final Tile t1;
        private final Tile t2;
        private final long area;

        Rect(Tile t1, Tile t2) {
            this.t1 = t1;
            this.t2 = t2;
            this.area = ((long)max(t1.x, t2.x) - min(t1.x, t2.x) + 1) * (max(t1.y, t2.y) - min(t1.y, t2.y) + 1);
        }

        boolean contains(Tile tile) {
            return t1 == tile || t2 == tile;
        }

        int minX() {
            return min(t1.x, t2.x);
        }
        int maxX() {
            return max(t1.x, t2.x);
        }
        int minY() {
            return min(t1.y, t2.y);
        }
        int maxY() {
            return max(t1.y, t2.y);
        }
    }

    record Edge(Tile t1, Tile t2) {
        boolean crosses(Rect r) {
            return (minX() <= r.minX() && maxX() >= r.maxX() && minY() > r.minY() && maxY() < r.maxY()) ||
                    (minY() <= r.minY() && maxY() >= r.maxY() && minX() > r.minX() && maxX() < r.maxX());
        }

        int minX() {
            return min(t1.x, t2.x);
        }
        int maxX() {
            return max(t1.x, t2.x);
        }
        int minY() {
            return min(t1.y, t2.y);
        }
        int maxY() {
            return max(t1.y, t2.y);
        }
    }

    @Data
    @ToString(of = {"x", "y"})
    static class Tile {
        private final int x;
        private final int y;
        private final List<Edge> edges = new ArrayList<>(2);

        public boolean isIn(Rect rect) {
            return x > rect.minX() && x < rect.maxX() && y > rect.minY() && y < rect.maxY();
        }
    }
}
