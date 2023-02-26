package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Console;
import java.util.*;
import java.util.function.Predicate;

public class Day18 extends DayBase {

    private int[][][] map;

    public String resolveExample1() {
//        setInputFromFile("/input_day18_example.txt");
//        return calculateSides();

        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18_example.txt"));
        return matrix.calculatePuzzle1();
    }


    @Override
    public String resolvePuzzle1() throws Exception {
//        setInputFromFile("/input_day18.txt");
//        return calculateSides();

        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18.txt"));
        return matrix.calculatePuzzle1();
    }

    public String resolveExample2() {
        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18_example.txt"));
//        matrix.output(10);
        return matrix.calculatePuzzle2();
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18.txt"));
//        matrix.output();
        return matrix.calculatePuzzle2();
    }

    void setInputFromFile(final String resourceName) {
        map = new int[23][23][23];
        for (String line : getLinesFromInputFile(resourceName)) {
            int[] coord = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
            int x = coord[0];
            int y = coord[1];
            int z = coord[2];
            set(x, y, z);
        }
    }

    String calculateSides() {
        int sides = 0;
        for (int x = 0; x <= 21; ++x) {
            for (int y = 0; y <= 21; ++y) {
                for (int z = 0; z <= 21; ++z) {
                    if (isSet(x, y, z)) {
                        sides += 6;
                        if (x > 0 && isSet(x-1, y, z)) --sides;
                        if (isSet(x+1, y, z)) --sides;
                        if (y > 0 && isSet(x, y-1, z)) --sides;
                        if (isSet(x, y+1, z)) --sides;
                        if (z > 0 && isSet(x, y, z-1)) --sides;
                        if (isSet(x, y, z+1)) --sides;
                    }
                }
            }
        }
        return String.valueOf(sides);
    }

    void checkOutside(int x, int y, int z) {
        setChecked(x, y, z);
        if (x==0 || x==21 || y==0 || y==21 || z==0 || z==21) {
            if (!isSet(x, y, z)) {
                setOutside(x, y, z);
            }
        } else {
            if (!isSet(x, y, z)) {
                if (isChecked(x-1, y, z) && isOutside(x-1, y, z)) setOutside(x, y, z);
                if (isChecked(x+1, y, z) && isOutside(x+1, y, z)) setOutside(x, y, z);
                if (isChecked(x, y-1, z) && isOutside(x, y-1, z)) setOutside(x, y, z);
                if (isChecked(x, y+1, z) && isOutside(x, y+1, z)) setOutside(x, y, z);
                if (isChecked(x, y, z-1) && isOutside(x, y, z-1)) setOutside(x, y, z);
                if (isChecked(x, y, z+1) && isOutside(x, y, z+1)) setOutside(x, y, z);
            }
        }
    }

    boolean isOuter(int x, int y, int z) {
        return x==0 || x==21 || y==0 || y==21 || z==0 || z==21;
    }

    int countOutsides(int x, int y, int z) {
        int result = 0;
        if (x==0 || x==21) ++result;
        if (y==0 || y==21) ++result;
        if (z==0 || z==21) ++result;
        return result;
    }

    void set(int x, int y, int z) {
        if (!isSet(x, y, z)) {
            map[x][y][z] += 1;
        }
    }

    boolean isSet(int x, int y, int z) {
        return map[x][y][z] % 2 == 1;
    }

    void setChecked(int x, int y, int z) {
        if (!isChecked(x, y, z)) {
            map[x][y][z] += 2;
        }
    }

    boolean isChecked(int x, int y, int z) {
        return map[x][y][z] % 4 > 1;
    }

    void setOutside(int x, int y, int z) {
        if (!isOutside(x, y, z)) {
            map[x][y][z] += 4;
        }
    }

    boolean isOutside(int x, int y, int z) {
        return map[x][y][z] % 8 > 3;
    }

    static class Matrix {
        Dot[][][] dots = new Dot[22][22][22];

        Matrix(List<String> lines) {
            init();

            for (String line : lines) {
                int[] coord = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                int x = coord[0];
                int y = coord[1];
                int z = coord[2];
                get(x, y, z).set = true;
            }

            dots[0][0][0].spreadOutsideConnectionFlag(true);
        }

        Dot get(int x, int y, int z) {
            return dots[x][y][z];
        }

        void set(int x, int y, int z) {
            dots[x][y][z].set = true;
        }

        boolean isSet(int x, int y, int z) {
            return dots[x][y][z].set;
        }

        boolean isCheckedForConnectionToOutside(int x, int y, int z) {
            return dots[x][y][z].connectedToOutside != null;
        }

        boolean isConnectedToOutside(int x, int y, int z) {
            return Boolean.TRUE.equals(dots[x][y][z].connectedToOutside);
        }

        void init() {
            for (int x = 0; x <= 21; ++x) {
                for (int y = 0; y <= 21; ++y) {
                    for (int z = 0; z <= 21; ++z) {
                        dots[x][y][z] = new Dot(new Point(x, y, z));
                    }
                }
            }

            Dot outside = new Dot(null);
            outside.setConnectedToOutside(true);

            for (int x = 0; x <= 21; ++x) {
                for (int y = 0; y <= 21; ++y) {
                    for (int z = 0; z <= 21; ++z) {
                        dots[x][y][z].neighbours[0] = (x==21) ? outside : dots[x+1][y][z];
                        dots[x][y][z].neighbours[1] = (x==0) ? outside : dots[x-1][y][z];
                        dots[x][y][z].neighbours[2] = (y==21) ? outside : dots[x][y+1][z];
                        dots[x][y][z].neighbours[3] = (y==0) ? outside : dots[x][y-1][z];
                        dots[x][y][z].neighbours[4] = (z==21) ? outside : dots[x][y][z+1];
                        dots[x][y][z].neighbours[5] = (z==0) ? outside : dots[x][y][z-1];
                    }
                }
            }
        }

        public String calculatePuzzle1() {
            return calculate(dot -> dot.connectedToOutside == null || Boolean.TRUE.equals(dot.connectedToOutside));
        }

        public String calculatePuzzle2() {
            return calculate(dot -> Boolean.TRUE.equals(dot.connectedToOutside));
        }

        private String calculate(Predicate<Dot> predicate) {
            int sides = 0;

            for (int x = 0; x <= 21; ++x) {
                for (int y = 0; y <= 21; ++y) {
                    for (int z = 0; z <= 21; ++z) {
                        Dot dot = get(x, y, z);
                        if (!dot.isSet() && predicate.test(dot)) {
                            int result = dot.countSetNeighbours();
                            sides += result;
                        } else if (dot.isSet() && (x==0 || x==21 || y==0 || y==21 || z==0 || z==21)) {
                            // Gesetzte Punkte an den Außenkanten müssen jeweils auch noch mit +1 gezählt werden!
                            ++sides;
                        }
                    }
                }
            }
            return String.valueOf(sides);
        }

        public void output() {
            output(22);
        }

        public void output(int max) {
            for (int z = 0; z < max; ++z) {
                for (int y = 0; y < max; ++y) {
                    for (int x = 0; x < max; ++x) {
                        Dot dot = get(x, y, z);
//                        System.out.print(dot.toOutputChar());
                        System.out.print(dot.toOutputCharWithSetNeighbours());
                    }
                    System.out.println();
                }
                System.out.println("-----------------------------------------------------------------------------------");
            }
        }
    }

    @Data
    @ToString(of = {"point", "set", "connectedToOutside"})
    @EqualsAndHashCode(of = "point")
    static class Dot {

        final Point point;
        boolean set;
        Boolean connectedToOutside;
        Dot[] neighbours = new Dot[6];

        public void spreadOutsideConnectionFlag(boolean isConnectedToOutside) {
            leftToCheck.clear();
            leftToCheck.add(this);

            while (leftToCheck.size() > 0) {
                Dot dot = leftToCheck.remove(0);
                if (dot.connectedToOutside == null) {
//                    System.out.println("Get " + dot.point + " with " + leftToCheck.size() + " dots left to check");
                    dot.spreadOutsideConnectionFlag(isConnectedToOutside, 0);
                }
            }
        }

        private void spreadOutsideConnectionFlag(boolean isConnectedToOutside, long depth) {
            if (connectedToOutside == null) {
                if (depth == MAX_DEPTH) {
                    leftToCheck.add(this);
                } else {
                    connectedToOutside = isConnectedToOutside;
                    if (!set) {
                        Arrays.stream(neighbours).filter(dot -> dot.point != null).forEach(neighbour -> {
                            neighbour.spreadOutsideConnectionFlag(isConnectedToOutside, depth + 1);
                        });
                    }
                }
            }
        }

        private static final long MAX_DEPTH = 500;
        private static final List<Dot> leftToCheck = new ArrayList<>();

        public String toOutputChar() {
            return set ? "o" : (connectedToOutside == null ? "?" : (connectedToOutside ? "·" : "x"));
        }

        public String toOutputCharWithSetNeighbours() {
            if (set) {
                return "o";
            } else if (connectedToOutside == null) {
                return "?";
            } else if (!connectedToOutside) {
                return "?";
            }
            int setNeighbours = countSetNeighbours();
            if (setNeighbours > 0) {
                return String.valueOf(setNeighbours);
            }
            return "·";
        }

        public int countSetNeighbours() {
            return (int) Arrays.stream(neighbours).filter(Dot::isSet).count();
        }
    }

    @Value
    static class Point {
        int x, y, z;
    }
}
