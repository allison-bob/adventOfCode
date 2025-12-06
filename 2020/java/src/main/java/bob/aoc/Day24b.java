package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import bob.util.Assert;
import java.util.List;

public class Day24b extends BaseClass<List<Coord2D>> {

    public static void main(String[] args) {
        new Day24b().run(args, "");
    }

    public Day24b() {
        super(false);
        setParser(new ObjectParser<>(this::findCoord));
    }

    private Coord2D findCoord(String line) {
        int x = 0;
        int y = 0;
        int pos = 0;

        while (pos < line.length()) {
            switch (line.charAt(pos++)) {
                case 'e' ->
                    x += 2;
                case 'w' ->
                    x -= 2;
                case 'n' -> {
                    y++;
                    switch (line.charAt(pos++)) {
                        case 'e' ->
                            x++;
                        case 'w' ->
                            x--;
                        default ->
                            throw Assert.failed(null, "Unexpected character after 'n': " + line.substring(pos - 2));
                    }
                }
                case 's' -> {
                    y--;
                    switch (line.charAt(pos++)) {
                        case 'e' ->
                            x++;
                        case 'w' ->
                            x--;
                        default ->
                            throw Assert.failed(null, "Unexpected character after 's': " + line.substring(pos - 2));
                    }
                }
                default ->
                    throw Assert.failed(null, "Unexpected character: " + line.substring(pos - 1));
            }
        }
        LOG.debug("Got {},{} from {}", x, y, line);
        return new Coord2D(x, y);
    }

    @Override
    public void solve(List<Coord2D> toFlip) {
        LOG.info("Read {} tile locations", toFlip.size());

        // Initialize the floor
        Day24Floor floor = new Day24Floor();
        floor.doInitial(toFlip);

        // Do the daily tile flips
        for (int i = 0; i < 100; i++) {
            floor.doDaily();
            LOG.debug("Day {}: {} black tiles", (i + 1), floor.countBlack());
        }

        // Compute the result
        LOG.info("There are {} black tiles", floor.countBlack());
    }
}
