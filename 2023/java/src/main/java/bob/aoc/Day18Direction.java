package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.util.Assert;

public enum Day18Direction {
    RIGHT('R', 1, 0),
    DOWN('D', 0, 1),
    LEFT('L', -1, 0),
    UP('U', 0, -1);

    public final char symbol;
    public final int dx;
    public final int dy;
    public final Coord2D offset;

    private Day18Direction(char symbol, int dx, int dy) {
        this.symbol = symbol;
        this.dx = dx;
        this.dy = dy;
        this.offset = new Coord2D(dx, dy);
    }

    public static Day18Direction byChar(char in) {
        for (Day18Direction at : Day18Direction.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Direction symbol: " + in);
    }
}
