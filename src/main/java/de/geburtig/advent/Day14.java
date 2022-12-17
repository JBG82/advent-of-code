package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day14 extends DayBase {

    static int[][] map;

    /** Source of falling sand */
    final static Point source = new Point(500, 0);

    /** Falling grains of sand */
    List<SandGrain> falling;

    /** Number of grains of sand that have fallen to the ground */
    int lying;

    public String resolveExample1() {
        initFromInputFile("/input_day14_example.txt", false);
        letItPour();
        return String.valueOf(lying);
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        initFromInputFile("/input_day14.txt", false);
        letItPour();
        return String.valueOf(lying);
    }

    public String resolveExample2() {
        initFromInputFile("/input_day14_example.txt", true);
        letItPour();
        return String.valueOf(lying);
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        initFromInputFile("/input_day14.txt", true);
        letItPour();
        return String.valueOf(lying);
    }

    void initFromInputFile(final String inputFile, boolean withGround) {
        map = new int[999][200];
        falling = new ArrayList<>();
        lying = 0;
        Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

        List<String> lines = getLinesFromInputFile(inputFile);
        for (String line : lines) {
            String[] split = line.split(" -> ");
            for (int i = 1; i < split.length; ++i) {
                Point p1 = toPoint(split[i - 1]);
                Point p2 = toPoint(split[i]);
                drawLineInMap(p1, p2);
                max.x = Math.max(max.x, Math.max(p1.x, p2.x));
                max.y = Math.max(max.y, Math.max(p1.y, p2.y));
            }
        }

        if (withGround) {
            int y = max.y + 2;
            for (int x = 0; x < map.length; ++x) {
                map[x][y] = 1;
            }
        }
    }

    Point toPoint(String part) {
        String[] split = part.split(",");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    void letItPour() {
        int count = 0;
        boolean stopProcess = false;
        do {
            Iterator<SandGrain> it = falling.iterator();
            while (it.hasNext() && !stopProcess) {
                SandGrain grain = it.next();
                if (grain.canFall()) {
                    grain.letFall();

                    // Stop condition 1: First sand grain falling into "infinity"
                    if (grain.pos.y >= map[0].length - 1) {
                        stopProcess = true;
                    }
                } else {
                    map[grain.pos.x][grain.pos.y] = 2;
                    ++lying;
                    it.remove();

                    // Stop condition 2: When first sand grain comes to stop at source
                    if (map[source.x][source.y] != 0) {
                        stopProcess = true;
                    }
                }
            }

            if (++count % 100 == 0) {
                System.out.println("Round " + count + ": " + lying + " lying, " + falling.size() + " falling");
            }

            falling.add(new SandGrain());
        } while (!stopProcess);
    }

    private void drawLineInMap(Point p1, Point p2) {
        if (p1.x == p2.x) {
            for (int y = Math.min(p1.y, p2.y); y <= Math.max(p1.y, p2.y); ++y) {
                map[p1.x][y] = 1;
            }
        } else if (p1.y == p2.y) {
            for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); ++x) {
                map[x][p1.y] = 1;
            }
        } else {
            throw new RuntimeException("Cannot draw line " + p1 + " to " + p2);
        }
    }

    static class SandGrain {
        private final Point pos = new Point(source.x, source.y);

        boolean canFall() {
            return map[pos.x][pos.y + 1] == 0 || map[pos.x - 1][pos.y + 1] == 0 || map[pos.x + 1][pos.y + 1] == 0;
        }

        void letFall() {
            if (map[pos.x][pos.y + 1] == 0) {
                // Simply fall down
            } else if (map[pos.x - 1][pos.y + 1] == 0) {
                // Fall down to the left
                --pos.x;
            } else if (map[pos.x + 1][pos.y + 1] == 0) {
                // Fall down to the right
                ++pos.x;
            } else {
                throw new RuntimeException("Should not be happening!");
            }
            ++pos.y;
        }
    }


}
