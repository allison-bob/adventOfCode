package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.Map;

public class Day03b extends BaseClass<Grid2D<Day03Spot>> {

    public static void main(String[] args) {
        new Day03b().run(args, "");
    }

    public Day03b() {
        super(false);
        setParser(new Grid2DParser<>(true, false, Day03Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day03Spot> map) {
        LOG.info("map contains {} lines", map.getSize().getY());

        Map<Coord2D, Integer> counts = new HashMap<>();
        counts.put(new Coord2D(1, 1), Day03Common.test(map, 1, 1));
        counts.put(new Coord2D(3, 1), Day03Common.test(map, 3, 1));
        counts.put(new Coord2D(5, 1), Day03Common.test(map, 5, 1));
        counts.put(new Coord2D(7, 1), Day03Common.test(map, 7, 1));
        counts.put(new Coord2D(1, 2), Day03Common.test(map, 1, 2));

        LOG.info("trees encountered: {}", counts);
        LOG.info("Answer: {}", counts.values().stream().mapToInt(v -> v).reduce(1, Math::multiplyExact));
    }
}
