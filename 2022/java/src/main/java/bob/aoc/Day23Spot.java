package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day23Spot {
    EMPTY('.'),
    ELF('#');

    private final char symbol;

    private Day23Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day23Spot byChar(char in) {
        for (Day23Spot at : Day23Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
