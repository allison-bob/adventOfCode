package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class Day18b extends BaseClass<List<Day18DigPlan>> {

    public static void main(String[] args) {
        new Day18b().run(args, "");
    }

    public Day18b() {
        super(true);
        setParser(new ObjectParser<>(Day18DigPlan::new));
    }

    @Override
    public void solve(List<Day18DigPlan> entries) {
        LOG.info("Read {} entries", entries.size());

        // Build the set of vertices, building the grid dimensions
        Coord2D loc = new Coord2D(0, 0);
        TreeSet<Integer> xValSet = new TreeSet<>();
        TreeSet<Integer> yValSet = new TreeSet<>();
        int maxX = 0;
        int minX = 0;
        int maxY = 0;
        int minY = 0;
        for (Day18DigPlan dp : entries) {
            dp.direction = Day18Direction.values()[dp.color % 4];
            dp.distance = dp.color / 16;
            loc = loc.addOffset(new Coord2D((dp.distance * dp.direction.dx), (dp.distance * dp.direction.dy)));
            dp.vertex = loc;
            xValSet.add(loc.getX());
            yValSet.add(loc.getY());
            maxX = Math.max(maxX, loc.getX());
            minX = Math.min(minX, loc.getX());
            maxY = Math.max(maxY, loc.getY());
            minY = Math.min(minY, loc.getY());
        }
        List<Integer> xVals = new ArrayList<>(xValSet);
        List<Integer> yVals = new ArrayList<>(yValSet);
        LOG.debug("Final location is {}, grid dims: x={},{}, y={},{}", loc, minX, maxX, minY, maxY);
        LOG.debug("xVals: {}", xVals);
        LOG.debug("yVals: {}", yVals);

        // Determine the grid sizes
        List<Integer> xSizes = buildSizes(xValSet);
        List<Integer> ySizes = buildSizes(yValSet);
        LOG.debug("xSizes: {}", xSizes);
        LOG.debug("ySizes: {}", ySizes);

        // Build the grids
        Grid2D<Integer> grid = new Grid2D<>();
        grid.fill(xSizes.size(), ySizes.size(), 0);
        Grid2D<Coord2D> sizes = new Grid2D<>();
        for (int y = 0; y < ySizes.size(); y++) {
            List<Coord2D> row = new ArrayList<>();
            for (int x = 0; x < xSizes.size(); x++) {
                row.add(new Coord2D(xSizes.get(x), ySizes.get(y)));
            }
            sizes.addRow(row);
        }
        LOG.debug("grid.size={}, sizes.size={}", grid.getSize(), sizes.getSize());

        // Place the trench
        loc = new Coord2D((2 * xVals.indexOf(0)), (2 * yVals.indexOf(0)));
        LOG.debug("Starting at {}", loc);
        for (Day18DigPlan dp : entries) {
            LOG.debug("Executing plan {}", dp);
            int moved = 0;
            while (moved < dp.distance) {
                loc = loc.addOffset(dp.direction.offset);
                LOG.debug("moving {} of {}, size is {}", moved, dp.distance, sizes.get(loc));
                grid.set(loc, dp.color);
                moved += (Math.abs(dp.direction.dx) * sizes.get(loc).getX())
                        + (Math.abs(dp.direction.dy) * sizes.get(loc).getY());
                LOG.debug("moved {} of {}, now at {}", moved, dp.distance, loc);
            }
        }
        LOG.debug("Trench map:\n{}", grid.dump(i -> (i > 0) ? '#' : ((i < 0) ? ' ' : '.')));

        // Mark the points that are not inside the trench boundary
        grid.addBorder(-1);
        sizes.addBorder(new Coord2D(0, 0));
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

        // Add the grid sizes of points that are not outside the trench boundary
        long sum = 0;
        for (int y = 0; y < grid.getSize().getY(); y++) {
            for (int x = 0; x < grid.getSize().getX(); x++) {
                if (grid.get(x, y) >= 0) {
                    Coord2D size = sizes.get(x, y);
                    sum += ((long) size.getX()) * size.getY();
                }
            }
        }
        LOG.info("Volume is {}", sum);
    }

    private List<Integer> buildSizes(TreeSet<Integer> vals) {
        List<Integer> result = new ArrayList<>();
        int pos = vals.first();
        for (Iterator<Integer> it = vals.iterator(); it.hasNext();) {
            Integer v = it.next();
            if (v != pos) {
                // Not the first element
                result.add(v - pos - 1);
            }
            result.add(1);
            pos = v;
        }
        return result;
    }
}
