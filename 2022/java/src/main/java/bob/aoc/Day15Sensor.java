package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.util.Assert;
import lombok.Getter;

@Getter
public class Day15Sensor {

    private final Coord2D pos;
    private final Coord2D beacon;
    private int coverLo;
    private int coverHi;

    public Day15Sensor(String line) {
        Assert.that(line.startsWith("Sensor at "), "wrong input line prefix");
        int p = line.indexOf(": closest beacon is at ");
        Assert.that((p > -1), "Delimiter not found");
        pos = parse(line.substring(10, p));
        beacon = parse(line.substring(p + 23));
    }

    private Coord2D parse(String line) {
        String[] bits = line.split(", ");
        Assert.that(bits[0].startsWith("x="), "Wrong x prefix");
        Assert.that(bits[1].startsWith("y="), "Wrong y prefix");
        return new Coord2D(Integer.parseInt(bits[0].substring(2)), Integer.parseInt(bits[1].substring(2)));
    }

    public int getDistance() {
        return pos.manhattan(beacon);
    }

    public boolean findCoverage(int y) {
        int dx = pos.manhattan(beacon) - Math.abs(y - pos.getY());
        coverLo = pos.getX() - dx;
        coverHi = pos.getX() + dx;
        return coverLo <= coverHi;
    }
}
