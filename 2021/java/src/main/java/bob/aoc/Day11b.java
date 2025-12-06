package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day11b extends BaseClass<Grid2D<Day11Octopus>> {

    public static void main(String[] args) {
        new Day11b().run(args, "");
    }

    public Day11b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day11Octopus::new));
    }

    @Override
    public void solve(Grid2D<Day11Octopus> cavern) {
        LOG.info("read {} octopi", cavern.getSize());
        long target = cavern.getSize().getX() * cavern.getSize().getY();

        // Add a border of inactive octopi to simplify processing
        cavern.addBorder(new Day11Octopus());

        // Run steps until everybody flashes
        long count = 0;
        int i = 0;
        while (count < target) {
            count = runStep(cavern);
            i++;
            if (LOG.isDebugEnabled()) {
                LOG.debug("After step {}, {} flashes:\n{}", i, count, cavern.dump(Day11Octopus::toString));
            }
        }

        LOG.info("First sync flash is {}", i);
    }

    private long runStep(Grid2D<Day11Octopus> cavern) {
        // Start the step
        cavern.pointStream().forEach(Day11Octopus::startStep);

        // Loop to make flashes
        boolean flashed = true;
        while (flashed) {
            flashed = false;
            for (int x = 0; x < cavern.getSize().getX(); x++) {
                for (int y = 0; y < cavern.getSize().getY(); y++) {
                    if (cavern.get(x, y).flashCheck()) {
                        cavern.neighborStream(new Coord2D(x, y)).forEach(Day11Octopus::neighborFlashed);
                        flashed = true;
                    }
                }
            }
        }

        // Count the number of flashes
        long ct = cavern.pointStream()
                .filter(Day11Octopus::hasFlashed)
                .count();

        return ct;
    }
}
