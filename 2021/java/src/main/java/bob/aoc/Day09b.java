package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day09b extends BaseClass<Grid2D<Character>> {

    private enum Direction {
        N(0, 1), S(0, -1), E(1, 0), W(-1, 0);

        public final int dx;
        public final int dy;

        private Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static void main(String[] args) {
        new Day09b().run(args, "");
    }

    public Day09b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, c -> c));
    }

    @Override
    public void solve(Grid2D<Character> points) {
        LOG.info("read {} points", points.getSize());

        // Add a border of 10 to simplify searching for low points
        points.addBorder('A');

        // Search for low points
        List<Integer> basinSizes = new ArrayList<>();
        Coord2D size = points.getSize();
        for (int y = 1; y < (size.getY() - 1); y++) {
            for (int x = 1; x < (size.getX() - 1); x++) {
                boolean low = true;
                for (Direction d : Direction.values()) {
                    low &= points.get(x, y) < points.get((x + d.dx), (y + d.dy));
                }
                if (low) {
                    int bsize = basinSize(points, x, y);
                    LOG.debug("low point at ({},{}), basin size {}", (x - 1), (y - 1), bsize);
                    basinSizes.add(bsize);
                }
            }
        }

        // Find the answer
        basinSizes.sort(null);
        LOG.debug("basins: {}", basinSizes);
        int len = basinSizes.size();
        long answer = basinSizes.get(len - 1) * basinSizes.get(len - 2) * basinSizes.get(len - 3);

        LOG.info("Answer is {}", answer);
    }

    public int basinSize(Grid2D<Character> points, int x, int y) {
        Set<Coord2D> inBasin = new HashSet<>();
        addToBasin(inBasin, points, x, y);
        return inBasin.size();
    }

    private void addToBasin(Set<Coord2D> inBasin, Grid2D<Character> points, int x, int y) {
        Coord2D here = new Coord2D(x, y);
        if (inBasin.contains(here)) {
            // This point has already been visited
            return;
        }
        if (points.get(x, y) >= '9') {
            // This can't be part of the basin
            return;
        }
        // Add this point to the basin
        inBasin.add(here);
        // Check the neighboring points
        addToBasin(inBasin, points, x - 1, y);
        addToBasin(inBasin, points, x + 1, y);
        addToBasin(inBasin, points, x, y - 1);
        addToBasin(inBasin, points, x, y + 1);
    }
}
