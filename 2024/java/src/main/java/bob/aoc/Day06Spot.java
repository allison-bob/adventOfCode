package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day06Spot {
    EMPTY('.'),
    GUARD('^'),
    VISITED('X'),
    OBSTRUCTION('#');

    private final char symbol;

    private Day06Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day06Spot byChar(char in) {
        for (Day06Spot at : Day06Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
