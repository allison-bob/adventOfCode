package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Day08a extends BaseClass<Grid2D<Character>> {

    public static void main(String[] args) {
        new Day08a().run(args, "");
    }

    public Day08a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Function.identity()));
    }

    @Override
    public void solve(Grid2D<Character> map) {
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map", mapSize);

        // Scan the map to find antennas
        Map<Character, List<Coord2D>> antennas = new HashMap<>();
        for (int y = 0; y < mapSize.getY(); y++) {
            for (int x = 0; x < mapSize.getX(); x++) {
                char c = map.get(x, y);
                if (c != '.') {
                    List<Coord2D> alist = antennas.get(c);
                    if (alist == null) {
                        alist = new ArrayList<>();
                        antennas.put(c, alist);
                    }
                    alist.add(new Coord2D(x, y));
                }
            }
        }
        LOG.debug("Antenna list: {}", antennas);

        // Place the antinodes
        Set<Coord2D> antinodes = new HashSet<>();
        for (Character c : antennas.keySet()) {
            List<Coord2D> alist = antennas.get(c);
            for (int i = 0; i < (alist.size() - 1); i++) {
                Coord2D a1 = alist.get(i);
                for (int j = (i + 1); j < alist.size(); j++) {
                    Coord2D a2 = alist.get(j);
                    int dx = a1.getX() - a2.getX();
                    int dy = a1.getY() - a2.getY();
                    Coord2D n1 = new Coord2D((a2.getX() - dx), (a2.getY() - dy));
                    Coord2D n2 = new Coord2D((a1.getX() + dx), (a1.getY() + dy));
                    LOG.debug("link {} to {}: dx={}, dy={}: {} and {}", a1, a2, dx, dy, n1, n2);
                    if (n1.isNotNegative() && (n1.getX() < mapSize.getX()) && (n1.getY() < mapSize.getY())) {
                        antinodes.add(n1);
                    }
                    if (n2.isNotNegative() && (n2.getX() < mapSize.getX()) && (n2.getY() < mapSize.getY())) {
                        antinodes.add(n2);
                    }
                    LOG.debug("antinode count is now {}", antinodes.size());
                }
            }
        }

        // Count the antinodes
        LOG.info("Antinode count is {}", antinodes.size());
    }
}
