package bob.aoc;

import bob.data.coordinate.Coord2D;

public class Day18DigPlan {

    public Day18Direction direction;
    public int distance;
    public int color;
    public Coord2D vertex;

    public Day18DigPlan(String line) {
        String[] bits = line.split(" ");
        direction = Day18Direction.byChar(bits[0].charAt(0));
        distance = Integer.parseInt(bits[1]);
        color = Integer.parseInt(bits[2].replaceAll("[()#]", ""), 16);
    }

    @Override
    public String toString() {
        return direction + ":" + distance;
    }
}
