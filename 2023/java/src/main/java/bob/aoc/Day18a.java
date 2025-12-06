package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day18a extends BaseClass<List<Day18DigPlan>> {

    public static void main(String[] args) {
        new Day18a().run(args, "");
    }

    public Day18a() {
        super(false);
        setParser(new ObjectParser<>(Day18DigPlan::new));
    }

    @Override
    public void solve(List<Day18DigPlan> entries) {
        LOG.info("Read {} entries", entries.size());

        // Build the set of vertices, building the grid dimensions
        Coord2D loc = new Coord2D(0, 0);
        int maxX = 0;
        int minX = 0;
        int maxY = 0;
        int minY = 0;
        for (Day18DigPlan dp : entries) {
            loc = loc.addOffset(new Coord2D((dp.distance * dp.direction.dx), (dp.distance * dp.direction.dy)));
            dp.vertex = loc;
            maxX = Math.max(maxX, loc.getX());
            minX = Math.min(minX, loc.getX());
            maxY = Math.max(maxY, loc.getY());
            minY = Math.min(minY, loc.getY());
        }
        LOG.debug("Final location is {}, grid dims: x={},{}, y={},{}", loc, minX, maxX, minY, maxY);

        // Build the grid
        Grid2D<Integer> grid = new Grid2D<>();
        grid.fill((maxX - minX + 1), (maxY - minY + 1), 0);

        // Place the trench
        loc = new Coord2D(-minX, -minY);
        for (Day18DigPlan dp : entries) {
            for (int i = 0; i < dp.distance; i++) {
                loc = loc.addOffset(dp.direction.offset);
                grid.set(loc, dp.color);
            }
        }
        LOG.debug("Trench map:\n{}", grid.dump(i -> (i > 0) ? '#' : ((i < 0) ? ' ' : '.')));

        // Mark the points that are not inside the trench boundary
        grid.addBorder(-1);
        boolean madeChanges = true;
        while (madeChanges) {
            madeChanges = false;
            for (int y = 1; y < (grid.getSize().getY() - 1); y++) {
                for (int x = 1; x < (grid.getSize().getX() - 1); x++) {
                    if (grid.get(x, y) == 0) {
                        if ((grid.get((x - 1), y) < 0) || (grid.get(x, (y - 1)) < 0)) {
                            grid.set(x, y, -1);
                            madeChanges = true;
                        }
                    }
                }
            }
            for (int y = (grid.getSize().getY() - 1); y > 0; y--) {
                for (int x = (grid.getSize().getX() - 1); x > 0; x--) {
                    if (grid.get(x, y) == 0) {
                        if ((grid.get((x + 1), y) < 0) || (grid.get(x, (y + 1)) < 0)) {
                            grid.set(x, y, -1);
                            madeChanges = true;
                        }
                    }
                }
            }
        }
        LOG.debug("Border map:\n{}", grid.dump(i -> (i > 0) ? '#' : ((i < 0) ? ' ' : '.')));

        // Count the grid points that are not outside the trench boundary
        long count = grid.pointStream()
                .filter(i -> i >= 0)
                .count();
        LOG.info("Volume is {}", count);
    }
}
