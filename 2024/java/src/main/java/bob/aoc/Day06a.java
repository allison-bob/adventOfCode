package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;

public class Day06a extends BaseClass<Grid2D<Day06Spot>> {

    public static void main(String[] args) {
        new Day06a().run(args, "");
    }

    public Day06a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day06Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day06Spot> map) {
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map", mapSize);
        LOG.debug("Starting map:\n{}", map.dump(Day06Spot::getSymbol));

        // Set up for operation
        Coord2D guard = null;
        for (int y = 0; ((y < mapSize.getY()) && (guard == null)); y++) {
            for (int x = 0; ((x < mapSize.getX()) && (guard == null)); x++) {
                if (map.get(x, y) == Day06Spot.GUARD) {
                    guard = new Coord2D(x, y);
                }
            }
        }
        Assert.that((guard != null), "No guard found");
        Coord2D dir = new Coord2D(0, -1);

        // Track the guard's path
        while (map.get(guard) != null) {
            map.set(guard, Day06Spot.VISITED);
            int dx = guard.getX() + dir.getX();
            int dy = guard.getY() + dir.getY();
            while (map.get(dx, dy) == Day06Spot.OBSTRUCTION) {
                dir = Mapper2D.NW.map(dir);
                dx = guard.getX() + dir.getX();
                dy = guard.getY() + dir.getY();
            }
            guard = new Coord2D(dx, dy);
            map.set(guard, Day06Spot.GUARD);
            LOG.debug("\nAfter step:\n{}", map.dump(Day06Spot::getSymbol));
        }

        // Count the positions visited
        long count = map.pointStream()
                .filter(s -> s == Day06Spot.VISITED)
                .count();
        LOG.info("Guard visited {} positions", count);
    }
}
