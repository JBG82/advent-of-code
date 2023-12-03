package de.geburtig.advent.y2022;

import de.geburtig.advent.base.DayBase;
import lombok.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day19 extends DayBase {

    public String resolveExample1() {
        Arrays.stream(Resource.values()).sorted(Comparator.comparing(Resource::ordinal).reversed()).forEach(System.out::println);
        return "blah";
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        return "blah";
    }

    public String resolveExample2() {
        return "blah";
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        return "blah";
    }

/*
    private class Game {
        int turn = 0;
        final Blueprint blueprint;
        final List<Robot> robots = new ArrayList<>();
        final Stock stock = new Stock();

        Game(Blueprint blueprint) {
            this.blueprint = blueprint;
            robots.add(blueprint.getRobotProducing(Resource.ore));
        }

        void turn() {
            ++turn;
            robots.stream().map(Robot::produces).forEach(p -> stock.add(p));
        }
    }
*/

    @Value
    class State {
        /** Current game minute */
        int turn;
        /** Blueprint for building robots */
        Blueprint blueprint;
        /** Robots in possession */
        List<Robot> robots;
        /** Resources in possession */
        Stock stock;
        /** Options for action each mapped to the situation they are leading to */
        Map<Option, State> optionsMap;

        State process(Option option) {
            ArrayList<Robot> newRobots = new ArrayList<>(robots);
            Stock newStock = stock.copy();
            Map<Option, State> newOptions = new HashMap<>();
            if (turn < 24) {
                newOptions.put(Option.NOTHING, null);
            }

            // TODO: Set newRobots and newStock according to given option

            // TODO: Evaluate possible options

            State newState = new State(turn + 1, blueprint, newRobots, newStock, newOptions);
            if (optionsMap.put(option, newState) != null) {
                throw new RuntimeException("New state for option " + option + " already set!");
            }
            return newState;
        }
    }

    @Value
    private class Blueprint {
        int number;
        List<Robot> robots;

        Optional<Robot> produce(Stock stock) {
            return Optional.empty();
        }

        Robot getRobotProducing(Resource resource) {
            return robots.stream().filter(r -> r.produces == resource).findAny().orElseThrow(() -> new RuntimeException("No producer of " + resource + " found in blueprint " + number));
        }
    }

    private class Stock {
        final Map<Resource, AtomicInteger> map = Map.of(
                Resource.ore, new AtomicInteger(),
                Resource.clay, new AtomicInteger(),
                Resource.obsidian, new AtomicInteger(),
                Resource.geode, new AtomicInteger()
        );

        void add(Resource resource) {
            map.get(resource).getAndIncrement();
        }

        void subtract(Cost cost) {
            map.get(cost.resouce).addAndGet(cost.amount * (-1));
        }

        void reduce(Cost... costs) {
            Arrays.stream(costs).forEach(this::subtract);
        }

        public Stock copy() {
            Stock result = new Stock();
            result.map.get(Resource.ore).set(map.get(Resource.ore).intValue());
            result.map.get(Resource.clay).set(map.get(Resource.clay).intValue());
            result.map.get(Resource.obsidian).set(map.get(Resource.obsidian).intValue());
            result.map.get(Resource.geode).set(map.get(Resource.geode).intValue());
            return result;
        }
    }

    record Robot(Resource produces, Cost... costs) {}

    record Cost(Resource resouce, int amount) {}

    private enum Resource {
        ore, clay, obsidian, geode
    }

    record Option(List<Action> actions) {
        static Option NOTHING = new Option(List.of(Action.none));
    }

    private enum Action {
        none(null),
        build_ore_robot(Resource.ore),
        build_clay_robot(Resource.clay),
        build_obsidian_robot(Resource.obsidian),
        build_geode_robot(Resource.geode);

        @Getter
        private final Resource resource;

        Action(final Resource resource) {
            this.resource = resource;
        }

        boolean isBuildAction() {
            return resource != null;
        }
    }
}
