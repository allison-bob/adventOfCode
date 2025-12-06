package bob.aoc;

public enum Day11Spot {
    FLOOR('.'),
    EMPTY('L'),
    OCCUPIED('#');

    public char symbol;

    private Day11Spot(char symbol) {
        this.symbol = symbol;
    }

    public static Day11Spot byChar(char in) {
        for (Day11Spot at : Day11Spot.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw new IllegalArgumentException("Unknown Spot symbol: " + in);
    }
}
