package bob.aoc;

import bob.parser.LineObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17a extends BaseClass<List<List<Day17Direction>>> {

    private List<Day17Direction> directions;
    private int dirpos = 0;
    private final List<Integer> column = new ArrayList<>();

    public static void main(String[] args) {
        new Day17a().run(args, "");
    }

    public Day17a() {
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
        for (int i = 0; i < 2022; i++) {
            dropRock(currRock);
            LOG.debug("i={}, column={}", i, column);
            currRock = currRock.getNext();
        }

        LOG.info("Answer is {}", (column.size() - 1));
    }

    private void dropRock(Day17Rock rock) {
        boolean rested = false;
        int[] dropping = Arrays.copyOf(rock.getShape(), 4);
        int ht = column.size() + 4;

        // Lower the rock until it rests on something
        while (!rested) {
            ht--;
            LOG.debug("Dropping {} from {}", Day17Rock.asString(dropping), ht);
            int[] newrock = moveHoriz(dropping, ht);
            rested = moveVert(newrock, ht);
            dropping = newrock;
        }

        // Add the rock to the column
        for (int i = 0; i < dropping.length; i++) {
            setRow((ht + i), dropping[i]);
        }
    }

    private int[] moveHoriz(int[] rock, int ht) {
        int[] retval = new int[rock.length];
        if (dirpos >= directions.size()) {
            dirpos = 0;
        }
        switch (directions.get(dirpos++)) {
            case LEFT -> {
                LOG.debug("Moving left {}", Day17Rock.asString(rock));
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
                LOG.debug("Moving right {}", Day17Rock.asString(rock));
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
        LOG.debug("    Moved to {}", Day17Rock.asString(retval));
        return retval;
    }

    private boolean moveVert(int[] rock, int ht) {
        LOG.debug("Moving vert from ht={}", ht);
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
