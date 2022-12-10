package de.geburtig.advent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day1 {

    public int resolvePuzzle1() throws Exception {
        Path resource = Paths.get(getClass().getResource("/input_day1.txt").toURI());

        int sum = 0;
        int max = 0;
        for (String line : Files.lines(resource).toList()) {
            if (line.length() != 0) {
                sum += Integer.parseInt(line);
            } else {
                max = Math.max(sum, max);
                sum = 0;
            }
        }
        return max;
    }

    public int resolvePuzzle2() throws Exception {
        Path resource = Paths.get(getClass().getResource("/input_day1.txt").toURI());

        List<Integer> list = new ArrayList<>(Arrays.asList(0, 0, 0));

        int sum = 0;
        for (String line : Files.lines(resource).toList()) {
            if (line.length() != 0) {
                sum += Integer.parseInt(line);
            } else {
                if (sum > list.get(0)) {
                    list.add(0, sum);
                    list.remove(3);
                } else if (sum > list.get(1)) {
                    list.add(1, sum);
                    list.remove(3);
                } else if (sum > list.get(2)) {
                    list.add(2, sum);
                    list.remove(3);
                }
                sum = 0;
            }
        }
        return list.stream().mapToInt(Integer::intValue).sum();
    }

}
