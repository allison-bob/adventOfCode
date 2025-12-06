package bob.aoc;

import bob.util.Assert;

public enum Day17Direction {

    LEFT('<'),
    RIGHT('>');

    private final char symbol;

    private Day17Direction(char symbol) {
        this.symbol = symbol;
    }

    public static Day17Direction byChar(char in) {
        for (Day17Direction at : Day17Direction.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
