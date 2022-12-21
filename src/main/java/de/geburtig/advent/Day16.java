package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends DayBase {

    private final List<Valve> valves = new ArrayList<>();

    public String resolveExample1() {
        initValvesFromInputFile("/input_day16_example.txt");
        Graph graph = new Graph();
        Valve startPos = valves.stream().filter(v -> v.name.equals("AA")).findAny().orElseThrow();
        List<Valve> relevantValves = valves.stream().filter(v -> v.flowRate > 0).toList();

        Game game = new Game(graph, startPos, new ArrayList<>(relevantValves));
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

        Game game = new Game(graph, startPos, new ArrayList<>(relevantValves));
        PathElement optionsGraph = game.createOptionsGraph();
        PathElement highscore = optionsGraph.scanForHighestScore(optionsGraph);

        return highscore.score + "";
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        return null;
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

        /**
         * Create a tree path with all useful options. Each result contains the score being resulted by it.
         */
        public PathElement createOptionsGraph() {
            PathElement root = new PathElement(0, 30, startPos, null);
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
