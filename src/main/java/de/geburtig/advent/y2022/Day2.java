package de.geburtig.advent.y2022;

import lombok.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day2 {

    public int resolvePuzzle1() throws Exception {
        Path resource = Paths.get(getClass().getResource("/y2022/input_day2.txt").toURI());

        int sum = 0;
        for (String line : Files.lines(resource).toList()) {
            sum += new Pair(line).calcPuzzle1();
        }
        return sum;
    }

    public int resolvePuzzle2() throws Exception {
        Path resource = Paths.get(getClass().getResource("/y2022/input_day2.txt").toURI());

        int sum = 0;
        for (String line : Files.lines(resource).toList()) {
            sum += new Pair(line).calcPuzzle2();
        }
        return sum;
    }

    @Value
    class Pair {
        /** A/B/C -> Rock/Paper/Scissors */
        char first;

        /**
         * Puzzle 1: X/Y/Z -> Rock/Paper/Scissors
         * Puzzle 2: X/Y/Z -> Need to Lose/Draw/Win
         */
        char last;

        private Pair(String value) {
            first = value.charAt(0);
            last = value.charAt(2);
        }

        int calcPuzzle1() {
            int opp = ((int) first) - 64;
            int you = ((int) last) - 87;
            int result;
            if (opp == you) {
                result = 3; // draw
            } else if (opp == (you == 1 ? 4 : you) - 1) {
                result = 6; // win
            } else {
                result = 0; // lose
            }
            return you + result;
        }

        int calcPuzzle2() {
            int opp = ((int) first) - 64;
            int result = (((int) last) - 88) * 3;
            int you;
            if (last == 'X') {
                you = (opp == 1 ? 4 : opp) - 1; // lose
            } else if (last == 'Y') {
                you = opp; // draw
            } else {
                you = (opp == 3 ? 0 : opp) + 1; // win
            }
            return you + result;
        }
    }
}
