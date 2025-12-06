package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day12Spring {
    WORKS('.'),
    UNKNOWN('?'),
    BROKEN('#');

    private final char symbol;

    private Day12Spring(char symbol) {
        this.symbol = symbol;
    }

    public static Day12Spring byChar(char in) {
        for (Day12Spring at : Day12Spring.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spring symbol: " + in);
    }
}
