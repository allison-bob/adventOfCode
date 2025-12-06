package bob.aoc;

import lombok.Getter;

@Getter
public enum Day23AmphipodType {
    AMBER('A', 2, 1),
    BRONZE('B', 4, 10),
    COPPER('C', 6, 100),
    DESERT('D', 8, 1000);

    private char symbol;
    private int homeX;
    private int costPerStep;

    private Day23AmphipodType(char symbol, int homeX, int costPerStep) {
        this.symbol = symbol;
        this.homeX = homeX;
        this.costPerStep = costPerStep;
    }

    public static Day23AmphipodType byChar(char in) {
        for (Day23AmphipodType at : Day23AmphipodType.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw new IllegalArgumentException("Unknown AmphipodType symbol: " + in);
    }
}
