package de.geburtig.advent.y2022;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day6 {

    public int resolvePuzzle1() throws Exception {
        Path resource = Paths.get(getClass().getResource("/y2022/input_day6.txt").toURI());

        String seq = Files.readAllLines(resource).get(0);
        for (int i = 4; i < seq.length(); ++i) {
            if (seq.substring(i - 4, i).chars().distinct().count() == 4) {
                return i;
            }
        }
        return -1;
    }

    public int resolvePuzzle2() throws Exception {
        Path resource = Paths.get(getClass().getResource("/y2022/input_day6.txt").toURI());

        String seq = Files.readAllLines(resource).get(0);
        for (int i = 14; i < seq.length(); ++i) {
            if (seq.substring(i - 14, i).chars().distinct().count() == 14) {
                return i;
            }
        }
        return -1;
    }
}
