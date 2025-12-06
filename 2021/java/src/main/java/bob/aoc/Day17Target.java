package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public class Day17Target {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;

    public Day17Target(String line) {
        Assert.that((line.substring(0, 13).equals("target area: ")), "input prefix");

        // Parse the remainder of the line
        String[] bits = line.substring(13).split("=|\\.\\.|, ");
        Assert.that((bits[0].equals("x")), "x header");
        Assert.that((bits[3].equals("y")), "y header");

        // Save the values
        this.xMin = Integer.parseInt(bits[1]);
        this.xMax = Integer.parseInt(bits[2]);
        this.yMin = Integer.parseInt(bits[4]);
        this.yMax = Integer.parseInt(bits[5]);
    }

    public int doLaunch(int initX, int initY) {
        int x = 0;
        int y = 0;
        int dx = initX;
        int dy = initY;
        int maxY = 0;

        // Run steps until the probe drops below the target area
        while (y > yMin) {
            x += dx;
            y += dy;

            maxY = Math.max(maxY, y);
            if ((x > xMax) || (y < yMin)) {
                // Missed the target area
                return Integer.MIN_VALUE;
            }
            if ((x >= xMin) && (y <= yMax)) {
                // Hit the target area
                return maxY;
            }

            dx = Math.max((dx - 1), 0);
            dy--;
        }

        // Missed the target area
        return Integer.MIN_VALUE;
    }

    @Override
    public String toString() {
        return "x=" + xMin + ".." + xMax + ", y=" + yMin + ".." + yMax;
    }
}
