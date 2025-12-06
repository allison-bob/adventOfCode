package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day14a extends BaseClass<Grid2D<Day14Spot>> {

    public static void main(String[] args) {
        new Day14a().run(args, "");
    }

    public Day14a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day14Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day14Spot> map) {
        LOG.info("read {} grid", map.getSize());

        // Roll the rocks to the north (-y)
        LOG.debug("Starting map:\n{}", map.dump(Day14Spot::getSymbol));
        for (int y = 1; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                //LOG.debug("Checking {},{}: {}", x, y, map.get(x, y));
                if (map.get(x, y) == Day14Spot.ROUND) {
                    for (int dy = 1; dy < (y + 2); dy++) {
                        //LOG.debug("   Move check {},{}-{}: {}", x, y, dy, map.get(x, (y - dy)));
                        if (map.get(x, (y - dy)) != Day14Spot.EMPTY) {
                            if (dy > 1) {
                                map.set(x, y, Day14Spot.EMPTY);
                                map.set(x, (y - dy + 1), Day14Spot.ROUND);
                            }
                            break;
                        }
                    }
                    //LOG.debug("Updated map:\n{}", map.dump(Day14Spot::getSymbol));
                }
            }
        }
        
        // Compute the load
        long load = 0;
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                if (map.get(x, y) == Day14Spot.ROUND) {
                    load += (map.getSize().getY() - y);
                }
            }
        }
        LOG.info("Total load is {}", load);
    }
}
