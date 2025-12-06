package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day15Direction {
    UP('^', new Coord2D(0, -1)),
    DOWN('v', new Coord2D(0, 1)),
    LEFT('<', new Coord2D(-1, 0)),
    RIGHT('>', new Coord2D(1, 0));

    private final char symbol;
    private final Coord2D offset;

    private Day15Direction(char symbol, Coord2D offset) {
        this.symbol = symbol;
        this.offset = offset;
    }

    public static Day15Direction byChar(char in) {
        for (Day15Direction at : Day15Direction.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Direction symbol: " + in);
    }
}
