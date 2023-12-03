package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class Day2 {
    public static void main(final String[] args) {
//        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day2_example1.txt", Day2.class);
        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day2.txt", Day2.class);

        List<Game> games = new ArrayList<>();
        for (String line : lines) {
            int id = Integer.parseInt(line.substring(5, line.indexOf(":")));
            Game game = new Game(id);
            String[] split = line.substring(line.indexOf(":") + 1).split(";");
            for (String pull : split) {
                String[] coloredCubes = pull.split(",");
                int blue = 0, green = 0, red = 0;
                for (String cube : coloredCubes) {
                    int amount = Integer.parseInt(cube.substring(1, cube.indexOf(" ", 1)));
                    if (cube.endsWith("blue")) {
                        blue = amount;
                    } else if (cube.endsWith("green")) {
                        green = amount;
                    } else if (cube.endsWith("red")) {
                        red = amount;
                    } else {
                        throw new IllegalStateException("What!? " + cube);
                    }
                }
                game.getPulls().add(new Pull(red, green, blue));
            }
            games.add(game);
        }

        Hypothesis hypothesis1 = new Hypothesis(12, 13, 14);
        List<Game> possibleGames = hypothesis1.resolvePossibleGames(games);
        int result = possibleGames.stream().mapToInt(Game::getId).sum();
        System.out.println("Result 1: " + result);

        result = games.stream().mapToInt(g -> g.resolveMinPull().multiply()).sum();
        System.out.println("Result 2: " + result);
    }

    @Data
    static class Game {
        private final int id;
        private final List<Pull> pulls = new ArrayList<>();

        Pull resolveMinPull() {
            int minRed = pulls.stream().mapToInt(Pull::red).max().orElse(0);
            int minBlue = pulls.stream().mapToInt(Pull::blue).max().orElse(0);
            int minGreen = pulls.stream().mapToInt(Pull::green).max().orElse(0);
            return new Pull(minRed, minGreen, minBlue);
        }
    }

    record Pull(int red, int green, int blue) {
        public int multiply() {
            return red * green * blue;
        }
    }

    record Hypothesis(int red, int green, int blue) {

        boolean checkPossible(Game game) {
            return game.pulls.stream().allMatch(this::checkPossible);
        }

        boolean checkPossible(Pull pull) {
            return pull.red <= red && pull.green <= green && pull.blue <= blue;
        }

        public List<Game> resolvePossibleGames(final List<Game> games) {
            return games.stream().filter(this::checkPossible).toList();
        }
    }

}
