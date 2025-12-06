package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day14Spot {
    EMPTY('.'),
    ROUND('O'),
    CUBE('#');

    private final char symbol;

    private Day14Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day14Spot byChar(char in) {
        for (Day14Spot at : Day14Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
