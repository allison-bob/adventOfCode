package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day08a extends BaseClass<Grid2D<Integer>> {

    public static void main(String[] args) {
        new Day08a().run(args, "");
    }

    public Day08a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, this::convertIn));
    }
    
    private int convertIn(char c) {
        return c - '0';
    }

    @Override
    public void solve(Grid2D<Integer> map) {
        LOG.info("read {} trees", map.getSize());
        
        int count = 0;
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                if (isVisible(map, x, y)) {
                    count++;
                }
            }
        }

        LOG.info("There are {} trees visible", count);
    }
    
    private boolean isVisible(Grid2D<Integer> map, int x, int y) {
        // Is the tree on an edge
        if ((x == 0) || (y == 0)) {
            return true;
        }
        if ((x == (map.getSize().getX() - 1)) || (y == (map.getSize().getY() - 1))) {
            return true;
        }
        
        // Check the neighboring tree heights
        int h = map.get(x, y);
        long ct = map.compassNeighborStream(new Coord2D(x, y), (start, offset) -> heightCheck(map, h, start, offset))
                .filter(n -> n < h)
                .count();
        
        LOG.debug("For ({},{}), count is {}", x, y, ct);
        return (ct > 0);
    }
    
    private Integer heightCheck(Grid2D<Integer> map, Integer toCheck, Coord2D start, Coord2D offset) {
        int x = start.getX() + offset.getX();
        int y = start.getY() + offset.getY();
        if ((x < 0) || (x >= map.getSize().getX())
                || (y < 0) || (y >= map.getSize().getY())) {
            return 0;
        }
        Integer tree = map.get(x, y);
        if (tree < toCheck) {
            return heightCheck(map, toCheck, new Coord2D(x, y), offset);
        } else {
            return tree;
        }
    }
}
