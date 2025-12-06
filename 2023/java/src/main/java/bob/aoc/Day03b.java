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
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class Day03b extends BaseClass<Grid2D<Character>> {

    private Map<Coord2D, List<Integer>> gears = new HashMap<>();

    public static void main(String[] args) {
        new Day03b().run(args, "");
    }

    public Day03b() {
        super(true);
        setParser(new Grid2DParser<>(false, false, Function.identity()));
    }

    @Override
    public void solve(Grid2D<Character> map) {
        LOG.info("Read {} schematic", map.getSize());
        int currNum = 0;
        Set<Coord2D> found = new HashSet<>();
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                if ((map.get(x, y) >= '0') && (map.get(x, y) <= '9')) {
                    // Got a digit, update the current part number
                    currNum = (10 * currNum) + (map.get(x, y) - '0');
                    // Check the neighbors for a star
                    map.neighborStream(new Coord2D(x, y), (start, offset) -> {
                        int gx = start.getX() + offset.getX();
                        int gy = start.getY() + offset.getY();
                        if ((map.get(gx, gy) != null) && (map.get(gx, gy) == '*')) {
                            found.add(new Coord2D(gx, gy));
                        }
                        return '.';
                    }).count();
                } else {
                    // Not a digit
                    for (Coord2D c : found) {
                        if (!gears.containsKey(c)) {
                            gears.put(c, new ArrayList<>());
                        }
                        gears.get(c).add(currNum);
                    }
                    currNum = 0;
                    found.clear();
                }
            }

            // Did the line end with a part number?
            for (Coord2D c : found) {
                if (!gears.containsKey(c)) {
                    gears.put(c, new ArrayList<>());
                }
                gears.get(c).add(currNum);
            }
            currNum = 0;
            found.clear();
        }
        
        long sum = 0;
        for (List<Integer> partnums : gears.values()) {
            if (partnums.size() == 2) {
                sum += partnums.get(0) * partnums.get(1);
            }
        }

        LOG.info("Answer is {}", sum);
    }
}
