package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day03a extends BaseClass<Grid2D<Day03Spot>> {

    public static void main(String[] args) {
        new Day03a().run(args, "");
    }

    public Day03a() {
        super(false);
        setParser(new Grid2DParser<>(true, false, Day03Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day03Spot> map) {
        LOG.info("map contains {} lines", map.getSize().getY());

        int count = Day03Common.test(map, 3, 1);

        LOG.info("trees encountered: {}", count);
    }
}
