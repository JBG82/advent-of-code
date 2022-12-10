package de.geburtig.advent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

public class Day5 {

/*
    [C]         [S] [H]
    [F] [B]     [C] [S]     [W]
    [B] [W]     [W] [M] [S] [B]
    [L] [H] [G] [L] [P] [F] [Q]
    [D] [P] [J] [F] [T] [G] [M] [T]
    [P] [G] [B] [N] [L] [W] [P] [W] [R]
    [Z] [V] [W] [J] [J] [C] [T] [S] [C]
    [S] [N] [F] [G] [W] [B] [H] [F] [N]
     1   2   3   4   5   6   7   8   9
*/

    private static final Stack<String>[] stack = new Stack[10];

    private void initStacks() {
        init(1, "SZPDLBFC");
        init(2, "NVGPHWB");
        init(3, "FWBJG");
        init(4, "GJNFLWCS");
        init(5, "WJLTPMSH");
        init(6, "BCWGFS");
        init(7, "HTPMQBW");
        init(8, "FSWT");
        init(9, "NCR");
    }

    private void init(int stackNo, String items) {
        stack[stackNo] = new Stack<>();
        items.chars().forEach(item -> stack[stackNo].push(String.valueOf((char)item)));
    }


    public String resolvePuzzle1() throws Exception {
        initStacks();
        Path resource = Paths.get(getClass().getResource("/input_day5.txt").toURI());
        Files.lines(resource).skip(10).forEach(line -> {
            String[] split = line.split(" ");
            int count = Integer.parseInt(split[1]);
            int from = Integer.parseInt(split[3]);
            int to = Integer.parseInt(split[5]);
            moveWithCrateMover9000(count, from, to);
        });
        return getTopItems();
    }

    public String resolvePuzzle2() throws Exception {
        initStacks();
        Path resource = Paths.get(getClass().getResource("/input_day5.txt").toURI());
        Files.lines(resource).skip(10).forEach(line -> {
            String[] split = line.split(" ");
            int count = Integer.parseInt(split[1]);
            int from = Integer.parseInt(split[3]);
            int to = Integer.parseInt(split[5]);
            moveWithCrateMover9001(count, from, to);
        });
        return getTopItems();
    }

    private void moveWithCrateMover9000(int count, int from, int to) {
        IntStream.range(0, count).forEach(i -> stack[to].push(stack[from].pop()));
    }

    private void moveWithCrateMover9001(int count, int from, int to) {
        List<String> block = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> block.add(0, stack[from].pop()));
        block.forEach(item -> stack[to].push(item));
    }

    private String getTopItems() {
        String result = "";
        for (int i = 1; i < 10; ++i) {
            result += stack[i].peek();
        }
        return result;
    }
}
