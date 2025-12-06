package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day15Spot {
    EMPTY('.'),
    BOX('O'),
    BOX_LEFT('['),
    BOX_RIGHT(']'),
    ROBOT('@'),
    WALL('#');

    private final char symbol;

    private Day15Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day15Spot byChar(char in) {
        for (Day15Spot at : Day15Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
