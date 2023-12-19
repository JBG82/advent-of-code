package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    static char[][] map = new char[140][140];
    static char[][] formatMap = new char[140][140];

    static long[][] followMap = new long[140][140];

    static char[][] check = new char[140][140];

    static int mapWidth, mapHeight;

    static char[][] doubleMap = new char[280][280];

    public static void main(final String[] args) {
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10_example.txt", Day10.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10_example2.txt", Day10.class);
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day10.txt", Day10.class);

        mapHeight = input.size();
        mapWidth = input.getFirst().length();

        // Input to map
        for (int i = 0; i < input.size(); ++i) {
            map[i] = input.get(i).toCharArray();
        }

        // Resolve start position and build formatted map
        Pos startPos = null;
        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                formatMap[y][x] = formatted(map[y][x], x, y);
                if (map[y][x] == 'S') {
                    startPos = new Pos(x, y);
                }
//                System.out.print(formatMap[y][x]);
            }
//            System.out.println();
        }

        System.out.println(startPos);
        followMap[startPos.y][startPos.x] = -1;
        System.out.println(followMap[startPos.y][startPos.x]);

        Step nextStep = new Step(startPos, startPos.go(Direction.EAST));
        int step = 0;
        do {
            followMap[nextStep.to.y][nextStep.to.x] = ++step;
            nextStep = getNextStep(nextStep);
            if (map[nextStep.to.y][nextStep.to.x] == 'S') {
                ++step;
                break;
            }
        } while (followMap[nextStep.to.y][nextStep.to.x] == 0);
        System.out.println("Completed the cycle after " + step + " steps");
        System.out.println("Result 1: " + (step / 2));

        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                check[y][x] = followMap[y][x] != 0 ? 'o' : '.';
                if (followMap[y][x] == 0 && formatMap[y][x] != '.') {
                    formatMap[y][x] = ' ';
                }
//                System.out.print(formatMap[y][x]);
            }
//            System.out.println();
        }

        for (int i = 0; i < mapWidth; ++i) {
            traverse(new Pos(i, 0));
        }
        traverse(new Pos(mapWidth - 1, mapHeight - 1));

        System.out.println();
        System.out.println();

        int count = 0;
        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
//                System.out.print(check[y][x]);
                if (check[y][x] == '.') {
                    ++count;
                    System.out.print('.');
                } else {
                    System.out.print(check[y][x] == '_' ? ' ' : formatMap[y][x]);
                }
            }
            System.out.println();
        }

        // 743 too high
        //  68 not right
        System.out.println("Result 2: " + count);

        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                doubleMap[y*2+0][x*2+0] = ' ';
                doubleMap[y*2+0][x*2+1] = in(formatMap[y][x], '║', '╝', '╚') ? '║' : ' ';
                doubleMap[y*2+1][x*2+0] = in(formatMap[y][x], '═', '╝', '╗') ? '═' : ' ';
                doubleMap[y*2+1][x*2+1] = formatMap[y][x];
            }
        }

        traverse2(new Pos(0, 0));
        traverse2(new Pos(doubleMap.length-2, doubleMap.length-2));

        for (int y = 0; y < mapHeight*2; ++y) {
            for (int x = 0; x < mapWidth*2; ++x) {
                System.out.print(doubleMap[y][x]);
            }
            System.out.println();
        }

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
                if (in(map[y - 1][x], '7', '|', 'F')) {
                    dirs.add(Direction.NORTH);
                }
                if (in(map[y + 1][x], 'J', '|', 'L')) {
                    dirs.add(Direction.SOUTH);
                }
                if (in(map[y][x - 1], 'L', '-', 'F')) {
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

    static void traverse(Pos pos) {
        if (pos.x >= 0 && pos.x < mapWidth && pos.y >= 0 && pos.y < mapHeight && check[pos.y][pos.x] == '.') {
            check[pos.y][pos.x] = '_';
            traverse(pos.go(Direction.EAST));
            traverse(pos.go(Direction.SOUTH));
            traverse(pos.go(Direction.WEST));
            traverse(pos.go(Direction.NORTH));
        }
    }

    static void traverse2(Pos pos) {
        if (pos.x >= 0 && pos.x < mapWidth*2 && pos.y >= 0 && pos.y < mapHeight*2 && doubleMap[pos.y][pos.x] == ' ') {
            doubleMap[pos.y][pos.x] = '▒';
            traverse2(pos.go(Direction.EAST));
            traverse2(pos.go(Direction.SOUTH));
            traverse2(pos.go(Direction.WEST));
            traverse2(pos.go(Direction.NORTH));
        }
    }

    private static Step getNextStep(final Step prevStep) {
        Pos pos = prevStep.to;
        Direction dir = prevStep.direction();
        char c = map[pos.y][pos.x];
        Direction newDir;
        if (c == '7') {
            newDir = dir == Direction.EAST ? Direction.SOUTH : Direction.WEST;
        } else if (c == 'F') {
            newDir = dir == Direction.WEST ? Direction.SOUTH : Direction.EAST;
        } else if (c == 'L') {
            newDir = dir == Direction.WEST ? Direction.NORTH : Direction.EAST;
        } else if (c == 'J') {
            newDir = dir == Direction.EAST ? Direction.NORTH : Direction.WEST;
        } else {
            newDir = dir;
        }
        return new Step(pos, pos.go(newDir));
    }

//    static Pos nextPosFrom(final Pos pos) {
//    }

    record Step(Pos from, Pos to) {
        Direction direction() {
            if (from.x == to.x) {
                return from.y < to.y ? Direction.SOUTH : Direction.NORTH;
            }
            return from.x < to.x ? Direction.EAST : Direction.WEST;
        }
    }
    record Pos(int x, int y) {
        Pos go(Direction direction) {
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
