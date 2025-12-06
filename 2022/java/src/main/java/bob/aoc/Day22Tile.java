package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day22Tile {
    OPEN('.'),
    WALL('#'),
    OUT(' ');

    private final char symbol;

    private Day22Tile(char symbol) {
        this.symbol = symbol;
    }

    public static Day22Tile byChar(char in) {
        for (Day22Tile at : Day22Tile.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Tile symbol: " + in);
    }
}
