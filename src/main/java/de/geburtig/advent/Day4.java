package de.geburtig.advent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day4 {

    public int resolvePuzzle1() throws Exception {
        Path resource = Paths.get(getClass().getResource("/input_day4.txt").toURI());

        int sum = 0;
        String[] elf;
        String[] part;
        for (String line : Files.lines(resource).toList()) {
            elf = line.split(",");
            part = elf[0].split("-");
            int min1 = Integer.parseInt(part[0]);
            int max1 = Integer.parseInt(part[1]);
            part = elf[1].split("-");
            int min2 = Integer.parseInt(part[0]);
            int max2 = Integer.parseInt(part[1]);
            if ((min1 <= min2 && max1 >= max2) || (min1 >= min2 && max1 <= max2)) {
                ++sum;
            }
        }
        return sum;
    }

    public int resolvePuzzle2() throws Exception {
        Path resource = Paths.get(getClass().getResource("/input_day4.txt").toURI());

        int sum = 0;
        String[] elf;
        String[] part;
        for (String line : Files.lines(resource).toList()) {
            elf = line.split(",");
            part = elf[0].split("-");
            int min1 = Integer.parseInt(part[0]);
            int max1 = Integer.parseInt(part[1]);
            part = elf[1].split("-");
            int min2 = Integer.parseInt(part[0]);
            int max2 = Integer.parseInt(part[1]);
            if ((min1 <= min2 && max1 >= min2) || (min2 <= min1 && max2 >= min1)) {
                ++sum;
            }
        }
        return sum;
    }
}
