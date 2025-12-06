package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day25Digit {
    TWO('2', 2),
    ONE('1', 1),
    ZERO('0', 0),
    MINUS('-', -1),
    DBLMIN('=', -2);

    private final char symbol;
    private final int value;

    private Day25Digit(char symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static Day25Digit byChar(char in) {
        for (Day25Digit at : Day25Digit.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Digit symbol: " + in);
    }

    public static Day25Digit byValue(int in) {
        for (Day25Digit at : Day25Digit.values()) {
            if (at.value == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Digit value: " + in);
    }
}
