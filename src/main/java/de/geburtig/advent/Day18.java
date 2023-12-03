package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Day18 extends DayBase {

    public String resolveExample1() {
        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18_example.txt"));
        return matrix.calculatePuzzle1();
    }


    @Override
    public String resolvePuzzle1() throws Exception {
        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18.txt"));
        return matrix.calculatePuzzle1();
    }

    public String resolveExample2() {
        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18_example.txt"));
        return matrix.calculatePuzzle2();
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        Matrix matrix = new Matrix(getLinesFromInputFile("/input_day18.txt"));
        return matrix.calculatePuzzle2();
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
    }

    @Data
    @ToString(of = {"point", "set", "connectedToOutside"})
    @EqualsAndHashCode(of = "point")
    static class Dot {

        final Point point;
        boolean set;
        Boolean connectedToOutside;
        Dot[] neighbours = new Dot[6];

        /** Maximum recursion depth for spreading of outside connection flag */
        private static final long MAX_DEPTH = 500;

        /** List of dots, that still need to be checked for outside connection after reaching maximum recursion depth */
        private static final List<Dot> leftToCheck = new ArrayList<>();

        /**
         * Set if this dot is connected to outside and spread this information to its adjacent neighbours recursively
         * @param isConnectedToOutside boolean
         */
        public void spreadOutsideConnectionFlag(boolean isConnectedToOutside) {
            leftToCheck.clear();
            leftToCheck.add(this);

            while (leftToCheck.size() > 0) {
                Dot dot = leftToCheck.remove(0);
                if (dot.connectedToOutside == null) {
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

        public int countSetNeighbours() {
            return (int) Arrays.stream(neighbours).filter(Dot::isSet).count();
        }
    }

    /**
     * Represents a Coordinate for a dot in 3d space
     */
    record Point(int x, int y, int z) {}
}
