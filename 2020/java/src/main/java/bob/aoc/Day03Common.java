package bob.aoc;

import bob.data.grid.Grid2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day03Common {

    private final static Logger LOG = LoggerFactory.getLogger(Day03Common.class);

    private Day03Common() {
    }

    static int test(Grid2D<Day03Spot> map, int right, int down) {
        int count = 0;
        int x = 0;

        for (int y = 0; y < map.getSize().getY(); y += down) {
            LOG.debug("checking {},{}", x, y);
            Day03Spot s = map.get(x, y);
            if (s == Day03Spot.TREE) {
                count++;
            }
            x += right;
        }

        return count;
    }
}
