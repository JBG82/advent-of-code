/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/10
 */
public class Day10 {

    private static int mapHeight, mapWidth;
    private static char[][] map;

    /** In der followMap wird der Weg des "Loops" verzeichnet */
    static long[][] followMap;


    public static void main(final String[] args) {
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10_example.txt", Day10New.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10_example2.txt", Day10New.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10_example3.txt", Day10New.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10_example4.txt", Day10New.class);
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10.txt", Day10.class);

        mapHeight = input.size();
        mapWidth = input.getFirst().length();

        // Input to map
        map = new char[mapHeight][mapWidth];
        for (int i = 0; i < input.size(); ++i) {
            map[i] = input.get(i).toCharArray();
        }

        // Resolve start position and format map
        Pos startPos = null;
        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                if (map[y][x] == 'S') {
                    startPos = new Pos(x, y);
                }
                map[y][x] = formatted(map[y][x], x, y);
            }
        }
        System.out.println("startPos=" + startPos);

        // Resolve way of loop beginning from the start position
        followMap = new long[mapHeight][mapWidth];
        followMap[startPos.y][startPos.x] = -1;

        // In what direction should the first step go?
        Direction firstDirection = switch (map[startPos.y][startPos.x]) {
            case '═', '╔', '╚' -> Direction.EAST;
            case '╝', '║' -> Direction.NORTH;
            case '╗' -> Direction.SOUTH;
            default -> throw new RuntimeException("Blahblah");
        };

        Step nextStep = new Step(startPos, startPos.go(firstDirection));
        int step = 0;
        do {
            followMap[nextStep.to.y][nextStep.to.x] = ++step;
            nextStep = resolveNextStep(nextStep);
            if (followMap[nextStep.to.y][nextStep.to.x] == -1) {
                ++step;
                break;
            }
        } while (followMap[nextStep.to.y][nextStep.to.x] == 0);
        System.out.println("Completed the cycle after " + step + " steps");
        // 6875
        System.out.println("Result 1: " + (step / 2));

//        output(map);

        int result2 = 0;
        for (int y = 0; y < mapHeight; ++y) {
            boolean inside = false;
            Direction from = null;
            for (int x = 0; x < mapWidth; ++x) {
                char c = map[y][x];
                if (followMap[y][x] == 0) {
                    map[y][x] = inside ? '▒' : ' ';
                    if (inside) {
                        map[y][x] = '▒';
                        ++result2;
                    } else {
                        map[y][x] = ' ';
                    }
                } else if (c == '╔') {
                    from = Direction.SOUTH;
                } else if (c == '╚') {
                    from = Direction.NORTH;
                } else if (c == '║' || (c == '╗' && from == Direction.NORTH) || (c == '╝' && from == Direction.SOUTH)) {
                    inside = !inside;
                }
            }
        }

//        output(map);

        // 471
        System.out.println("Result 2: " + result2);
    }

    private static Step resolveNextStep(final Step prevStep) {
        Pos pos = prevStep.to;
        Direction dir = prevStep.direction();
        char c = map[pos.y][pos.x];
        Direction newDir;
        if (c == '╗') {
            newDir = dir == Direction.EAST ? Direction.SOUTH : Direction.WEST;
        } else if (c == '╔') {
            newDir = dir == Direction.WEST ? Direction.SOUTH : Direction.EAST;
        } else if (c == '╚') {
            newDir = dir == Direction.WEST ? Direction.NORTH : Direction.EAST;
        } else if (c == '╝') {
            newDir = dir == Direction.EAST ? Direction.NORTH : Direction.WEST;
        } else {
            newDir = dir;
        }
        return new Step(pos, pos.go(newDir));
    }

    // ═ (205)
    // ║ (186)
    // ╗ (187)
    // ╝ (188)
    // ╚ (200)
    // ╔ (201)
    static char formatted(final char value, int x, int y) {
        return switch (value) {
            case '-' -> '═';
            case '|' -> '║';
            case 'J' -> '╝';
            case 'F' -> '╔';
            case 'L' -> '╚';
            case '7' -> '╗';
            case 'S' -> {
                List<Direction> dirs = new ArrayList<>();
                if (y > 0 && in(map[y - 1][x], '7', '╗', '|', '║', 'F', '╔')) {
                    dirs.add(Direction.NORTH);
                }
                if (in(map[y + 1][x], 'J', '╝', '|', '║', 'L', '╚')) {
                    dirs.add(Direction.SOUTH);
                }
                if (in(map[y][x - 1], 'L', '╚', '-', '═', 'F', '╔')) {
                    dirs.add(Direction.WEST);
                }
                if (in(map[y][x + 1], 'J', '-', '7')) {
                    dirs.add(Direction.EAST);
                }
                if (dirs.size() != 2) throw new RuntimeException("Cannot determine start char!");
                if (dirs.contains(Direction.NORTH)) {
                    yield dirs.contains(Direction.EAST) ? '╝' : dirs.contains(Direction.WEST) ? '╚' : '║';
                } else if (dirs.contains(Direction.SOUTH)) {
                    yield dirs.contains(Direction.EAST) ? '╔' : '╗';
                }
                yield '═';
            }
            default  -> value;
        };
    }

    private static boolean in(char c, char... in) {
        for (char o : in) {
            if (c == o) return true;
        }
        return false;
    }

    static void output(final char[][] map) {
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                System.out.print(map[y][x]);
            }
            System.out.println();
        }
    }

    record Step(Pos from, Pos to) {
        Direction direction() {
            if (from.x == to.x) {
                return from.y < to.y ? Direction.SOUTH : Direction.NORTH;
            }
            return from.x < to.x ? Direction.EAST : Direction.WEST;
        }
    }
    record Pos(int x, int y) {
        Pos go(final Direction direction) {
            return switch (direction) {
                case NORTH -> new Pos(x, y - 1);
                case EAST -> new Pos(x + 1, y);
                case SOUTH -> new Pos(x, y + 1);
                case WEST -> new Pos(x - 1, y);
            };
        }
    }

    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }
}
