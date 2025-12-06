package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.stream.IntStream;

public class Day14b extends BaseClass<List<Day14b.Robot>> {

    public static class Robot {

        public final Coord2D initPosition;
        public final Coord2D initVelocity;
        public Coord2D finalPosition;

        public Robot(String line) {
            String[] bits = line.split("[=, ]");
            initPosition = new Coord2D(Integer.parseInt(bits[1]), Integer.parseInt(bits[2]));
            initVelocity = new Coord2D(Integer.parseInt(bits[4]), Integer.parseInt(bits[5]));
            finalPosition = initPosition;
        }

        @Override
        public String toString() {
            return "[p=" + initPosition + ",v=" + initVelocity + ",fp=" + finalPosition + "]";
        }
    }

    private int maxX;
    private int maxY;

    public static void main(String[] args) {
        new Day14b().run(args, "");
    }

    public Day14b() {
        super(false);
        setParser(new ObjectParser<>(Robot::new));
    }

    @Override
    public void solve(List<Robot> robots) {
        LOG.info("Read {} robots", robots.size());

        // Set the map size and midpoints
        maxX = isRealData() ? 101 : 11;
        maxY = isRealData() ? 103 : 7;

        // Step the robots and look for the Christmas tree pattern
        boolean found = false;
        long stepNum = 0;
        while (!found) {
            stepRobots(robots);
            stepNum++;
            if (patternCheck(robots)) {
                found = patternCompare(robots);
            }
        }
        robots.sort((a, b) -> a.finalPosition.compareTo(b.finalPosition));
        LOG.info("Steps to get tree: {}", stepNum);
    }

    private void stepRobots(List<Robot> robots) {
        for (Robot r : robots) {
            int x = (r.finalPosition.getX() + r.initVelocity.getX()) % maxX;
            if (x < 0) {
                x += maxX;
            }
            int y = (r.finalPosition.getY() + r.initVelocity.getY()) % maxY;
            if (y < 0) {
                y += maxY;
            }
            r.finalPosition = new Coord2D(x, y);
        }
    }

    private boolean patternCheck(List<Robot> robots) {
        int[] counts = new int[maxY];

        // Count the number of robots on each row
        for (Robot r : robots) {
            counts[r.finalPosition.getY()]++;
        }
        // Check for tree pattern if a row has 30+ robots
        int maxCt = IntStream.of(counts).max().getAsInt();
        return maxCt >= 30;
    }

    private boolean patternCompare(List<Robot> robots) {
        // Build a grid with the robot positions
        Grid2D<Character> map = new Grid2D<>();
        map.fill(maxX, maxY, ' ');
        for (Robot r : robots) {
            map.set(r.finalPosition, 'X');
        }

        // Get a dump of the grid
        String dump = map.dump(c -> c);

        // Find the location of the first line of the pattern
        int pos = dump.indexOf(CHRISTMAS_TREE.get(0));
        if (pos == -1) {
            // No first line
            return false;
        }
        LOG.debug("Dump with possible match:\n{}", dump);

        // Check for each subsequent line
        for (int i = 1; i < CHRISTMAS_TREE.size(); i++) {
            int nextpos = dump.indexOf(CHRISTMAS_TREE.get(i), (pos + 1));
            LOG.debug("Next line at {} = {} + {}", nextpos, pos, (nextpos - pos));
            if ((nextpos - pos) != (maxX + 1)) {
                // Next line not positioned above previous line
                return false;
            }
            pos = nextpos;
        }

        return true;
    }

    static final List<String> CHRISTMAS_TREE = List.of(
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "X                             X",
            "X                             X",
            "X                             X",
            "X                             X",
            "X              X              X",
            "X             XXX             X",
            "X            XXXXX            X",
            "X           XXXXXXX           X",
            "X          XXXXXXXXX          X",
            "X            XXXXX            X",
            "X           XXXXXXX           X",
            "X          XXXXXXXXX          X",
            "X         XXXXXXXXXXX         X",
            "X        XXXXXXXXXXXXX        X",
            "X          XXXXXXXXX          X",
            "X         XXXXXXXXXXX         X",
            "X        XXXXXXXXXXXXX        X",
            "X       XXXXXXXXXXXXXXX       X",
            "X      XXXXXXXXXXXXXXXXX      X",
            "X        XXXXXXXXXXXXX        X",
            "X       XXXXXXXXXXXXXXX       X",
            "X      XXXXXXXXXXXXXXXXX      X",
            "X     XXXXXXXXXXXXXXXXXXX     X",
            "X    XXXXXXXXXXXXXXXXXXXXX    X",
            "X             XXX             X",
            "X             XXX             X",
            "X             XXX             X",
            "X                             X",
            "X                             X",
            "X                             X",
            "X                             X",
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    );
}
