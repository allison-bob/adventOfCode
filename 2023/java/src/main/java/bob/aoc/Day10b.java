package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day10b extends BaseClass<Grid2D<Day10Tile>> {

    public static void main(String[] args) {
        new Day10b().run(args, "e");
    }

    public Day10b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day10Tile::byChar));
    }

    @Override
    public void solve(Grid2D<Day10Tile> map) {
        LOG.info("read {} grid", map.getSize());

        // Make grid for marking loop
        Grid2D<Day10Tile> loopmap = new Grid2D<>();
        loopmap.fill(map.getSize().getX(), map.getSize().getY(), Day10Tile.GND);

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
        loopmap.set(start, map.get(start));
        loopmap.set(pos1, map.get(pos1));
        loopmap.set(pos2, map.get(pos2));
        LOG.debug("Starting point:\n{}", dumpMap(map, pos1, pos2));

        // Isolate the loop from other points
        while (!pos1.equals(pos2)) {
            Coord2D n = nextStep(map, pos1, prevPos1);
            prevPos1 = pos1;
            pos1 = n;

            n = nextStep(map, pos2, prevPos2);
            prevPos2 = pos2;
            pos2 = n;

            if ((loopmap.get(pos1) != Day10Tile.GND) && (loopmap.get(pos2) != Day10Tile.GND)) {
                break;
            }
            loopmap.set(pos1, map.get(pos1));
            loopmap.set(pos2, map.get(pos2));
            //LOG.debug("Next step:\n{}", dumpMap(map, pos1, pos2));
        }
        LOG.debug("Just the loop:\n{}", loopmap.dump(Day10Tile::getSymbol));
        Assert.that((loopmap.get(0, 0) == Day10Tile.GND), "Upper-left corner not outside");

        // Expand the map to allow navigating between pipes; original points are at (2x,2y), new points at (2x+1,2y+1)
        Grid2D<Day10Tile> exploopmap = new Grid2D<>();
        for (int y = 0; y < map.getSize().getY(); y++) {
            List<Day10Tile> copiedRow = new ArrayList<>();
            List<Day10Tile> addedRow = new ArrayList<>();
            for (int x = 0; x < loopmap.getSize().getX(); x++) {
                Day10Tile curr = loopmap.get(x, y);
                copiedRow.add(curr);
                Day10Tile toAdd = switch (curr) {
                    case NS, SE, SW ->
                        Day10Tile.NS;
                    default ->
                        Day10Tile.FILL;
                };
                addedRow.add(toAdd);
                toAdd = switch (curr) {
                    case NE, SE, EW ->
                        Day10Tile.EW;
                    default ->
                        Day10Tile.FILL;
                };
                copiedRow.add(toAdd);
                addedRow.add(Day10Tile.FILL);
            }
            exploopmap.addRow(copiedRow);
            exploopmap.addRow(addedRow);
        }
        LOG.debug("Expanded loop:\n{}", exploopmap.dump(Day10Tile::getSymbol));

        // Find the parts outside the loop
        for (int x = 0; x < exploopmap.getSize().getX(); x++) {
            findOutside(exploopmap, x, 0);
            findOutside(exploopmap, x, (exploopmap.getSize().getY() - 1));
        }
        for (int y = 0; y < exploopmap.getSize().getY(); y++) {
            findOutside(exploopmap, 0, y);
            findOutside(exploopmap, (exploopmap.getSize().getX() - 1), y);
        }
        LOG.debug("Outside loop:\n{}", exploopmap.dump(Day10Tile::getSymbol));

        // Find the number of ground tiles (which must be inside the loop)
        long count = exploopmap.pointStream()
                .filter(t -> t == Day10Tile.GND)
                .count();
        LOG.info("Number of inside tiles: {}", count);
    }

    private List<Coord2D> findStartConns(Grid2D<Day10Tile> map, Coord2D start) {
        List<Coord2D> result = new ArrayList<>();
        List<Day10Tile> options = new ArrayList<>(Day10Tile.directions());

        LOG.debug("Before result {}, options {}", result, options);
        // Does the tile to the East connect to the tile to it's West
        if (connCheck(map.get(start.getX() + 1, start.getY() + 0), Day10Tile.EW, Day10Tile.NW, Day10Tile.SW)) {
            result.add(new Coord2D(start.getX() + 1, start.getY() + 0));
            // Options for this tile must include East
            options.removeAll(List.of(Day10Tile.NW, Day10Tile.SW, Day10Tile.NS));
        }
        LOG.debug("E result {}, options {}", result, options);

        // Does the tile to the West connect to the tile to it's East
        if (connCheck(map.get(start.getX() - 1, start.getY() + 0), Day10Tile.EW, Day10Tile.NE, Day10Tile.SE)) {
            result.add(new Coord2D(start.getX() - 1, start.getY() + 0));
            // Options for this tile must include West
            options.removeAll(List.of(Day10Tile.NE, Day10Tile.SE, Day10Tile.NS));
        }
        LOG.debug("W result {}, options {}", result, options);

        // Does the tile to the North connect to the tile to it's South
        if (connCheck(map.get(start.getX() + 0, start.getY() - 1), Day10Tile.NS, Day10Tile.SE, Day10Tile.SW)) {
            result.add(new Coord2D(start.getX() + 0, start.getY() - 1));
            // Options for this tile must include North
            options.removeAll(List.of(Day10Tile.SE, Day10Tile.SW, Day10Tile.EW));
        }
        LOG.debug("N result {}, options {}", result, options);

        // Does the tile to the South connect to the tile to it's North
        if (connCheck(map.get(start.getX() + 0, start.getY() + 1), Day10Tile.NS, Day10Tile.NE, Day10Tile.NW)) {
            result.add(new Coord2D(start.getX() + 0, start.getY() + 1));
            // Options for this tile must include South
            options.removeAll(List.of(Day10Tile.NE, Day10Tile.NW, Day10Tile.EW));
        }
        LOG.debug("S result {}, options {}", result, options);

        Assert.that((options.size() == 1), "More than one option remained?");
        map.set(start, options.get(0));
        return result;
    }

    private boolean connCheck(Day10Tile toCheck, Day10Tile... validOpts) {
        LOG.debug("checking {}: {}", toCheck, validOpts);
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

    private void findOutside(Grid2D<Day10Tile> map, int x, int y) {
        if (map.get(x, y) == null) {
            return;
        }
        switch (map.get(x, y)) {
            case FILL, GND -> {
                map.set(x, y, Day10Tile.OUTSIDE);
                findOutside(map, (x + 1), (y + 0));
                findOutside(map, (x - 1), (y + 0));
                findOutside(map, (x + 0), (y + 1));
                findOutside(map, (x + 0), (y - 1));
            }
        }
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
