package bob.aoc;

public class Day05Line {

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Day05Line(String line) {
        if (line == null) {
            x1 = -1;
        } else {
            String[] coords = line.split(",| -> ");
            x1 = Integer.parseInt(coords[0]);
            y1 = Integer.parseInt(coords[1]);
            x2 = Integer.parseInt(coords[2]);
            y2 = Integer.parseInt(coords[3]);
        }
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public boolean isVertical() {
        return x1 == x2;
    }
    
    public int maxCoord() {
        return Math.max(x1, Math.max(x2, Math.max(y1, y2)));
    }

    @Override
    public String toString() {
        return x1 + "," + y1 + " -> " + x2 + "," + y2;
    }
}
