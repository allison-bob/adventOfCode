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

public class Day08b extends BaseClass<Grid2D<Character>> {

    public static void main(String[] args) {
        new Day08b().run(args, "");
    }

    public Day08b() {
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
            antinodes.addAll(alist);
            for (int i = 0; i < (alist.size() - 1); i++) {
                Coord2D a1 = alist.get(i);
                for (int j = (i + 1); j < alist.size(); j++) {
                    Coord2D a2 = alist.get(j);
                    int dx = a1.getX() - a2.getX();
                    int dy = a1.getY() - a2.getY();
                    
                    int nx = a2.getX() - dx;
                    int ny = a2.getY() - dy;
                    while ((nx >= 0) && (nx < mapSize.getX()) && (ny >= 0) && (ny < mapSize.getY())) {
                        antinodes.add(new Coord2D(nx, ny));
                        nx -= dx;
                        ny -= dy;
                    }
                    
                    nx = a1.getX() + dx;
                    ny = a1.getY() + dy;
                    while ((nx >= 0) && (nx < mapSize.getX()) && (ny >= 0) && (ny < mapSize.getY())) {
                        antinodes.add(new Coord2D(nx, ny));
                        nx += dx;
                        ny += dy;
                    }
                    
                    LOG.debug("antinode count is now {}", antinodes.size());
                }
            }
        }

        // Count the antinodes
        LOG.info("Antinode count is {}", antinodes.size());
    }
}
