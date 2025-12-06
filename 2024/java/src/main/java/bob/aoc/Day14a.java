package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.stream.IntStream;

public class Day14a extends BaseClass<List<Day14a.Robot>> {

    public static class Robot {

        public final Coord2D initPosition;
        public final Coord2D initVelocity;
        public Coord2D finalPosition;

        public Robot(String line) {
            String[] bits = line.split("[=, ]");
            initPosition = new Coord2D(Integer.parseInt(bits[1]), Integer.parseInt(bits[2]));
            initVelocity = new Coord2D(Integer.parseInt(bits[4]), Integer.parseInt(bits[5]));
        }

        @Override
        public String toString() {
            return "[p=" + initPosition + ",v=" + initVelocity + ",fp=" + finalPosition + "]";
        }
    }

    public static void main(String[] args) {
        new Day14a().run(args, "");
    }

    public Day14a() {
        super(false);
        setParser(new ObjectParser<>(Robot::new));
    }

    @Override
    public void solve(List<Robot> robots) {
        LOG.info("Read {} robots", robots.size());

        // Set the map size and midpoints
        int maxX = isRealData() ? 101 : 11;
        int maxY = isRealData() ? 103 : 7;
        int midX = maxX / 2;
        int midY = maxY / 2;

        // Move the robots 100 steps into the future
        int timeFrame = 100;
        for (Robot r : robots) {
            int x = (r.initPosition.getX() + (timeFrame * r.initVelocity.getX())) % maxX;
            if (x < 0) {
                x += maxX;
            }
            int y = (r.initPosition.getY() + (timeFrame * r.initVelocity.getY())) % maxY;
            if (y < 0) {
                y += maxY;
            }
            r.finalPosition = new Coord2D(x, y);
        }
        robots.sort((a, b) -> a.finalPosition.compareTo(b.finalPosition));
        LOG.debug("Robots: {}", robots);

        // Count the number of robots in each quadrant
        int[] counts = new int[4];
        for (Robot r : robots) {
            if ((r.finalPosition.getX() != midX) && (r.finalPosition.getY() != midY)) {
                int q = 0;
                if (r.finalPosition.getX() > midX) {
                    q = 2;
                }
                if (r.finalPosition.getY() > midY) {
                    counts[q + 1]++;
                } else {
                    counts[q]++;
                }
            }
        }

        long total = IntStream.of(counts)
                .asLongStream()
                .reduce(1, (a,b) -> a * b);
        LOG.info("total: {}", total);
    }
}
