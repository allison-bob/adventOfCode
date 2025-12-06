package bob.aoc;

import lombok.Getter;

public enum Day20Pixel {
    BLACK('.'),
    WHITE('#');

    @Getter
    private final char symbol;

    private Day20Pixel(char symbol) {
        this.symbol = symbol;
    }

    public static Day20Pixel byChar(char in) {
        for (Day20Pixel at : Day20Pixel.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw new IllegalArgumentException("Unknown Pixel symbol: " + in);
    }
}
