package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import de.geburtig.advent.util.Permutation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends DayBase {

    private final List<Valve> valves = new ArrayList<>();

    public String resolveExample1() {
        initValvesFromInputFile("/input_day16_example.txt");
        Graph graph = new Graph();
        Valve startPos = valves.stream().filter(v -> v.name.equals("AA")).findAny().orElseThrow();
        List<Valve> relevantValves = valves.stream().filter(v -> v.flowRate > 0).toList();

        Game game = new Game(graph, startPos, new ArrayList<>(relevantValves), 30);
        PathElement optionsGraph = game.createOptionsGraph();
        PathElement highscore = optionsGraph.scanForHighestScore(optionsGraph);

        return highscore.score + "";
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        initValvesFromInputFile("/input_day16.txt");
        Graph graph = new Graph();
        Valve startPos = valves.stream().filter(v -> v.name.equals("AA")).findAny().orElseThrow();
        List<Valve> relevantValves = valves.stream().filter(v -> v.flowRate > 0).toList();

        Game game = new Game(graph, startPos, new ArrayList<>(relevantValves), 30);
        PathElement optionsGraph = game.createOptionsGraph();
        PathElement highscore = optionsGraph.scanForHighestScore(optionsGraph);

        return highscore.score + "";
    }

    public String resolveExample2() {
        initValvesFromInputFile("/input_day16_example.txt");
        List<Valve> relevantValves = valves.stream().filter(v -> v.flowRate > 0).toList();

        Permutation<String> perm = new Permutation<>(relevantValves.stream().map(Valve::name).toList());
        Set<String> combinations = new HashSet<>();
        perm.forEach(strings -> {
            List<String> rel = strings.subList(0, 3).stream().sorted().toList();
            combinations.add(rel.get(0) + rel.get(1) + rel.get(2));
        });
        List<List<String>> p1 = combinations.stream().map(t -> List.of(t.split(("(?<=\\G..)")))).toList();
        System.out.println("Got " + p1.size() + " possible solutions.");

        Graph graph = new Graph();
        Valve startPos = valves.stream().filter(v -> v.name.equals("AA")).findAny().orElseThrow();

        int maxScore = 0;
        PathElement h1 = null, h2 = null;
        for (List<String> valveNamesForP1 : p1) {
            List<Valve> rel = relevantValves.stream().filter(v -> valveNamesForP1.contains(v.name)).toList();

            // Simulate Player
            Game game = new Game(graph, startPos, new ArrayList<>(rel), 26);
            PathElement optionsGraph = game.createOptionsGraph();
            PathElement highscore1 = optionsGraph.scanForHighestScore(optionsGraph);

            // Simulate Elephant
            rel = relevantValves.stream().filter(v -> !valveNamesForP1.contains(v.name)).toList();
            game = new Game(graph, startPos, new ArrayList<>(rel), 26);
            optionsGraph = game.createOptionsGraph();
            PathElement highscore2 = optionsGraph.scanForHighestScore(optionsGraph);

            if (maxScore < highscore1.score + highscore2.score) {
                maxScore = highscore1.score + highscore2.score;
                h1 = highscore1;
                h2 = highscore2;
            }
        }

        System.out.println("Player:   " + h1.path() + " (" + h1.score + ")");
        System.out.println("Elephant: " + h2.path() + " (" + h2.score + ")");
        return maxScore + "";
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        initValvesFromInputFile("/input_day16.txt");
        Graph graph = new Graph();
        Valve startPos = valves.stream().filter(v -> v.name.equals("AA")).findAny().orElseThrow();
        List<Valve> relevantValves = valves.stream().filter(v -> v.flowRate > 0).toList();
        System.out.println(relevantValves.size());

        Permutation<String> perm = new Permutation<>(relevantValves.stream().map(Valve::name).toList());
        System.out.println(perm.getMax());

        Set<String> combinations = ConcurrentHashMap.newKeySet();

        int period = 5;
        AtomicInteger secs = new AtomicInteger(0);
        AtomicLong count = new AtomicLong();
        List<String> processed = new ArrayList<>();

        AtomicInteger maxScore = new AtomicInteger();
        List<String> maxScoreCombination = new ArrayList<>(1);
        maxScoreCombination.add("<init>");

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            double percentage = (double) count.get() / perm.getMax();
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(12);

            System.out.println("Got " + combinations.size() + " combinations after " + secs.addAndGet(period) + " seconds and " + count + " iterations (" + df.format(percentage) + ")");
            System.out.println(" -> Max score " + maxScore.get() + " out of " + processed.size() + " simulations (" + maxScoreCombination.get(0) + ")");
        }, period, period, TimeUnit.SECONDS);

        Executors.newSingleThreadExecutor().execute(() -> {
            do {
                for (String combination : combinations) {
                    if (processed.contains(combination)) continue;

                    List<String> rel = List.of(combination.split("(?<=\\G..)"));
                    int score = processPuzzle2(graph, startPos, relevantValves, rel);
                    processed.add(combination);

                    if (score > maxScore.get()) {
                        maxScore.set(score);
                        maxScoreCombination.set(0, combination);
                    }
                }
            } while (true);
        });

        perm.forEach(strings -> {
            List<String> rel = strings.subList(0, 7).stream().sorted().toList();
            combinations.add(rel.get(0) + rel.get(1) + rel.get(2) + rel.get(3) + rel.get(4) + rel.get(5) + rel.get(6));
            count.incrementAndGet();
        });

        List<List<String>> p1 = combinations.stream().map(t -> List.of(t.split(("(?<=\\G..)")))).toList();
        System.out.println("Got " + p1.size() + " possible solutions.");

        return "0";
    }

    int processPuzzle2(Graph graph, Valve startPos, List<Valve> relevantValves, List<String> valveNamesP1) {
        List<Valve> p1Valves = relevantValves.stream().filter(v -> valveNamesP1.contains(v.name)).toList();
        List<Valve> p2Valves = relevantValves.stream().filter(v -> !valveNamesP1.contains(v.name)).toList();

        Game game = new Game(graph, startPos, new ArrayList<>(p1Valves), 26);
        PathElement optionsGraph = game.createOptionsGraph();
        PathElement highscoreP1 = optionsGraph.scanForHighestScore(optionsGraph);

        game = new Game(graph, startPos, new ArrayList<>(p2Valves), 26);
        optionsGraph = game.createOptionsGraph();
        PathElement highscoreP2 = optionsGraph.scanForHighestScore(optionsGraph);

        return highscoreP1.score + highscoreP2.score;
    }

    void initValvesFromInputFile(final String inputFile) {
        valves.clear();
        final Pattern pattern = Pattern.compile("^Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.+)$");

        List<String> lines = getLinesFromInputFile(inputFile);
        for (int i = 0; i < lines.size(); ++i) {
            String line = lines.get(i);
            Matcher matcher = pattern.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException("Unparsable input line \"" + line +  "\"");
            }
            String name = matcher.group(1);
            int flowRate = Integer.parseInt(matcher.group(2));
            String neighbours = matcher.group(3);
            valves.add(new Valve(i, name, flowRate, Arrays.stream(neighbours.split(", ")).toList()));
        }
    }
    @RequiredArgsConstructor
    class Game {
        private final Graph graph;
        private @NonNull Valve startPos;

        /** Relevant are all valves that are suitable for a throughput and therefore worth opening */
        private final List<Valve> relevantValves;

        private final int minutes;

        /**
         * Create a tree path with all useful options. Each result contains the score being resulted by it.
         */
        public PathElement createOptionsGraph() {
            PathElement root = new PathElement(0, minutes, startPos, null);
            root.relevantValves.addAll(relevantValves);
            scanOptions(root);
            return root;
        }

        /** Scan the given PathElement for the most valuable path through the tunnel */
        private void scanOptions(PathElement parent) {
            List<Option> options = parent.relevantValves.stream().map(valve -> {
                int dist = graph.distanceBetween(parent.valve, valve);
                return new Option(valve, dist, Math.max(parent.minutesLeft - dist - 1, 0) * valve.flowRate);
            }).filter(o -> o.score > 0).toList();
            options.forEach(o -> {
                PathElement child = new PathElement(parent.score + o.score, parent.minutesLeft - o.dist - 1, o.valve, parent);
                child.relevantValves.addAll(parent.relevantValves.stream().filter(v -> !v.name.equals(o.valve.name)).toList());
                parent.children.add(child);
                scanOptions(child);
            });
        }
    }

    @Value
    @ToString(of = {"score"})
    static class PathElement {
        int score;
        int minutesLeft;
        Valve valve;
        List<Valve> relevantValves = new ArrayList<>();
        PathElement parent;
        List<PathElement> children = new ArrayList<>();

        PathElement scanForHighestScore(PathElement elem) {
            PathElement highest = null;
            for (PathElement child : elem.children) {
                PathElement res = scanForHighestScore(child);
                if (highest == null || res.score > highest.score) {
                    highest = res;
                }
            }
            return highest != null ? highest : elem;
        }

        public String path() {
            return (parent != null ? parent.path() + " -> " : "") + valve.name;
        }
    }

    /**
     * Graph for all pathways through the tunnel, using floyd warshall algorithm to resolve the shortest distances
     * between all valves.
     */
    class Graph {
        private final static int INF = 999_999;
        private final int[][] dist;
        private final List<String> valveNames;

        public Graph() {
            dist = new int[valves.size()][valves.size()];
            for (int x = 0; x < valves.size(); ++x) {
                for (int y = 0; y < valves.size(); y++) {
                    dist[x][y] = (x==y) ? 0 : INF;
                }
            }

            valveNames = valves.stream().map(Valve::name).toList();

            for (int i = 0; i < valves.size(); ++i) {
                Valve valve = valves.get(i);
                for (String neighbour : valve.adjoining) {
                    int n = valveNames.indexOf(neighbour);
                    dist[i][n] = 1;
                }
            }

            // Resolve distances between all node pairs
            adoptFloydWarshallAlgorithm();
        }

        String out(int x, int y) {
            return dist[x][y] == INF ? " " : dist[x][y] + "";
        }

        void adoptFloydWarshallAlgorithm() {
            for (int k = 0; k < dist.length; ++k) {
                for (int i = 0; i < dist.length; ++i) {
                    for (int j = 0; j < dist.length; ++j) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }

        public int distanceBetween(Valve from, Valve to) {
            return dist[valveNames.indexOf(from.name)][valveNames.indexOf(to.name)];
        }
    }

    record Option(Valve valve, int dist, int score) {}

    record Valve(int index, String name, int flowRate, List<String> adjoining) {}
}
