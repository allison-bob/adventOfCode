package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.Optional;
import java.util.function.Function;

public class Day03a extends BaseClass<Grid2D<Character>> {

    public static void main(String[] args) {
        new Day03a().run(args, "");
    }

    public Day03a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Function.identity()));
    }

    @Override
    public void solve(Grid2D<Character> map) {
        LOG.info("Read {} schematic", map.getSize());
        long sum = 0;
        int currNum = 0;
        boolean valid = false;
        for (int y = 0; y < map.getSize().getY(); y++) {
            for (int x = 0; x < map.getSize().getX(); x++) {
                if ((map.get(x, y) >= '0') && (map.get(x, y) <= '9')) {
                    // Got a digit, update the current part number
                    currNum = (10 * currNum) + (map.get(x, y) - '0');
                    // Check the neighbors for a symbol
                    Optional<Character> found = map.neighborStream(new Coord2D(x, y))
                            .filter(c -> c != null)
                            .filter(c -> c != '.')
                            .filter(c -> (c < '0') || (c > '9'))
                            .findFirst();
                    if (found.isPresent()) {
                        valid = true;
                    }
                } else {
                    // Not a digit
                    if (valid) {
                        sum += currNum;
                    }
                    currNum = 0;
                    valid = false;
                }
                LOG.debug("at ({},{}), sum {} curr {} valid {}", x, y, sum, currNum, valid);
            }

            // Did the line end with a part number?
            if (valid) {
                sum += currNum;
            }
            currNum = 0;
            valid = false;
        }

        LOG.info("Answer is {}", sum);
    }
}
