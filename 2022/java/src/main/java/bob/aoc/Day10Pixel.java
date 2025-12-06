package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public enum Day10Pixel {
    OFF('.'),
    ON('#');

    private final char symbol;

    private Day10Pixel(char symbol) {
        this.symbol = symbol;
    }

    public static Day10Pixel byChar(char in) {
        for (Day10Pixel at : Day10Pixel.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Spot symbol: " + in);
    }
}
