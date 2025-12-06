package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day11a extends BaseClass<Grid2D<Day11Spot>> {

    public static void main(String[] args) {
        new Day11a().run(args, "");
    }

    public Day11a() {
        super(false);
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

        // Double-up empty rows and columns
        Grid2D<Day11Spot> expmap = new Grid2D<>();
        for (int y = 0; y < map.getSize().getY(); y++) {
            List<Day11Spot> row = new ArrayList<>();
            for (int x = 0; x < map.getSize().getX(); x++) {
                row.add(map.get(x, y));
                if (xAdds.contains(x)) {
                    row.add(Day11Spot.EMPTY);
                }
            }
            expmap.addRow(row);
            if (yAdds.contains(y)) {
                expmap.addRow(row);
            }
        }
        LOG.debug("Expanded grid {}:\n{}", expmap.getSize(), expmap.dump(Day11Spot::getSymbol));
        
        // Reset the galaxy locations
        galaxies.clear();
        for (int y = 0; y < expmap.getSize().getY(); y++) {
            for (int x = 0; x < expmap.getSize().getX(); x++) {
                if (expmap.get(x, y) == Day11Spot.GALAXY) {
                    galaxies.add(new Coord2D(x, y));
                }
            }
        }
        LOG.debug("Galaxies now at {}", galaxies);
        
        // Find the shortest path between each galaxy
        long total = 0;
        for (int i = 0; i < (galaxies.size() - 1); i++) {
            for (int j = (i + 1); j < galaxies.size(); j++) {
                total += galaxies.get(i).manhattan(galaxies.get(j));
            }
        }
        
        LOG.info("Total path length: {}", total);
    }
}
