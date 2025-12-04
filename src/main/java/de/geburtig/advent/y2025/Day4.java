/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

/**
 * https://adventofcode.com/2025/day/4/input
 */
public class Day4 {

    private static char[][] map;

    public static void main(String[] args) {
        map = InputResolver.fetchInputAsCharArray("input_day4.txt");

        int accessableRolls = solvePart1();
        int removableRolls = solvePart2();

        // Part 1:  1540
        // Part 2:  8972
        System.out.println(accessableRolls);
        System.out.println(removableRolls);
    }

    private static int solvePart1() {
        int accessableRolls = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                Pos pos = new Pos(x, y);
                if (isRole(pos)) {
                    if (countSurroundingRolls(pos) < 4) {
                        accessableRolls++;
                    }
                }
            }
        }
        return accessableRolls;
    }

    private static int solvePart2() {
        int removedRollsOverall = 0;

        int justRemoved;
        do {
            justRemoved = 0;
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    Pos pos = new Pos(x, y);
                    if (isRole(pos)) {
                        if (countSurroundingRolls(pos) < 4) {
                            ++justRemoved;
                            map[x][y] = 'X';
                        }
                    }
                }
            }
            removedRollsOverall += justRemoved;
        } while (justRemoved > 0);

        return removedRollsOverall;
    }

    static int countSurroundingRolls(Pos pos) {
        int result = 0;
        result += isRole(new Pos(pos.x - 1, pos.y - 1)) ? 1 : 0;
        result += isRole(new Pos(pos.x - 1, pos.y)) ? 1 : 0;
        result += isRole(new Pos(pos.x - 1, pos.y + 1)) ? 1 : 0;
        result += isRole(new Pos(pos.x, pos.y - 1)) ? 1 : 0;
        result += isRole(new Pos(pos.x, pos.y + 1)) ? 1 : 0;
        result += isRole(new Pos(pos.x + 1, pos.y - 1)) ? 1 : 0;
        result += isRole(new Pos(pos.x + 1, pos.y)) ? 1 : 0;
        result += isRole(new Pos(pos.x + 1, pos.y + 1)) ? 1 : 0;
        return result;
    }

    static boolean isRole(Pos pos) {
        if (pos.x < 0 || pos.x >= map.length || pos.y < 0 || pos.y >= map[0].length) {
            return false;
        }
        return map[pos.x][pos.y] == '@';
    }

    record Pos(int x, int y) {}
}
