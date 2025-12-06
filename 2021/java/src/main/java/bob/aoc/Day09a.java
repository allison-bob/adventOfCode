package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day09a extends BaseClass<Grid2D<Character>> {

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
        new Day09a().run(args, "");
    }

    public Day09a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, c -> c));
    }

    @Override
    public void solve(Grid2D<Character> points) {
        LOG.info("read {} points", points.getSize());
        
        // Add a border of 10 to simplify searching for low points
        points.addBorder('A');
        
        // Search for low points
        int risk = 0;
        Coord2D size = points.getSize();
        for (int y = 1; y < (size.getY() - 1); y++) {
            for (int x = 1; x < (size.getX() - 1); x++) {
                boolean low = true;
                for (Direction d : Direction.values()) {
                    low &= points.get(x, y) < points.get((x + d.dx), (y + d.dy));
                }
                if (low) {
                    risk += points.get(x, y) - '0' + 1;
                    LOG.debug("low point at ({},{}), risk now {}", (x - 1), (y - 1), risk);
                }
            }
        }
        
        LOG.info("Answer is {}", risk);
    }
}
