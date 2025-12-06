package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day24Blizzard {
    EMPTY('.', 0, 0),
    WALL('#', 0, 0),
    LEFT('<', -1, 0),
    RIGHT('>', 1, 0),
    UP('^', 0, -1),
    DOWN('v', 0, 1);

    private final char symbol;
    private final Coord2D move;

    private Day24Blizzard(char symbol, int dx, int dy) {
        this.symbol = symbol;
        this.move = new Coord2D(dx, dy);
    }

    public static Day24Blizzard byChar(char in) {
        for (Day24Blizzard at : Day24Blizzard.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Blizzard symbol: " + in);
    }
}
