package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day8 extends DayBase {

    private final int[][] trees;

    public Day8() {
        List<String> lines = getLinesFromInputFile("/input_day8.txt");
        int maxX = lines.get(0).length();
        int maxY = lines.size();
        trees = new int[maxY][maxX];

        int y = 0;
        for (String line : lines) {
            trees[y++] = line.chars().map(x -> Integer.parseInt(String.valueOf((char)x))).toArray();
        }

//        print(trees);
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        int[][] tree = trees;

//        System.out.println();
        int count = 0;
        for (int y = 0; y < tree.length; ++y) {
            for (int x = 0; x < tree[y].length; ++x) {
                if (x == 0 || y == 0 || x == tree[y].length - 1 || y == tree.length - 1) {
                    // Äußere Bäume immer zählen
                    ++count;
//                    System.out.print("o");
                } else {
                    int v = tree[y][x];
//                    if (isVisibleFromNorth(tree, x, y)) {
                    if (isVisibleFromNorth(tree, x, y) || isVisibleFromSouth(tree, x, y) || isVisibleFromEast(tree, x, y) || isVisibleFromWest(tree, x, y)) {
                        ++count;
//                        System.out.print("o");
                    } else {
//                        System.out.print("x");
                    }
                }
            }
//            System.out.println();
        }

        return String.valueOf(count);
    }

    @Override
    public String resolvePuzzle2() {
        int[][] tree = trees;
        int maxScore = 0;
        for (int y = 1; y < tree.length - 1; ++y) {
            for (int x = 1; x < tree[y].length - 1; ++x) {
                maxScore = Math.max(maxScore, score(tree, y, x));
            }
        }
        return String.valueOf(maxScore);
    }

    private int score(int[][] tree, int treeX, int treeY) {
        // North
        int distanceNorth = 0;
        for (int y = treeY - 1; y >= 0; --y) {
            ++distanceNorth;
            if (tree[y][treeX] >= tree[treeY][treeX]) break;
        }
        // South
        int distanceSouth = 0;
        for (int y = treeY + 1; y < tree.length; ++y) {
            ++distanceSouth;
            if (tree[y][treeX] >= tree[treeY][treeX]) break;
        }
        // East
        int distanceEast = 0;
        for (int x = treeX - 1; x >= 0; --x) {
            ++distanceEast;
            if (tree[treeY][x] >= tree[treeY][treeX]) break;
        }
        // West
        int distanceWest = 0;
        for (int x = treeX + 1; x < tree[treeY].length; ++x) {
            ++distanceWest;
            if (tree[treeY][x] >= tree[treeY][treeX]) break;
        }
        return distanceNorth * distanceSouth * distanceEast * distanceWest;
    }

    private boolean isVisibleFromNorth(int[][] tree, int treeX, int treeY) {
        for (int y = treeY - 1; y >= 0; --y) {
            if (tree[y][treeX] >= tree[treeY][treeX]) return false;
        }
        return true;
    }

    private boolean isVisibleFromSouth(int[][] tree, int treeX, int treeY) {
        for (int y = treeY + 1; y < tree.length; ++y) {
            if (tree[y][treeX] >= tree[treeY][treeX]) return false;
        }
        return true;
    }

    private boolean isVisibleFromEast(int[][] tree, int treeX, int treeY) {
        for (int x = treeX - 1; x >= 0; --x) {
            if (tree[treeY][x] >= tree[treeY][treeX]) return false;
        }
        return true;
    }

    private boolean isVisibleFromWest(int[][] tree, int treeX, int treeY) {
        for (int x = treeX + 1; x < tree[treeY].length; ++x) {
            if (tree[treeY][x] >= tree[treeY][treeX]) return false;
        }
        return true;
    }

    private void print(int[][] tree) {
        System.out.println("Print: ");
        for (int y = 0; y < tree.length; ++y) {
            for (int x = 0; x < tree[y].length; ++x) {
                System.out.print(tree[y][x]);
            }
            System.out.println();
        }
    }
}
