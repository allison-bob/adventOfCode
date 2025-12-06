package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day11Spot {
    EMPTY('.'),
    GALAXY('#');

    private final char symbol;

    private Day11Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day11Spot byChar(char in) {
        for (Day11Spot at : Day11Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
