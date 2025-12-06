package bob.aoc;

import bob.util.Assert;

public enum Day03Spot {
    EMPTY('.'),
    TREE('#');

    public char symbol;

    private Day03Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day03Spot byChar(char in) {
        for (Day03Spot at : Day03Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
