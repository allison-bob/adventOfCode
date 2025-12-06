package bob.aoc;

import lombok.Getter;

@Getter
public enum Day20Pixel {
    OFF('.', 0),
    ON('#', 1);

    private final char symbol;
    private final int value;

    private Day20Pixel(char symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public static Day20Pixel byChar(char in) {
        for (Day20Pixel p : Day20Pixel.values()) {
            if (p.symbol == in) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown pixel symbol: " + in);
    }
}
