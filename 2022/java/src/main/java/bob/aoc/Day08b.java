package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day08b extends BaseClass<Grid2D<Integer>> {

    public static void main(String[] args) {
        new Day08b().run(args, "");
    }

    public Day08b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, this::convertIn));
    }
    
    private int convertIn(char c) {
        return c - '0';
    }

    @Override
    public void solve(Grid2D<Integer> map) {
        LOG.info("read {} trees", map.getSize());
        
        long maxScore = 0;
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                maxScore = Math.max(maxScore, getScore(map, x, y));
            }
        }

        LOG.info("The max score is {}", maxScore);
    }
    
    private long getScore(Grid2D<Integer> map, int x, int y) {
        // Is the tree on an edge
        if ((x == 0) || (y == 0)) {
            return 0;
        }
        if ((x == (map.getSize().getX() - 1)) || (y == (map.getSize().getY() - 1))) {
            return 0;
        }
        
        // Check the neighboring tree heights
        int h = map.get(x, y);
        long score = map.compassNeighborStream(new Coord2D(x, y),
                (start, offset) -> heightCheck(map, h, 0, start, offset))
                .reduce(1, (a, b) -> a * b);
        
        LOG.debug("For ({},{}), score is {}", x, y, score);
        return score;
    }
    
    private Integer heightCheck(Grid2D<Integer> map, Integer toCheck, int currCt, Coord2D start, Coord2D offset) {
        int x = start.getX() + offset.getX();
        int y = start.getY() + offset.getY();
        if ((x < 0) || (x >= map.getSize().getX())
                || (y < 0) || (y >= map.getSize().getY())) {
            //LOG.debug("Reached edge at ({},{}), count is {}", x, y, currCt);
            return currCt;
        }
        Integer tree = map.get(x, y);
        if (tree < toCheck) {
            return heightCheck(map, toCheck, (currCt + 1), new Coord2D(x, y), offset);
        } else {
            //LOG.debug("Found higher tree at ({},{}):{}, count is {}", x, y, tree, (currCt + 1));
            return (currCt + 1);
        }
    }
}
