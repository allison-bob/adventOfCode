package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day10a extends BaseClass<Grid2D<Day10Tile>> {

    public static void main(String[] args) {
        new Day10a().run(args, "a");
    }

    public Day10a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day10Tile::byChar));
    }

    @Override
    public void solve(Grid2D<Day10Tile> map) {
        LOG.info("read {} grid", map.getSize());

        // Find starting point
        Coord2D start = null;
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                if (map.get(x, y) == Day10Tile.START) {
                    start = new Coord2D(x, y);
                }
            }
        }
        Assert.that((start != null), "No starting point found");

        // Find the connections to the starting point
        List<Coord2D> found = findStartConns(map, start);
        Assert.that((found.size() == 2), "Found " + found.size() + " connections to starting point");
        Coord2D pos1 = found.get(0);
        Coord2D prevPos1 = start;
        Coord2D pos2 = found.get(1);
        Coord2D prevPos2 = start;
        //LOG.debug("Starting point:\n{}", dumpMap(map, pos1, pos2));
        
        // Find the farthest point in the loop
        int count = 1;
        while (!pos1.equals(pos2)) {
            Coord2D n = nextStep(map, pos1, prevPos1);
            prevPos1 = pos1;
            pos1 = n;
            
            n = nextStep(map, pos2, prevPos2);
            prevPos2 = pos2;
            pos2 = n;
            
            count++;
            //LOG.debug("Next step:\n{}", dumpMap(map, pos1, pos2));
        }

        LOG.info("Number of steps: {}", count);
    }
    
    private List<Coord2D> findStartConns(Grid2D<Day10Tile> map, Coord2D start) {
        List<Coord2D> result = new ArrayList<>();
        
        if (connCheck(map.get(start.getX() + 1, start.getY() + 0), Day10Tile.EW, Day10Tile.NW, Day10Tile.SW)) {
            result.add(new Coord2D(start.getX() + 1, start.getY() + 0));
        }
        
        if (connCheck(map.get(start.getX() - 1, start.getY() + 0), Day10Tile.EW, Day10Tile.NE, Day10Tile.SE)) {
            result.add(new Coord2D(start.getX() + 1, start.getY() + 0));
        }
        
        if (connCheck(map.get(start.getX() + 0, start.getY() + 1), Day10Tile.NS, Day10Tile.SE, Day10Tile.SW)) {
            result.add(new Coord2D(start.getX() + 0, start.getY() + 1));
        }
        
        if (connCheck(map.get(start.getX() + 0, start.getY() - 1), Day10Tile.NS, Day10Tile.NE, Day10Tile.NW)) {
            result.add(new Coord2D(start.getX() + 0, start.getY() - 1));
        }
        
        return result;
    }
    
    private boolean connCheck(Day10Tile toCheck, Day10Tile... validOpts) {
        for (Day10Tile v : validOpts) {
            if (toCheck == v) {
                return true;
            }
        }
        return false;
    }
    
    private Coord2D nextStep(Grid2D<Day10Tile> map, Coord2D curr, Coord2D prev) {
        Day10Tile t = map.get(curr);
        Coord2D next1 = new Coord2D((curr.getX() + t.getConn1().getX()), (curr.getY() + t.getConn1().getY()));
        Coord2D next2 = new Coord2D((curr.getX() + t.getConn2().getX()), (curr.getY() + t.getConn2().getY()));
        if (next1.equals(prev)) {
            return next2;
        }
        return next1;
    }
    
    private String dumpMap(Grid2D<Day10Tile> map, Coord2D pos1, Coord2D pos2) {
        Day10Tile t1 = map.get(pos1);
        map.set(pos1, Day10Tile.CURR);
        Day10Tile t2 = map.get(pos2);
        map.set(pos2, Day10Tile.CURR);
        
        String result = map.dump(Day10Tile::getSymbol);
        
        map.set(pos2, t2);
        map.set(pos1, t1);
        
        return result;
    }
}
