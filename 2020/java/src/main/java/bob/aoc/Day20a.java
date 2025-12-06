package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DMapParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.Map;

public class Day20a extends BaseClass<Map<Integer, Grid2D<Day20Pixel>>> {

    public static void main(String[] args) {
        new Day20a().run(args, "");
    }

    public Day20a() {
        super(false);
        setParser(new Grid2DMapParser<>(false, false, Day20Pixel::byChar, this::parseID));
    }
    
    private Integer parseID(String line) {
        Integer id = Integer.valueOf(line.substring(5, line.length() - 1));
        Assert.that((id > 0), "Tile number must be >0");
        return id;
    }

    @Override
    public void solve(Map<Integer, Grid2D<Day20Pixel>> tiles) {
        LOG.info("read {} tiles", tiles.size());
        LOG.debug("tile IDs: {}", tiles.keySet());
        
        // Validate that all tiles are square and the same size
        int tileSize = 0;
        for (Map.Entry<Integer, Grid2D<Day20Pixel>> e : tiles.entrySet()) {
            if (tileSize == 0) {
                tileSize = e.getValue().getSize().getX();
            }
            Assert.that((e.getValue().getSize().getX() == tileSize),
                    "X size of tile " + e.getKey() + " is not " + tileSize);
            Assert.that((e.getValue().getSize().getY() == tileSize),
                    "Y size of tile " + e.getKey() + " is not " + tileSize);
        }
        
        Day20EdgeMatcher matcher = new Day20EdgeMatcher(tiles);
        matcher.findMatches();
        
        // Compute answer
        Grid2D<Integer> tilegrid = matcher.getTilegrid();
        int gridSize = tilegrid.getSize().getX();
        long t1 = tilegrid.get(0, 0);
        long t2 = tilegrid.get(0, (gridSize - 1));
        long t3 = tilegrid.get((gridSize - 1), 0);
        long t4 = tilegrid.get((gridSize - 1), (gridSize - 1));
        long answer = t1 * t2 * t3 * t4;
        LOG.info("({},{},{},{}) answer = {}", t1, t2, t3, t4, answer);
    }
}
