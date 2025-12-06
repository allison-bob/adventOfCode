package bob.aoc;

import bob.parser.LineObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17b extends BaseClass<List<List<Day17Direction>>> {
    
    private static record ColumnData(long ht, long rockct) {
    }

    private static final long ROCK_CT = 1000000000000L;
    private List<Day17Direction> directions;
    private int dirpos = 0;
    private final List<Integer> column = new ArrayList<>();
    private long baseht = 0;
    private final Map<Day17State, ColumnData> cache = new HashMap<>();

    public static void main(String[] args) {
        new Day17b().run(args, "");
    }

    public Day17b() {
        super(false);
        setParser(new LineObjectParser<>(Day17Direction::byChar));
    }

    @Override
    public void solve(List<List<Day17Direction>> data) {
        Assert.that((data.size() == 1), "Too many lines read");
        directions = data.get(0);
        LOG.info("Read {} directions", directions.size());
        
        // Add the bottom of the column
        column.add(0x7F);

        Day17Rock currRock = Day17Rock.DASH;
        for (long i = 0; i < ROCK_CT; i++) {
            dropRock(currRock);
            //LOG.debug("i={}, column={}", i, column.size());
            i += cacheColumn(currRock, i);
            //LOG.debug("       baseht={}, column={}", baseht, column.size());
            currRock = currRock.getNext();
        }

        LOG.info("Answer is {} + {} = {}", baseht, (column.size() - 1), (baseht + column.size() - 1));
    }

    private long cacheColumn(Day17Rock currRock, long rocks) {
        Day17State state = new Day17State(currRock, dirpos);
        ColumnData data = new ColumnData(column.size(), rocks);
        
        // Find the first rock in each column
        int[] depths = state.getColumnDepth();
        int notFound = 7;
        for (int i = 0; ((i < column.size()) && (notFound > 0)); i++) {
            int thisrow = column.get(column.size() - i - 1);
            for (int j = 0; j < depths.length; j++) {
                if (((thisrow % 2) == 1) && (depths[j] < 0)) {
                    depths[j] = i;
                }
                thisrow /= 2;
            }
        }
        
        // Make sure all columns have depths
        for (int i = 0; i < depths.length; i++) {
            if (depths[i] < 0) {
                depths[i] = column.size();
            }
        }
        
        // Add to cache
        if (cache.containsKey(state)) {
            ColumnData oldval = cache.get(state);
            LOG.debug("At {}, {} duplicates {}", data, state, oldval);
            long cycleLen = data.rockct() - oldval.rockct();
            long cyclesToEnd = (ROCK_CT - oldval.rockct() - 1) / cycleLen;
            baseht = (cyclesToEnd - 1) * (data.ht() - oldval.ht());
            LOG.debug("       Need {} cycles of {}", cyclesToEnd, cycleLen);
            cache.clear();
            return (cyclesToEnd - 1) * cycleLen;
        } else {
            cache.put(state, data);
            LOG.debug("At {}, caching {}", data, state);
            return 0;
        }
    }

    private void dropRock(Day17Rock rock) {
        boolean rested = false;
        int[] dropping = Arrays.copyOf(rock.getShape(), 4);
        int ht = column.size() + 4;

        // Lower the rock until it rests on something
        while (!rested) {
            ht--;
            //LOG.debug("Dropping {} from {}", Day17Rock.asString(dropping), ht);
            int[] newrock = moveHoriz(dropping, ht);
            rested = moveVert(newrock, ht);
            dropping = newrock;
            dirpos = (dirpos + 1) % directions.size();
        }

        // Add the rock to the column
        for (int i = 0; i < dropping.length; i++) {
            setRow((ht + i), dropping[i]);
        }
    }

    private int[] moveHoriz(int[] rock, int ht) {
        int[] retval = new int[rock.length];
        switch (directions.get(dirpos)) {
            case LEFT -> {
                //LOG.debug("Moving left {}", Day17Rock.asString(rock));
                for (int i = 0; i < rock.length; i++) {
                    if (rock[i] > 63) {
                        // Bumped against side wall
                        return rock;
                    }
                    retval[i] = rock[i] << 1;
                    if ((retval[i] & getRow(ht + i)) != 0) {
                        // Bumped against resting rock
                        return rock;
                    }
                }
            }
            case RIGHT -> {
                //LOG.debug("Moving right {}", Day17Rock.asString(rock));
                for (int i = 0; i < rock.length; i++) {
                    if ((rock[i] % 2) == 1) {
                        // Bumped against side wall
                        return rock;
                    }
                    retval[i] = rock[i] >> 1;
                    if ((retval[i] & getRow(ht + i)) != 0) {
                        // Bumped against resting rock
                        return rock;
                    }
                }
            }
        }
        //LOG.debug("    Moved to {}", Day17Rock.asString(retval));
        return retval;
    }

    private boolean moveVert(int[] rock, int ht) {
        //LOG.debug("Moving vert from ht={}", ht);
        for (int i = 0; i < rock.length; i++) {
            if ((rock[i] & getRow(ht + i - 1)) != 0) {
                return true;
            }
        }
        return false;
    }

    private int getRow(int ht) {
        if (ht >= column.size()) {
            return 0;
        }
        return column.get(ht);
    }

    private void setRow(int ht, int rock) {
        if (rock != 0) {
            if (ht > column.size()) {
                throw Assert.failed(null, "A row got skipped");
            } else if (ht == column.size()) {
                column.add(rock);
            } else {
                column.set(ht, (column.get(ht) | rock));
            }
        }
    }
}
