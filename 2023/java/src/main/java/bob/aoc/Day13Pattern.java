package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day13Pattern {
    ASH('.'),
    ROCK('#');

    private final char symbol;

    private Day13Pattern(char symbol) {
        this.symbol = symbol;
    }

    public static Day13Pattern byChar(char in) {
        for (Day13Pattern at : Day13Pattern.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Pattern symbol: " + in);
    }
}
