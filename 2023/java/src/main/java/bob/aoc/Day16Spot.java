package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day16Spot {
    EMPTY('.'),
    MIRROR_D('\\'),
    MIRROR_U('/'),
    SPLIT_UD('|'),
    SPLIT_LR('-');

    private final char symbol;

    private Day16Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day16Spot byChar(char in) {
        for (Day16Spot at : Day16Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
