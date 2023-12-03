package de.geburtig.advent.y2022;

import de.geburtig.advent.base.DayBase;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends DayBase {

    @Override
    public String resolvePuzzle1() throws Exception {
        CPU cpu = new CPU();
        List<String> lines = getLinesFromInputFile("/y2022/input_day10.txt");
        lines.forEach(cpu::process);
        return String.valueOf(cpu.signals.stream().mapToInt(Integer::intValue).sum());
    }

    @Override
    public String resolvePuzzle2() {
        CPU cpu = new CPU();
        List<String> lines = getLinesFromInputFile("/y2022/input_day10.txt");
        lines.forEach(cpu::process);
        cpu.renderOutput();
        return "null";
    }

    class CPU {
        private int x = 1;
        private int tickCount;

        private String output = "";

        private final List<Integer> signals = new ArrayList<>();

        void process(final String command) {
            if (command.equals("noop")) {
                tick();
                return;
            }
            String[] part = command.split(" ");
            if (part[0].equals("addx")) {
                int value = Integer.parseInt(part[1]);
                tick();
                tick();
                x += value;
                return;
            }
            throw new IllegalArgumentException("Unexpected command: \"" + command + "\"");
        }

        void tick() {
            int pixelToDraw = tickCount % 40;
            output += (x >= pixelToDraw - 1 && x <= pixelToDraw + 1) ? "#" : ".";

            if (++tickCount % 40 == 20) {
                signals.add(tickCount * x);
            }
        }

        public void renderOutput() {
            for (int i = 40; i <= output.length(); i+=40) {
                System.out.println(output.substring(i-40, i));
            }
        }
    }
}
