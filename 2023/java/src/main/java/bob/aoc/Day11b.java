package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day11b extends BaseClass<Grid2D<Day11Spot>> {

    public static void main(String[] args) {
        new Day11b().run(args, "");
    }

    public Day11b() {
        super(true);
        setParser(new Grid2DParser<>(false, false, Day11Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day11Spot> map) {
        LOG.info("read {} grid", map.getSize());

        // Find the rows and columns that need to be expanded
        LOG.debug("Initial grid:\n{}", map.dump(Day11Spot::getSymbol));
        List<Coord2D> galaxies = new ArrayList<>();
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                if (map.get(x, y) == Day11Spot.GALAXY) {
                    galaxies.add(new Coord2D(x, y));
                }
            }
        }
        LOG.debug("Galaxies at {}", galaxies);
        List<Integer> xAdds = new ArrayList<>();
        for (int x = 0; x < map.getSize().getX(); x++) {
            int x1 = x;
            boolean notEmpty = galaxies.stream()
                    .mapToInt(Coord2D::getX)
                    .anyMatch(v -> v == x1);
            if (!notEmpty) {
                xAdds.add(x);
            }
        }
        LOG.debug("Empty columns: {}", xAdds);
        List<Integer> yAdds = new ArrayList<>();
        for (int y = 0; y < map.getSize().getY(); y++) {
            int y1 = y;
            boolean notEmpty = galaxies.stream()
                    .mapToInt(Coord2D::getY)
                    .anyMatch(v -> v == y1);
            if (!notEmpty) {
                yAdds.add(y);
            }
        }
        LOG.debug("Empty rows: {}", yAdds);
        
        // Find the shortest path between each galaxy
        long expFactor = isRealData() ? 999999 : 99;
        long total = 0;
        for (int i = 0; i < (galaxies.size() - 1); i++) {
            for (int j = (i + 1); j < galaxies.size(); j++) {
                Coord2D gi = galaxies.get(i);
                Coord2D gj = galaxies.get(j);
                total += gi.manhattan(gj);
                LOG.debug("Path from {} to {}", gi, gj);
                
                int minx = Math.min(gi.getX(), gj.getX());
                int maxx = Math.max(gi.getX(), gj.getX());
                for (int k = 0; k < xAdds.size(); k++) {
                    if ((xAdds.get(k) > minx) && (xAdds.get(k) < maxx)) {
                        LOG.debug("Adding for column {}", xAdds.get(k));
                        total += expFactor;
                    }
                }
                
                int miny = Math.min(gi.getY(), gj.getY());
                int maxy = Math.max(gi.getY(), gj.getY());
                for (int k = 0; k < yAdds.size(); k++) {
                    if ((yAdds.get(k) > miny) && (yAdds.get(k) < maxy)) {
                        LOG.debug("Adding for row {}", yAdds.get(k));
                        total += expFactor;
                    }
                }
            }
        }
        
        LOG.info("Total path length: {}", total);
    }
}
