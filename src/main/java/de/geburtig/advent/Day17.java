package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.Setter;
import lombok.Value;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static de.geburtig.advent.Day17.Chamber.CHECK_FOR_MATRIX_REDUCE_AT;
import static de.geburtig.advent.Day17.Chamber.MATRIX_HEIGHT;

public class Day17 extends DayBase {

    public String resolveExample1() {
        Game game = new Game(getLinesFromInputFile("/input_day17_example.txt").get(0));
        game.setEndObjectDropsAfter(2022);
        game.process();
        return String.valueOf(game.chamber.overallTowerHeight);
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        Game game = new Game(getLinesFromInputFile("/input_day17.txt").get(0));
        game.setEndObjectDropsAfter(2022);
        game.process();
        return String.valueOf(game.chamber.overallTowerHeight);
    }

    public String resolveExample2() {
        Game game = new Game(getLinesFromInputFile("/input_day17_example.txt").get(0));
        game.setEndObjectDropsAfter(1_000_000_000_000L);
        game.setRepetitionOptimization(true);

        int period = 10;
        AtomicInteger secs = new AtomicInteger();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            double l = (double) game.chamber.objectCount / 1_000_000_000_000L * 100;
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);

            System.out.println("Tower height after " + secs.addAndGet(period) + " seconds and " + game.chamber.objectCount + " fallen rocks: " + game.chamber.overallTowerHeight + " (" + df.format(l) + "%)");
        }, period, period, TimeUnit.SECONDS);

        game.process();
        return String.valueOf(game.chamber.overallTowerHeight);
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        Game game = new Game(getLinesFromInputFile("/input_day17.txt").get(0));
        game.setEndObjectDropsAfter(1_000_000_000_000L);
        game.setRepetitionOptimization(true);
        game.process();
        return String.valueOf(game.chamber.overallTowerHeight);
    }

    record Drop(int obj, int x) {};

    record State(long objects, long towerHeight) {};

    class Game {
        private final Chamber chamber = new Chamber();
        int seqIdx = 0;
        String moveSequence;
        private @Setter long endObjectDropsAfter = 0;

        private @Setter boolean repetitionOptimization = false;
        private int dropSequenceIdx = 0;
        private final List<Drop> dropSequence = new ArrayList<>(5);
        private State lastRepetitionState = null;

        public Game(String moveSequence) {
            this.moveSequence = moveSequence;
            makeFirst4Movements();
        }

        char nextMoveFromSequence() {
            char c = moveSequence.charAt(seqIdx++);
            seqIdx %= moveSequence.length();
            return c;
        }

        boolean turnAndCheckForEnd() {
            if (chamber.rock.downFree()) {
                --chamber.rock.y;

                // and move again
                if (nextMoveFromSequence() == '<') {
                    chamber.rock.moveLeftIfPossible();
                } else {
                    chamber.rock.moveRightIfPossible();
                }
            } else {
                if (repetitionOptimization && chamber.objectCount > 1000) {
                    // Search for repetitions in the drop pattern
                    if (dropSequence.size() < 5) {
                        dropSequence.add(new Drop(chamber.rock.shape.id, chamber.rock.x));
                    } else {
                        Drop drop = dropSequence.get(dropSequenceIdx);
                        if (dropSequence.size() < 10) {
                            dropSequence.add(new Drop(chamber.rock.shape.id, chamber.rock.x));
                        }
                        if (drop.obj == chamber.rock.shape.id && drop.x == chamber.rock.x) {
                            if (++dropSequenceIdx == dropSequence.size()) {
                                State state = new State(chamber.objectCount, chamber.overallTowerHeight);
                                if (lastRepetitionState != null) {
                                    long objectDelta = state.objects - lastRepetitionState.objects;
                                    long towerGrowth = state.towerHeight - lastRepetitionState.towerHeight;
                                    System.out.println("Got sequence after " + chamber.objectCount + " drops: objectDelta=" + objectDelta + ", towerGrowth=" + towerGrowth);

                                    System.out.println("Performing repetition...");
                                    while (chamber.objectCount + objectDelta < endObjectDropsAfter) {
                                        chamber.objectCount += objectDelta;
                                        chamber.overallTowerHeight += towerGrowth;
                                    }
                                    System.out.println("Done repetition sequence.");
                                }
                                lastRepetitionState = state;
                                dropSequenceIdx = 0;
                            }
                        } else {
                            dropSequenceIdx = 0;
                        }
                    }
                }

                chamber.fixRockAndInitNextOne();
                makeFirst4Movements();
                checkAndCutMatrix();
            }

            return chamber.objectCount > endObjectDropsAfter;
        }

        private void checkAndCutMatrix() {
            if (chamber.rock.y + 3 >= CHECK_FOR_MATRIX_REDUCE_AT) {
                int newGround = Arrays.stream(chamber.colMin).min().orElseThrow();
                if (newGround > 0) {
                    byte[][] tmp = new byte[7][MATRIX_HEIGHT];

                    for (int x = 0; x < tmp.length; ++x) {
                        System.arraycopy(chamber.matrix[x], newGround, tmp[x], 0, tmp[x].length - newGround);
                        chamber.colMin[x] -= newGround;
                    }
                    chamber.matrix = tmp;

                    chamber.rock.y -= newGround;
                    chamber.towerHeight -= newGround;
                }
            }
        }

        private void makeFirst4Movements() {
            int movement = 0;
            for (int i = 0; i < 4; ++i) {
                char c = nextMoveFromSequence();
                if (c == '<' && movement > -2) {
                    --movement;
                } else if (c == '>' && movement + chamber.rock.shape.width < 5) {
                    ++movement;
                }
            }
            chamber.rock.x += movement;
        }

        public void process() {
            do {
                if (turnAndCheckForEnd()) break;
            } while (true);
        }
    }

    class Chamber {
        static final int MATRIX_HEIGHT = 9999;
        static final int CHECK_FOR_MATRIX_REDUCE_AT = 9990;
        private byte[][] matrix = new byte[7][MATRIX_HEIGHT];
        private final int[] colMin = new int[7];
        private int towerHeight = 0;
        private long overallTowerHeight = 0;
        private long objectCount = 0;
        private Rock rock;

        public Chamber() {
            for (int x = 0; x < matrix.length; ++x) matrix[x][0] = 1; // Set the ground
            initNewRock();
        }

        public void fixRockAndInitNextOne() {
            rock.shape.fixOnMatrix(rock);

            int tmp = towerHeight;
            towerHeight = Math.max(towerHeight, rock.y + rock.shape.height - 1);
            overallTowerHeight += towerHeight - tmp;

            initNewRock();
        }

        private void initNewRock() {
            rock = new Rock(Shape.of(objectCount++ % Shape.SHAPES.length), this);
        }
    }

    class Rock {
        private final Shape shape;
        private int x = 2;
        private int y;
        private final Chamber chamber;

        public Rock(Shape shape, Chamber chamber) {
            this.shape = shape;
            this.chamber = chamber;
            y = chamber.towerHeight + 1;
        }

        public boolean downFree() {
            return shape.downFree(chamber.matrix, x, y);
        }

        public boolean rightFree() {
            return shape.rightFree(chamber.matrix, x, y);
        }

        public boolean leftFree() {
            return shape.leftFree(chamber.matrix, x, y);
        }

        public void moveRightIfPossible() {
            if (rightFree()) ++x;
        }

        public void moveLeftIfPossible() {
            if (leftFree()) --x;
        }
    }

    @Value
    static class Shape {

        // ACHTUNG: Die Anordnung der Dimensionen ist hier anders als bei der Chamber-Matrix!!!
        // Hier gilt shape[y][x], in der Matrix hingegen matrix[x][y]
        public static final byte[][][] SHAPES = {
                {
                        {1, 1, 1, 1}
                },{
                        {0, 1, 0},
                        {1, 1, 1},
                        {0, 1, 0},
                },{
                        {0, 0, 1},
                        {0, 0, 1},
                        {1, 1, 1},
                },{
                        {1},
                        {1},
                        {1},
                        {1},
                },{
                        {1, 1},
                        {1, 1},
                },
        };

        int id;
        byte[][] shape;
        int[] lowest;
        int width, height;

        private static final Map<Integer, Shape> shapes = new HashMap<>(5);

        private Shape(int object) {
            this.id = object;
            this.shape = SHAPES[object];
            this.width = shape[0].length;
            this.height = shape.length;
            this.lowest = new int[width];

            for (int x = 0; x < width; ++x) {
                for (int y = height - 1; y >= 0; --y) {
                    if (shape[y][x] == 1) {
                        lowest[x] = Math.abs(y - height) - 1;
                        break;
                    }
                }
            }
        }

        static Shape of(long object) {
            return shapes.computeIfAbsent((int)object, Shape::new);
        }

        public void fixOnMatrix(Rock rock) {
            int posX = rock.x;
            int posY = rock.y;
            for (int x = posX, sx = 0; x < posX + width; ++x, ++sx) {
                for (int y = posY, sy = height - 1; y < posY + height; ++y, --sy) {
                    if (shape[sy][sx] == 1) {
                        rock.chamber.matrix[x][y] = 1;
                        rock.chamber.colMin[x] = Math.max(rock.chamber.colMin[x], y);
                    }
                }
            }
        }

        public boolean downFree(byte[][] matrix, int posX, int posY) {
            for (int x = posX, sx = 0; x < posX + width; ++x, ++sx) {
                if (matrix[x][posY + lowest[sx] - 1] == 1) return false;
            }
            return true;
        }

        public boolean rightFree(byte[][] matrix, int posX, int posY) {
            if (posX + width >= matrix.length) return false;

            posY = posY + height - 1;
            for (int x = posX, sx = 0; x < posX + width; ++x, ++sx) {
                for (int y = posY, sy = 0; sy < height; --y, ++sy) {
                    if (shape[sy][sx] == 1 && matrix[x + 1][y] != 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean leftFree(byte[][] matrix, int posX, int posY) {
            if (posX == 0) return false;

            posY = posY + height - 1;
            for (int x = posX, sx = 0; x < posX + width; ++x, ++sx) {
                for (int y = posY, sy = 0; sy < height; --y, ++sy) {
                    if (shape[sy][sx] == 1 && matrix[x - 1][y] != 0) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
