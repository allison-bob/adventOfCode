package bob.aoc;

import lombok.Getter;

@Getter
public enum Day25Spot {
    EMPTY('.'),
    EAST('>'),
    SOUTH('v');

    public char symbol;

    private Day25Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day25Spot byChar(char in) {
        for (Day25Spot at : Day25Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw new IllegalArgumentException("Unknown Spot symbol: " + in);
    }
}
