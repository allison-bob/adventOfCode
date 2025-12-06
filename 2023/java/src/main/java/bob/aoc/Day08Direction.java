package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day08Direction {
    LEFT('L'),
    RIGHT('R');

    private final char symbol;

    private Day08Direction(char symbol) {
        this.symbol = symbol;
    }

    public static Day08Direction byChar(char in) {
        for (Day08Direction at : Day08Direction.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Direction symbol: " + in);
    }
}
