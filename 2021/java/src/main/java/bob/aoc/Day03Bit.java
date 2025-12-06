package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

public enum Day03Bit {
    OFF('0', 0),
    ON('1', 1);

    @Getter
    private final char symbol;
    @Getter
    private final int value;

    private Day03Bit(char symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static Day03Bit byChar(char in) {
        for (Day03Bit at : Day03Bit.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Bit symbol: " + in);
    }
}
