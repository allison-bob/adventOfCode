package bob.aoc;

import bob.data.coordinate.Mapper2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day14b extends BaseClass<Grid2D<Day14Spot>> {

    List<String> stateCache = new ArrayList<>();
    List<Long> loadCache = new ArrayList<>();

    public static void main(String[] args) {
        new Day14b().run(args, "");
    }

    public Day14b() {
        super(true);
        setParser(new Grid2DParser<>(false, false, Day14Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day14Spot> map) {
        LOG.info("read {} grid", map.getSize());
        //LOG.debug("Starting map:\n{}", map.dump(Day14Spot::getSymbol));

        long prefix = -1;
        long looplen = -1;
        while (prefix < 0) {
            // Run a cycle
            tiltMap(map);
            map.setOrientation(Mapper2D.NW);
            tiltMap(map);
            map.setOrientation(Mapper2D.WS);
            tiltMap(map);
            map.setOrientation(Mapper2D.SE);
            tiltMap(map);
            map.setOrientation(Mapper2D.EN);
            //LOG.debug("Altered map:\n{}", map.dump(Day14Spot::getSymbol));

            // Compute the map state and load
            String state = getMapState(map);
            long load = 0;
            for (int y = 0; y < map.getSize().getY(); y++) {
                for (int x = 0; x < map.getSize().getX(); x++) {
                    if (map.get(x, y) == Day14Spot.ROUND) {
                        load += (map.getSize().getY() - y);
                    }
                }
            }

            // Is this a repeated state?
            int pos = stateCache.indexOf(state);
            if (pos > -1) {
                prefix = pos;
                looplen = stateCache.size() - prefix;
            }

            // Cache this cycle
            stateCache.add(state);
            loadCache.add(load);
        }

        LOG.info("found prefix {} and loop len {}", prefix, looplen);
        long loopct = 1000000000L / looplen;
        long suffix = (1000000000L % looplen) - prefix;
        if (suffix < 0) {
            loopct--;
            suffix += looplen;
        }
        LOG.info("Target count results in {} loops with {} remaining", loopct, suffix);

        LOG.info("load cache {}", loadCache);
        LOG.info("Final load is {}", loadCache.get((int) (prefix + suffix - 1)));
    }

    private String getMapState(Grid2D<Day14Spot> map) {
        return map.pointStream()
                .map(Day14Spot::getSymbol)
                .map(c -> c.toString())
                .collect(Collectors.joining());
    }

    private void tiltMap(Grid2D<Day14Spot> map) {
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
    }
}
