package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day11b extends BaseClass<Grid2D<Day11Spot>> {

    public static void main(String[] args) {
        new Day11b().run(args, "");
    }

    public Day11b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day11Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day11Spot> currState) {
        LOG.info("map contains {} lines", currState.getSize().getY());
        LOG.debug("Starting point:\n{}", currState.dump(s -> s.symbol));

        boolean changed = true;
        while (changed) {
            changed = false;
            Grid2D<Day11Spot> newState = new Grid2D<>(false, false);
            for (int y = 0; y < currState.getSize().getY(); y++) {
                List<Day11Spot> row = new ArrayList<>();
                for (int x = 0; x < currState.getSize().getX(); x++) {
                    Day11Spot newval = applyRules(currState, x, y);
                    changed |= (newval != currState.get(x, y));
                    //LOG.debug("{},{}: {} -> {} ({})", x, y, currState.get(x, y), newval, changed);
                    row.add(newval);
                }
                newState.addRow(row);
            }
            LOG.debug("Changing to:\n{}", newState.dump(s -> s.symbol));
            currState = newState;
        }

        // Count the number of occupied seats
        long ct = currState.pointStream()
                .filter(ls -> ls == Day11Spot.OCCUPIED)
                .count();
        LOG.info("Number of occupied seats is {}", ct);
    }

    private Day11Spot applyRules(Grid2D<Day11Spot> currState, int x, int y) {
        switch (currState.get(x, y)) {
            case FLOOR -> {
                return Day11Spot.FLOOR;
            }
            case EMPTY -> {
                if (occupyCount(currState, x, y) == 0) {
                    return Day11Spot.OCCUPIED;
                } else {
                    return Day11Spot.EMPTY;
                }
            }
            case OCCUPIED -> {
                if (occupyCount(currState, x, y) >= 5) {
                    return Day11Spot.EMPTY;
                } else {
                    return Day11Spot.OCCUPIED;
                }
            }
        }
        throw Assert.failed(null, "Unexpected value in grid: " + currState.get(x, y));
    }

    private Day11Spot visible(Grid2D<Day11Spot> currState, Coord2D start, Coord2D offset) {
        int x = start.getX() + offset.getX();
        int y = start.getY() + offset.getY();
        if ((x < 0) || (x >= currState.getSize().getX())
                || (y < 0) || (y >= currState.getSize().getY())) {
            return Day11Spot.FLOOR;
        }
        Day11Spot ls = currState.get(x, y);
        if (ls == Day11Spot.FLOOR) {
            return visible(currState, new Coord2D(x, y), offset);
        } else {
            return ls;
        }
    }

    private long occupyCount(Grid2D<Day11Spot> currState, int x, int y) {
        return currState.neighborStream(new Coord2D(x, y), (start, offset) -> visible(currState, start, offset))
                .filter(ls -> ls == Day11Spot.OCCUPIED)
                .count();
    }
}
